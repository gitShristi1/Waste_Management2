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

@WebServlet("/companySellingStatus")
public class companySellingStatus extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        // ==========================================
        // GET COMPANY SESSION
        // ==========================================

        HttpSession session =
                request.getSession(false);

        // ==========================================
        // CHECK SESSION
        // ==========================================

        if (session == null) {

            response.sendRedirect("companySignin.html");

            return;
        }

        // ==========================================
        // GET COMPANY REGISTRATION NUMBER
        // ==========================================

        String companyRegno =
                (String) session.getAttribute("companyRegno");

        // ==========================================
        // CHECK COMPANY LOGIN
        // ==========================================

        if (companyRegno == null ||
                companyRegno.trim().isEmpty()) {

            response.sendRedirect("companySignin.html");

            return;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement companyStatement = null;

        ResultSet rs = null;
        ResultSet companyResult = null;

        try {

            // ==========================================
            // LOAD ORACLE DRIVER
            // ==========================================

            Class.forName("oracle.jdbc.driver.OracleDriver");

            // ==========================================
            // DATABASE CONNECTION
            // ==========================================

            connection =
                    DriverManager.getConnection(
                            "jdbc:oracle:thin:@localhost:1521:xe",
                            "system",
                            "manager"
                    );

            // ==========================================
            // GET COMPANY NAME
            // ==========================================

            String companyName = "";

            String companySQL =
                    "SELECT company " +
                    "FROM companysignin " +
                    "WHERE regno = ?";

            companyStatement =
                    connection.prepareStatement(companySQL);

            companyStatement.setString(
                    1,
                    companyRegno
            );

            companyResult =
                    companyStatement.executeQuery();

            if (companyResult.next()) {

                companyName =
                        companyResult.getString("company");
            }

            companyResult.close();
            companyResult = null;

            companyStatement.close();
            companyStatement = null;

            // ==========================================
            // GET SELLING STATUS
            // ==========================================

            String sql =
                    "SELECT " +
                    "p.product_id, " +
                    "p.product_name, " +
                    "p.product_type, " +
                    "p.color, " +
                    "p.quality, " +
                    "p.price, " +
                    "p.quantity, " +
                    "p.product_image, " +
                    "NVL(SUM(t.quantity), 0) " +
                    "AS sold_quantity, " +
                    "NVL(SUM(t.total_amount), 0) " +
                    "AS total_sales, " +
                    "NVL(SUM(t.commission), 0) " +
                    "AS total_commission, " +
                    "NVL(SUM(t.company_amount), 0) " +
                    "AS company_earnings " +
                    "FROM product_information p " +
                    "LEFT JOIN transactions t " +
                    "ON p.product_id = t.product_id " +
                    "AND t.COMPANY_REGNO = p.COMPANY_REGNO " +
                    "WHERE p.COMPANY_REGNO = ? " +
                    "GROUP BY " +
                    "p.product_id, " +
                    "p.product_name, " +
                    "p.product_type, " +
                    "p.color, " +
                    "p.quality, " +
                    "p.price, " +
                    "p.quantity, " +
                    "p.product_image " +
                    "ORDER BY p.product_id DESC";

            statement =
                    connection.prepareStatement(sql);

            statement.setString(
                    1,
                    companyRegno
            );

            rs =
                    statement.executeQuery();

            // ==========================================
            // START HTML
            // ==========================================

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");

            out.println("<head>");

            out.println(
                    "<meta charset='UTF-8'>"
            );

            out.println(
                    "<meta name='viewport' " +
                    "content='width=device-width, " +
                    "initial-scale=1.0'>"
            );

            out.println(
                    "<title>Selling Status - EcoCycle</title>"
            );

            // ==========================================
            // CSS
            // ==========================================

            out.println("<style>");

            out.println(
                    "* { " +
                    "box-sizing: border-box; " +
                    "}"
            );

            out.println(
                    "body { " +
                    "margin: 0; " +
                    "font-family: Arial, sans-serif; " +
                    "background: #f4f7f5; " +
                    "color: #222; " +
                    "}"
            );

            // ==========================================
            // NAVBAR
            // ==========================================

            out.println(
                    ".navbar { " +
                    "background: #1b5e20; " +
                    "color: white; " +
                    "padding: 18px 40px; " +
                    "display: flex; " +
                    "justify-content: space-between; " +
                    "align-items: center; " +
                    "}"
            );

            out.println(
                    ".logo { " +
                    "font-size: 26px; " +
                    "font-weight: bold; " +
                    "}"
            );

            out.println(
                    ".navbar a { " +
                    "color: white; " +
                    "text-decoration: none; " +
                    "margin-left: 20px; " +
                    "font-size: 15px; " +
                    "}"
            );

            out.println(
                    ".navbar a:hover { " +
                    "text-decoration: underline; " +
                    "}"
            );

            // ==========================================
            // CONTAINER
            // ==========================================

            out.println(
                    ".container { " +
                    "width: 94%; " +
                    "max-width: 1400px; " +
                    "margin: 35px auto; " +
                    "}"
            );

            // ==========================================
            // PAGE HEADING
            // ==========================================

            out.println(
                    ".heading { " +
                    "margin-bottom: 25px; " +
                    "}"
            );

            out.println(
                    ".heading h1 { " +
                    "margin: 0; " +
                    "color: #1b5e20; " +
                    "}"
            );

            out.println(
                    ".heading p { " +
                    "color: #666; " +
                    "}"
            );

            // ==========================================
            // SUMMARY CARDS
            // ==========================================

            out.println(
                    ".summary { " +
                    "display: grid; " +
                    "grid-template-columns: repeat(4, 1fr); " +
                    "gap: 20px; " +
                    "margin-bottom: 35px; " +
                    "}"
            );

            out.println(
                    ".summary-card { " +
                    "background: white; " +
                    "padding: 25px; " +
                    "border-radius: 12px; " +
                    "box-shadow: 0 3px 12px rgba(0,0,0,0.10); " +
                    "text-align: center; " +
                    "}"
            );

            out.println(
                    ".summary-card h3 { " +
                    "margin: 0 0 12px; " +
                    "font-size: 16px; " +
                    "color: #666; " +
                    "}"
            );

            out.println(
                    ".summary-card .value { " +
                    "font-size: 28px; " +
                    "font-weight: bold; " +
                    "color: #1b5e20; " +
                    "}"
            );

            // ==========================================
            // PRODUCT GRID
            // ==========================================

            out.println(
                    ".product-grid { " +
                    "display: grid; " +
                    "grid-template-columns: repeat(auto-fit, minmax(330px, 1fr)); " +
                    "gap: 25px; " +
                    "}"
            );

            // ==========================================
            // PRODUCT CARD
            // ==========================================

            out.println(
                    ".product-card { " +
                    "background: white; " +
                    "border-radius: 14px; " +
                    "overflow: hidden; " +
                    "box-shadow: 0 4px 15px rgba(0,0,0,0.10); " +
                    "}"
            );

            out.println(
                    ".product-image { " +
                    "width: 100%; " +
                    "height: 220px; " +
                    "object-fit: cover; " +
                    "}"
            );

            out.println(
                    ".product-body { " +
                    "padding: 22px; " +
                    "}"
            );

            out.println(
                    ".product-body h2 { " +
                    "margin-top: 0; " +
                    "color: #1b5e20; " +
                    "}"
            );

            out.println(
                    ".product-type { " +
                    "color: #777; " +
                    "margin-bottom: 18px; " +
                    "}"
            );

            // ==========================================
            // INFORMATION
            // ==========================================

            out.println(
                    ".info { " +
                    "display: grid; " +
                    "grid-template-columns: 1fr 1fr; " +
                    "gap: 12px; " +
                    "margin-bottom: 20px; " +
                    "}"
            );

            out.println(
                    ".info-box { " +
                    "background: #f5f7f6; " +
                    "padding: 12px; " +
                    "border-radius: 8px; " +
                    "}"
            );

            out.println(
                    ".info-box span { " +
                    "display: block; " +
                    "font-size: 13px; " +
                    "color: #777; " +
                    "margin-bottom: 5px; " +
                    "}"
            );

            out.println(
                    ".info-box strong { " +
                    "font-size: 17px; " +
                    "color: #222; " +
                    "}"
            );

            // ==========================================
            // SELLING SECTION
            // ==========================================

            out.println(
                    ".selling-box { " +
                    "background: #e8f5e9; " +
                    "border-radius: 10px; " +
                    "padding: 18px; " +
                    "margin-top: 15px; " +
                    "}"
            );

            out.println(
                    ".selling-box h3 { " +
                    "margin-top: 0; " +
                    "color: #1b5e20; " +
                    "}"
            );

            out.println(
                    ".selling-row { " +
                    "display: flex; " +
                    "justify-content: space-between; " +
                    "padding: 7px 0; " +
                    "border-bottom: 1px solid #c8e6c9; " +
                    "}"
            );

            out.println(
                    ".selling-row:last-child { " +
                    "border-bottom: none; " +
                    "}"
            );

            out.println(
                    ".earned { " +
                    "font-size: 20px; " +
                    "font-weight: bold; " +
                    "color: #1b5e20; " +
                    "}"
            );

            // ==========================================
            // STATUS
            // ==========================================

            out.println(
                    ".status { " +
                    "display: inline-block; " +
                    "padding: 7px 12px; " +
                    "border-radius: 20px; " +
                    "font-size: 13px; " +
                    "font-weight: bold; " +
                    "}"
            );

            out.println(
                    ".sold { " +
                    "background: #ffebee; " +
                    "color: #c62828; " +
                    "}"
            );

            out.println(
                    ".available { " +
                    "background: #e8f5e9; " +
                    "color: #2e7d32; " +
                    "}"
            );

            // ==========================================
            // EMPTY STATE
            // ==========================================

            out.println(
                    ".empty { " +
                    "background: white; " +
                    "padding: 60px 20px; " +
                    "text-align: center; " +
                    "border-radius: 14px; " +
                    "box-shadow: 0 3px 12px rgba(0,0,0,0.10); " +
                    "}"
            );

            out.println(
                    ".empty-icon { " +
                    "font-size: 60px; " +
                    "margin-bottom: 15px; " +
                    "}"
            );

            out.println(
                    ".empty h2 { " +
                    "color: #555; " +
                    "}"
            );

            out.println(
                    ".back-btn { " +
                    "display: inline-block; " +
                    "margin-top: 25px; " +
                    "padding: 12px 22px; " +
                    "background: #1b5e20; " +
                    "color: white; " +
                    "text-decoration: none; " +
                    "border-radius: 7px; " +
                    "}"
            );

            // ==========================================
            // FEEDBACK SECTION
            // ==========================================

            out.println(
                    ".feedback-section { " +
                    "background: white; " +
                    "padding: 35px; " +
                    "border-radius: 14px; " +
                    "box-shadow: 0 4px 15px rgba(0,0,0,0.10); " +
                    "margin-top: 40px; " +
                    "text-align: center; " +
                    "}"
            );

            out.println(
                    ".feedback-icon { " +
                    "font-size: 38px; " +
                    "margin-bottom: 10px; " +
                    "}"
            );

            out.println(
                    ".feedback-section h2 { " +
                    "margin: 0 0 12px; " +
                    "color: #1b5e20; " +
                    "font-size: 25px; " +
                    "}"
            );

            out.println(
                    ".feedback-section p { " +
                    "color: #666; " +
                    "font-size: 15px; " +
                    "line-height: 1.6; " +
                    "max-width: 700px; " +
                    "margin: 0 auto 22px; " +
                    "}"
            );

            out.println(
                    ".feedback-btn { " +
                    "display: inline-block; " +
                    "padding: 13px 28px; " +
                    "background: #2e7d32; " +
                    "color: white; " +
                    "text-decoration: none; " +
                    "border-radius: 8px; " +
                    "font-size: 15px; " +
                    "font-weight: bold; " +
                    "transition: 0.3s; " +
                    "}"
            );

            out.println(
                    ".feedback-btn:hover { " +
                    "background: #1b5e20; " +
                    "transform: translateY(-2px); " +
                    "}"
            );

            // ==========================================
            // RESPONSIVE
            // ==========================================

            out.println(
                    "@media(max-width: 900px) { " +
                    ".summary { " +
                    "grid-template-columns: 1fr 1fr; " +
                    "} " +
                    "}"
            );

            out.println(
                    "@media(max-width: 550px) { " +
                    ".summary { " +
                    "grid-template-columns: 1fr; " +
                    "} " +
                    ".navbar { " +
                    "flex-direction: column; " +
                    "gap: 15px; " +
                    "} " +
                    ".feedback-section { " +
                    "padding: 25px 18px; " +
                    "} " +
                    "}"
            );

            out.println("</style>");

            out.println("</head>");

            // ==========================================
            // BODY
            // ==========================================

            out.println("<body>");

            // ==========================================
            // NAVBAR
            // ==========================================

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
                    "<a href='companySellingStatus'>Selling Status</a>"
            );

            out.println(
                    "<a href='companySignin.html'>Logout</a>"
            );

            out.println("</nav>");

            out.println("</header>");

            // ==========================================
            // MAIN CONTAINER
            // ==========================================

            out.println(
                    "<main class='container'>"
            );

            // ==========================================
            // HEADING
            // ==========================================

            out.println(
                    "<div class='heading'>"
            );

            out.println(
                    "<h1>Selling Status</h1>"
            );

            out.println(
                    "<p>" +
                    companyName +
                    " | Registration No: " +
                    companyRegno +
                    "</p>"
            );

            out.println("</div>");

            // ==========================================
            // VARIABLES FOR TOTALS
            // ==========================================

            int totalSold = 0;
            double totalSales = 0;
            double totalCommission = 0;
            double totalEarnings = 0;

            boolean hasProducts = false;

            // ==========================================
            // PRODUCT GRID START
            // ==========================================

            out.println(
                    "<div class='product-grid'>"
            );

            while (rs.next()) {

                hasProducts = true;

                // ==========================================
                // PRODUCT INFORMATION
                // ==========================================

                int productId =
                        rs.getInt("product_id");

                String productName =
                        rs.getString("product_name");

                String productType =
                        rs.getString("product_type");

                String color =
                        rs.getString("color");

                String quality =
                        rs.getString("quality");

                double price =
                        rs.getDouble("price");

                int quantity =
                        rs.getInt("quantity");

                String image =
                        rs.getString("product_image");

                // ==========================================
                // SELLING INFORMATION
                // ==========================================

                int soldQuantity =
                        rs.getInt("sold_quantity");

                double sales =
                        rs.getDouble("total_sales");

                double commission =
                        rs.getDouble("total_commission");

                double earnings =
                        rs.getDouble("company_earnings");

                // ==========================================
                // ADD TO COMPANY TOTALS
                // ==========================================

                totalSold += soldQuantity;
                totalSales += sales;
                totalCommission += commission;
                totalEarnings += earnings;

                // ==========================================
                // PRODUCT CARD
                // ==========================================

                out.println(
                        "<div class='product-card'>"
                );

                // ==========================================
                // IMAGE
                // ==========================================

                out.println(
                        "<img class='product-image' " +
                        "src='productImages/" +
                        image +
                        "' " +
                        "alt='" +
                        productName +
                        "'>"
                );

                // ==========================================
                // PRODUCT BODY
                // ==========================================

                out.println(
                        "<div class='product-body'>"
                );

                out.println(
                        "<h2>" +
                        productName +
                        "</h2>"
                );

                out.println(
                        "<div class='product-type'>" +
                        productType +
                        "</div>"
                );

                // ==========================================
                // BASIC INFORMATION
                // ==========================================

                out.println(
                        "<div class='info'>"
                );

                out.println(
                        "<div class='info-box'>" +
                        "<span>Product ID</span>" +
                        "<strong>" +
                        productId +
                        "</strong>" +
                        "</div>"
                );

                out.println(
                        "<div class='info-box'>" +
                        "<span>Price</span>" +
                        "<strong>₹" +
                        String.format("%.2f", price) +
                        "</strong>" +
                        "</div>"
                );

                out.println(
                        "<div class='info-box'>" +
                        "<span>Color</span>" +
                        "<strong>" +
                        color +
                        "</strong>" +
                        "</div>"
                );

                out.println(
                        "<div class='info-box'>" +
                        "<span>Quality</span>" +
                        "<strong>" +
                        quality +
                        "</strong>" +
                        "</div>"
                );

                out.println(
                        "<div class='info-box'>" +
                        "<span>Remaining Quantity</span>" +
                        "<strong>" +
                        quantity +
                        "</strong>" +
                        "</div>"
                );

                out.println(
                        "<div class='info-box'>" +
                        "<span>Products Sold</span>" +
                        "<strong>" +
                        soldQuantity +
                        "</strong>" +
                        "</div>"
                );

                out.println("</div>");

                // ==========================================
                // SELLING INFORMATION
                // ==========================================

                out.println(
                        "<div class='selling-box'>"
                );

                out.println(
                        "<h3>Selling Information</h3>"
                );

                out.println(
                        "<div class='selling-row'>" +
                        "<span>Total Sales</span>" +
                        "<strong>₹" +
                        String.format("%.2f", sales) +
                        "</strong>" +
                        "</div>"
                );

                out.println(
                        "<div class='selling-row'>" +
                        "<span>Admin Fee (5%)</span>" +
                        "<strong>₹" +
                        String.format("%.2f", commission) +
                        "</strong>" +
                        "</div>"
                );

                out.println(
                        "<div class='selling-row'>" +
                        "<span>Company Earnings (95%)</span>" +
                        "<strong class='earned'>₹" +
                        String.format("%.2f", earnings) +
                        "</strong>" +
                        "</div>"
                );

                out.println("</div>");

                // ==========================================
                // PRODUCT STATUS
                // ==========================================

                if (quantity == 0) {

                    out.println(
                            "<span class='status sold'>" +
                            "Sold Out" +
                            "</span>"
                    );

                } else if (soldQuantity > 0) {

                    out.println(
                            "<span class='status available'>" +
                            soldQuantity +
                            " Sold • Available" +
                            "</span>"
                    );

                } else {

                    out.println(
                            "<span class='status available'>" +
                            "Not Sold Yet" +
                            "</span>"
                    );
                }

                out.println("</div>");
                out.println("</div>");
            }

            out.println("</div>");

            // ==========================================
            // SUMMARY
            // ==========================================

            out.println(
                    "<div class='summary' " +
                    "style='margin-top:35px;'>"
            );

            out.println(
                    "<div class='summary-card'>" +
                    "<h3>Total Products Sold</h3>" +
                    "<div class='value'>" +
                    totalSold +
                    "</div>" +
                    "</div>"
            );

            out.println(
                    "<div class='summary-card'>" +
                    "<h3>Total Sales</h3>" +
                    "<div class='value'>₹" +
                    String.format("%.2f", totalSales) +
                    "</div>" +
                    "</div>"
            );

            out.println(
                    "<div class='summary-card'>" +
                    "<h3>Admin Commission</h3>" +
                    "<div class='value'>₹" +
                    String.format("%.2f", totalCommission) +
                    "</div>" +
                    "</div>"
            );

            out.println(
                    "<div class='summary-card'>" +
                    "<h3>Total Company Earnings</h3>" +
                    "<div class='value'>₹" +
                    String.format("%.2f", totalEarnings) +
                    "</div>" +
                    "</div>"
            );

            out.println("</div>");

            // ==========================================
            // NO PRODUCTS
            // ==========================================

            if (!hasProducts) {

                out.println(
                        "<div class='empty'>"
                );

                out.println(
                        "<div class='empty-icon'>📦</div>"
                );

                out.println(
                        "<h2>No Products Are Purchased Yet</h2>"
                );

                out.println(
                        "<p>" +
                        "You have not received any product " +
                        "purchases yet." +
                        "</p>"
                );

                out.println(
                        "<a class='back-btn' " +
                        "href='companyHome.html'>" +
                        "Back to Company Home" +
                        "</a>"
                );

                out.println("</div>");
            }

            // ==========================================
            // COMPANY FEEDBACK SECTION
            // ==========================================

            out.println(
                    "<div class='feedback-section'>"
            );

            out.println(
                    "<div class='feedback-icon'>💚</div>"
            );

            out.println(
                    "<h2>Help Us Make EcoCycle Better</h2>"
            );

            out.println(
                    "<p>" +
                    "Your feedback will help us make our platform " +
                    "better. Share your experience, suggestions, " +
                    "or any problems you faced while using EcoCycle." +
                    "</p>"
            );

            out.println(
                    "<a href='companyfeedback.html' " +
                    "class='feedback-btn'>" +
                    "Give Your Feedback" +
                    "</a>"
            );

            out.println("</div>");

            // ==========================================
            // BACK BUTTON
            // ==========================================

            out.println(
                    "<a class='back-btn' " +
                    "href='companyHome.html'>" +
                    "← Back to Company Home" +
                    "</a>"
            );

            out.println("</main>");

            // ==========================================
            // FOOTER
            // ==========================================

            out.println(
                    "<footer style='text-align:center;" +
                    "padding:25px;color:#777;'>" +
                    "© 2026 EcoCycle. Building a cleaner future." +
                    "</footer>"
            );

            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<html><body>"
            );

            out.println(
                    "<h2>Something went wrong!</h2>"
            );

            out.println(
                    "<p>" +
                    e.getMessage() +
                    "</p>"
            );

            out.println(
                    "<div style='display:flex; gap:15px;'>"
            );

            out.println(
                    "<a href='companysell.html' " +
                    "class='add-product-btn'>" +
                    "+ Add Product" +
                    "</a>"
            );

            out.println(
                    "<a href='companySellingStatus' " +
                    "class='add-product-btn'>" +
                    "Selling Status" +
                    "</a>"
            );

            out.println("</div>");

            out.println(
                    "</body></html>"
            );

        } finally {

            try {

                if (companyResult != null) {
                    companyResult.close();
                }

                if (companyStatement != null) {
                    companyStatement.close();
                }

                if (rs != null) {
                    rs.close();
                }

                if (statement != null) {
                    statement.close();
                }

                if (connection != null) {
                    connection.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}