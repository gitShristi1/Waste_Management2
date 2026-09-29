import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/companyCustomerFeedback")
public class companyCustomerFeedback extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DB_URL =
            "jdbc:oracle:thin:@localhost:1521:XE";

    private static final String DB_USER =
            "system";

    private static final String DB_PASSWORD =
            "manager";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        // ==================================================
        // GET SESSION
        // ==================================================

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect("companySignin.html");
            return;
        }

        // ==================================================
        // GET COMPANY ID
        // ==================================================

        Object companyIdObject =
                session.getAttribute("companyId");

        if (companyIdObject == null) {

            response.sendRedirect("companySignin.html");
            return;
        }

        String companyId =
                companyIdObject.toString().trim();

        if (companyId.isEmpty()) {

            response.sendRedirect("companySignin.html");
            return;
        }

        // ==================================================
        // GET OPTIONAL PRODUCT ID
        // ==================================================

        String productIdString =
                request.getParameter("product_id");

        Integer productId = null;

        if (productIdString != null &&
                !productIdString.trim().isEmpty()) {

            try {

                productId =
                        Integer.parseInt(
                                productIdString.trim()
                        );

            } catch (NumberFormatException e) {

                out.println("<h2>Invalid Product ID.</h2>");
                out.println(
                        "<a href='companySellingStatus'>" +
                        "Back to Selling Status</a>"
                );

                return;
            }
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {

            // ==================================================
            // LOAD ORACLE DRIVER
            // ==================================================

            Class.forName(
                    "oracle.jdbc.driver.OracleDriver"
            );

            // ==================================================
            // DATABASE CONNECTION
            // ==================================================

            connection =
                    DriverManager.getConnection(
                            DB_URL,
                            DB_USER,
                            DB_PASSWORD
                    );

            // ==================================================
            // QUERY
            // ==================================================

            String sql;

            if (productId != null) {

                /*
                 * SHOW FEEDBACK FOR ONE PRODUCT
                 *
                 * f.product_id comes directly from
                 * USER_BUY_FEEDBACK.
                 *
                 * t.COMPANY_ID makes sure that the
                 * feedback belongs to this company.
                 */

                sql =
                        "SELECT " +
                        "f.FEEDBACK_ID, " +
                        "f.PRODUCT_ID, " +
                        "f.TRANSACTION_ID, " +
                        "f.NAME, " +
                        "f.RATING, " +
                        "f.FEEDBACK, " +
                        "f.FEEDBACK_DATE, " +
                        "t.PRODUCT_NAME, " +
                        "t.BUYER_EMAIL " +

                        "FROM USER_BUY_FEEDBACK f " +

                        "INNER JOIN TRANSACTIONS t " +
                        "ON f.TRANSACTION_ID = t.TRANSACTION_ID " +

                        "WHERE t.COMPANY_ID = ? " +
                        "AND f.PRODUCT_ID = ? " +

                        "ORDER BY f.FEEDBACK_DATE DESC";

                statement =
                        connection.prepareStatement(sql);

                statement.setString(
                        1,
                        companyId
                );

                statement.setInt(
                        2,
                        productId
                );

            } else {

                /*
                 * SHOW ALL FEEDBACK FOR THIS COMPANY
                 */

                sql =
                        "SELECT " +
                        "f.FEEDBACK_ID, " +
                        "f.PRODUCT_ID, " +
                        "f.TRANSACTION_ID, " +
                        "f.NAME, " +
                        "f.RATING, " +
                        "f.FEEDBACK, " +
                        "f.FEEDBACK_DATE, " +
                        "t.PRODUCT_NAME, " +
                        "t.BUYER_EMAIL " +

                        "FROM USER_BUY_FEEDBACK f " +

                        "INNER JOIN TRANSACTIONS t " +
                        "ON f.TRANSACTION_ID = t.TRANSACTION_ID " +

                        "WHERE t.COMPANY_ID = ? " +

                        "ORDER BY f.FEEDBACK_DATE DESC";

                statement =
                        connection.prepareStatement(sql);

                statement.setString(
                        1,
                        companyId
                );
            }

            rs =
                    statement.executeQuery();

            // ==================================================
            // HTML
            // ==================================================

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println(
                    "<meta name='viewport' " +
                    "content='width=device-width, " +
                    "initial-scale=1.0'>"
            );

            out.println(
                    "<title>Customer Feedback - EcoCycle</title>"
            );

            out.println("<style>");

            out.println(
                    "*{" +
                    "box-sizing:border-box;" +
                    "}"
            );

            out.println(
                    "body{" +
                    "margin:0;" +
                    "font-family:Arial,sans-serif;" +
                    "background:#f4f7f5;" +
                    "color:#222;" +
                    "}"
            );

            // ==================================================
            // NAVBAR
            // ==================================================

            out.println(
                    ".navbar{" +
                    "background:#1b5e20;" +
                    "color:white;" +
                    "padding:18px 40px;" +
                    "display:flex;" +
                    "justify-content:space-between;" +
                    "align-items:center;" +
                    "}"
            );

            out.println(
                    ".logo{" +
                    "font-size:26px;" +
                    "font-weight:bold;" +
                    "}"
            );

            out.println(
                    ".navbar a{" +
                    "color:white;" +
                    "text-decoration:none;" +
                    "margin-left:20px;" +
                    "}"
            );

            out.println(
                    ".navbar a:hover{" +
                    "text-decoration:underline;" +
                    "}"
            );

            // ==================================================
            // CONTAINER
            // ==================================================

            out.println(
                    ".container{" +
                    "width:94%;" +
                    "max-width:1200px;" +
                    "margin:40px auto;" +
                    "}"
            );

            out.println(
                    ".heading h1{" +
                    "color:#1b5e20;" +
                    "margin-bottom:8px;" +
                    "}"
            );

            out.println(
                    ".heading p{" +
                    "color:#666;" +
                    "}"
            );

            // ==================================================
            // FEEDBACK CARD
            // ==================================================

            out.println(
                    ".feedback-card{" +
                    "background:white;" +
                    "padding:25px;" +
                    "margin-bottom:25px;" +
                    "border-radius:15px;" +
                    "box-shadow:0 5px 20px rgba(0,0,0,0.08);" +
                    "}"
            );

            out.println(
                    ".feedback-header{" +
                    "display:flex;" +
                    "justify-content:space-between;" +
                    "align-items:center;" +
                    "gap:20px;" +
                    "flex-wrap:wrap;" +
                    "border-bottom:1px solid #eee;" +
                    "padding-bottom:15px;" +
                    "margin-bottom:15px;" +
                    "}"
            );

            out.println(
                    ".customer-name{" +
                    "font-size:20px;" +
                    "font-weight:bold;" +
                    "color:#1b5e20;" +
                    "}"
            );

            out.println(
                    ".rating{" +
                    "color:#f9a825;" +
                    "font-size:20px;" +
                    "}"
            );

            out.println(
                    ".details{" +
                    "background:#f7faf7;" +
                    "padding:15px;" +
                    "border-radius:10px;" +
                    "margin-bottom:15px;" +
                    "}"
            );

            out.println(
                    ".details p{" +
                    "margin:6px 0;" +
                    "}"
            );

            out.println(
                    ".feedback-text{" +
                    "font-size:16px;" +
                    "line-height:1.6;" +
                    "color:#444;" +
                    "}"
            );

            out.println(
                    ".date{" +
                    "font-size:13px;" +
                    "color:#888;" +
                    "margin-top:15px;" +
                    "}"
            );

            // ==================================================
            // NO FEEDBACK
            // ==================================================

            out.println(
                    ".no-feedback{" +
                    "background:white;" +
                    "padding:50px;" +
                    "text-align:center;" +
                    "border-radius:15px;" +
                    "box-shadow:0 5px 20px rgba(0,0,0,0.08);" +
                    "color:#777;" +
                    "}"
            );

            // ==================================================
            // BUTTON
            // ==================================================

            out.println(
                    ".back-btn{" +
                    "display:inline-block;" +
                    "padding:12px 25px;" +
                    "background:#1b5e20;" +
                    "color:white;" +
                    "text-decoration:none;" +
                    "border-radius:8px;" +
                    "font-weight:bold;" +
                    "margin-top:10px;" +
                    "}"
            );

            out.println(
                    ".back-btn:hover{" +
                    "background:#124116;" +
                    "}"
            );

            out.println("</style>");

            out.println("</head>");

            // ==================================================
            // BODY
            // ==================================================

            out.println("<body>");

            out.println(
                    "<header class='navbar'>"
            );

            out.println(
                    "<div class='logo'>EcoCycle</div>"
            );

            out.println("<nav>");

            out.println(
                    "<a href='companyHome.html'>Home</a>"
            );

            out.println(
                    "<a href='companyProducts'>My Products</a>"
            );

            out.println(
                    "<a href='companySellingStatus'>" +
                    "Selling Status</a>"
            );

            out.println(
                    "<a href='companySignin.html'>Logout</a>"
            );

            out.println("</nav>");

            out.println("</header>");

            // ==================================================
            // MAIN
            // ==================================================

            out.println(
                    "<main class='container'>"
            );

            out.println(
                    "<div class='heading'>"
            );

            if (productId != null) {

                out.println(
                        "<h1>Customer Feedback for Product</h1>"
                );

                out.println(
                        "<p>" +
                        "Feedback from customers who purchased " +
                        "this product." +
                        "</p>"
                );

            } else {

                out.println(
                        "<h1>All Customer Feedback</h1>"
                );

                out.println(
                        "<p>" +
                        "Feedback from customers who purchased " +
                        "your company's products." +
                        "</p>"
                );
            }

            out.println("</div>");

            boolean found = false;

            // ==================================================
            // DISPLAY FEEDBACK
            // ==================================================

            while (rs.next()) {

                found = true;

                int feedbackId =
                        rs.getInt("FEEDBACK_ID");

                int resultProductId =
                        rs.getInt("PRODUCT_ID");

                int transactionId =
                        rs.getInt("TRANSACTION_ID");

                String customerName =
                        rs.getString("NAME");

                int rating =
                        rs.getInt("RATING");

                String feedback =
                        rs.getString("FEEDBACK");

                String feedbackDate =
                        rs.getString("FEEDBACK_DATE");

                String productName =
                        rs.getString("PRODUCT_NAME");

                String buyerEmail =
                        rs.getString("BUYER_EMAIL");

                // ==================================================
                // FEEDBACK CARD
                // ==================================================

                out.println(
                        "<div class='feedback-card'>"
                );

                out.println(
                        "<div class='feedback-header'>"
                );

                out.println(
                        "<div class='customer-name'>" +
                        "👤 " +
                        customerName +
                        "</div>"
                );

                out.println(
                        "<div class='rating'>"
                );

                for (int i = 1; i <= 5; i++) {

                    if (i <= rating) {

                        out.println("★");

                    } else {

                        out.println("☆");
                    }
                }

                out.println(
                        " (" +
                        rating +
                        "/5)"
                );

                out.println("</div>");
                out.println("</div>");

                // ==================================================
                // PURCHASE DETAILS
                // ==================================================

                out.println(
                        "<div class='details'>"
                );

                out.println(
                        "<p><strong>Product:</strong> " +
                        productName +
                        "</p>"
                );

                out.println(
                        "<p><strong>Product ID:</strong> " +
                        resultProductId +
                        "</p>"
                );

                out.println(
                        "<p><strong>Transaction ID:</strong> " +
                        transactionId +
                        "</p>"
                );

                out.println(
                        "<p><strong>Buyer Email:</strong> " +
                        buyerEmail +
                        "</p>"
                );

                out.println("</div>");

                // ==================================================
                // FEEDBACK
                // ==================================================

                out.println(
                        "<div class='feedback-text'>"
                );

                out.println(
                        "<strong>Customer Feedback:</strong>"
                );

                out.println(
                        "<p>" +
                        feedback +
                        "</p>"
                );

                out.println("</div>");

                // ==================================================
                // DATE
                // ==================================================

                if (feedbackDate != null) {

                    out.println(
                            "<div class='date'>" +
                            "Submitted: " +
                            feedbackDate +
                            "</div>"
                    );
                }

                out.println("</div>");
            }

            // ==================================================
            // NO FEEDBACK
            // ==================================================

            if (!found) {

                out.println(
                        "<div class='no-feedback'>"
                );

                out.println(
                        "<div style='font-size:45px;'>💬</div>"
                );

                out.println(
                        "<h2>No Customer Feedback Yet</h2>"
                );

                if (productId != null) {

                    out.println(
                            "<p>" +
                            "No customer feedback has been submitted " +
                            "for this product yet." +
                            "</p>"
                    );

                } else {

                    out.println(
                            "<p>" +
                            "No customer feedback has been submitted " +
                            "for any of your products yet." +
                            "</p>"
                    );
                }

                out.println("</div>");
            }

            // ==================================================
            // BUTTONS
            // ==================================================

            out.println(
                    "<div style='text-align:center;" +
                    "margin-top:30px;'>"
            );

            if (productId != null) {

                out.println(
                        "<a href='companyCustomerFeedback' " +
                        "class='back-btn'>" +
                        "View All Customer Feedback" +
                        "</a>"
                );

                out.println("&nbsp;&nbsp;");
            }

            out.println(
                    "<a href='companySellingStatus' " +
                    "class='back-btn'>" +
                    "← Back to Selling Status" +
                    "</a>"
            );

            out.println("</div>");

            out.println("</main>");

            // ==================================================
            // FOOTER
            // ==================================================

            out.println(
                    "<footer style='" +
                    "text-align:center;" +
                    "padding:25px;" +
                    "color:#777;'>" +
                    "© 2026 EcoCycle. All rights reserved." +
                    "</footer>"
            );

            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<h2>Error loading customer feedback</h2>"
            );

            out.println(
                    "<p>" +
                    e.getMessage() +
                    "</p>"
            );

        } finally {

            try {

                if (rs != null)
                    rs.close();

            } catch (Exception ignored) {
            }

            try {

                if (statement != null)
                    statement.close();

            } catch (Exception ignored) {
            }

            try {

                if (connection != null)
                    connection.close();

            } catch (Exception ignored) {
            }
        }
    }
}