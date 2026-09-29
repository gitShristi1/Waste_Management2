

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/feedback")
public class userBuyFeedback extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ==========================================================
    // DATABASE DETAILS
    // ==========================================================

    private static final String DB_URL =
            "jdbc:oracle:thin:@localhost:1521:XE";

    private static final String DB_USER =
            "system";

    private static final String DB_PASSWORD =
            "manager";

    // ==========================================================
    // DO POST
    // ==========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        // ======================================================
        // GET LOGGED-IN USER SESSION
        // ======================================================

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("email") == null) {

            out.println("<h2>User session not found.</h2>");
            out.println("<p>Please sign in again.</p>");
            return;
        }

        // ======================================================
        // GET USER EMAIL
        // ======================================================

        String email = (String) session.getAttribute("email");

        // ======================================================
        // GET FORM DATA
        // ======================================================

        String name = request.getParameter("name");

        String ratingString =
                request.getParameter("rating");

        String feedback =
                request.getParameter("feedback");
        


        // ======================================================
        // CHECK TRANSACTION ID
        // ======================================================

        Integer productId =
        (Integer) session.getAttribute("purchasedProductId");

Integer transactionId =
        (Integer) session.getAttribute("purchasedTransactionId");


if (productId == null || transactionId == null) {

    out.println("<h2>Purchase information not found.</h2>");
    out.println("<p>Please purchase a product before submitting feedback.</p>");
    return;
}

        

        // ======================================================
        // VALIDATE FORM DATA
        // ======================================================

        if (name == null ||
                name.trim().isEmpty() ||

                ratingString == null ||
                ratingString.trim().isEmpty() ||

                feedback == null ||
                feedback.trim().isEmpty()) {

            out.println("<h2>All fields are required.</h2>");
            out.println("<p>Please go back and fill the feedback form.</p>");
            return;
        }

        // ======================================================
        // VALIDATE RATING
        // ======================================================

        int rating;

        try {

            rating = Integer.parseInt(ratingString);

        } catch (NumberFormatException e) {

            out.println("<h2>Invalid rating.</h2>");
            out.println("<p>Rating must be between 1 and 5.</p>");
            return;
        }

        if (rating < 1 || rating > 5) {

            out.println("<h2>Invalid rating.</h2>");
            out.println("<p>Rating must be between 1 and 5.</p>");
            return;
        }

        // ======================================================
        // DATABASE CONNECTION
        // ======================================================

        Connection con = null;

        
        PreparedStatement insertPS = null;

        

        try {

            // ==================================================
            // LOAD ORACLE DRIVER
            // ==================================================

            Class.forName("oracle.jdbc.driver.OracleDriver");

            // ==================================================
            // CONNECT TO ORACLE DATABASE
            // ==================================================

            con = DriverManager.getConnection(
                    DB_URL,
                    DB_USER,
                    DB_PASSWORD);

            
            // ==================================================
            // INSERT FEEDBACK (ORACLE)
            // ==================================================

            String sql =
    "INSERT INTO USER_BUY_FEEDBACK " +
    "(FEEDBACK_ID, NAME, RATING, FEEDBACK, PRODUCT_ID, TRANSACTION_ID) " +
    "VALUES (USER_BUY_FEEDBACK_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";
            insertPS = con.prepareStatement(sql);

insertPS.setString(1, name);

insertPS.setInt(2, rating);

insertPS.setString(3, feedback);

insertPS.setInt(4, productId);

insertPS.setInt(5, transactionId);

int result = insertPS.executeUpdate();

            // ==================================================
            // SUCCESS
            // ==================================================

            if (result > 0) {

                out.println("<!DOCTYPE html>");
                out.println("<html>");

                out.println("<head>");

                out.println("<meta charset='UTF-8'>");
                out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");

                out.println("<title>EcoCycle - Feedback</title>");

                out.println("<style>");

                out.println("body{font-family:Arial,sans-serif;background:#f4f8f5;margin:0;padding:0;}");

                out.println(".container{width:60%;margin:100px auto;background:white;padding:40px;border-radius:15px;box-shadow:0 4px 15px rgba(0,0,0,.1);text-align:center;}");

                out.println("h1{color:#31975b;}");

                out.println(".button{display:inline-block;padding:12px 25px;background:#31975b;color:white;text-decoration:none;border-radius:8px;margin-top:20px;}");

                out.println("</style>");

                out.println("</head>");

                out.println("<body>");

                out.println("<div class='container'>");

                out.println("<h1>Thank You!</h1>");

                out.println("<h2>Your feedback has been submitted successfully.</h2>");

                out.println("<p>Thank you for helping us improve EcoCycle.</p>");

                out.println("<a href='HomeUser.html' class='button'>Back to Home</a>");

                out.println("</div>");

                out.println("</body>");

                out.println("</html>");

            } else {

                out.println("<h2>Feedback could not be submitted.</h2>");

            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Database Error</h2>");

            out.println("<p>" + e.getMessage() + "</p>");

        } finally {

            

            try {
                if (insertPS != null)
                    insertPS.close();
            } catch (Exception ignored) {
            }

            try {
                if (con != null)
                    con.close();
            } catch (Exception ignored) {
            }

        }

    }

    // ==========================================================
    // DO GET
    // ==========================================================

    @Override
protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    response.sendRedirect("userBuyFeedback.html");

}
}