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

@WebServlet("/companyProducts")
public class companyproducts extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        // ==========================================
        // GET SESSION
        // ==========================================

        HttpSession session = request.getSession(false);

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

        // ==========================================
        // HTML START
        // ==========================================

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println(
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>"
        );

        out.println("<title>My Products - EcoCycle</title>");

        // CSS
        out.println(
                "<link rel='stylesheet' " +
                "href='companyProducts.css'>"
        );

        // ==========================================
        // EXTRA CSS FOR OUT OF STOCK
        // ==========================================

        out.println(
                "<style>" +

                ".company-out-of-stock {" +
                "background:#ffebee;" +
                "color:#c62828;" +
                "border:1px solid #ef9a9a;" +
                "padding:10px 15px;" +
                "border-radius:6px;" +
                "font-weight:bold;" +
                "text-align:center;" +
                "margin-top:10px;" +
                "}" +

                ".company-available {" +
                "background:#e8f5e9;" +
                "color:#2e7d32;" +
                "border:1px solid #a5d6a7;" +
                "padding:10px 15px;" +
                "border-radius:6px;" +
                "font-weight:bold;" +
                "text-align:center;" +
                "margin-top:10px;" +
                "}" +

                "</style>"
        );

        out.println("</head>");

        out.println("<body>");

        // ==========================================
        // NAVBAR
        // ==========================================

        out.println("<header class='navbar'>");

        out.println(
                "<div class='logo'>EcoCycle</div>"
        );

        out.println("<nav>");

        out.println(
                "<a href='companyHome.html'>Home</a>"
        );

        out.println(
                "<a href='companyProducts' class='active'>" +
                "My Products" +
                "</a>"
        );

        out.println(
                "<a href='companysell.html'>Sell Product</a>"
        );

        out.println(
                "<a href='#'>Orders</a>"
        );

        out.println(
                "<a href='#'>Contact</a>"
        );

        out.println(
                "<a href='companySignin.html'>Logout</a>"
        );

        out.println("</nav>");

        out.println("</header>");

        // ==========================================
        // MAIN
        // ==========================================

        out.println("<main class='container'>");

        // ==========================================
        // PAGE HEADING
        // ==========================================

        out.println("<div class='page-heading'>");

        out.println("<div>");

        out.println("<h1>My Products</h1>");

        out.println(
                "<p>" +
                "Manage the recycled products you are selling." +
                "</p>"
        );

        out.println("</div>");

        // ==========================================
        // ADD PRODUCT + SELLING STATUS
        // ==========================================

        out.println(
                "<div style='display:flex; gap:15px; " +
                "align-items:center;'>"
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

        out.println("</div>");

        // ==========================================
        // PRODUCT GRID
        // ==========================================

        out.println("<div class='product-grid'>");

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        boolean hasProducts = false;

        try {

            // ==========================================
            // ORACLE DATABASE CONNECTION
            // ==========================================

            Class.forName(
                    "oracle.jdbc.driver.OracleDriver"
            );

            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "system",
                    "manager"
            );

            // ==========================================
            // GET PRODUCTS OF LOGGED-IN COMPANY
            // ==========================================

            String sql =
                    "SELECT * FROM product_information " +
                    "WHERE COMPANY_REGNO = ? " +
                    "ORDER BY PRODUCT_ID DESC";

            statement = connection.prepareStatement(sql);

            statement.setString(
                    1,
                    companyRegno
            );

            rs = statement.executeQuery();

            // ==========================================
            // DISPLAY PRODUCTS
            // ==========================================

            while (rs.next()) {

                hasProducts = true;

                int productId =
                        rs.getInt("PRODUCT_ID");

                String productName =
                        rs.getString("PRODUCT_NAME");

                String productType =
                        rs.getString("PRODUCT_TYPE");

                String color =
                        rs.getString("COLOR");

                String quality =
                        rs.getString("QUALITY");

                double price =
                        rs.getDouble("PRICE");

                int quantity =
                        rs.getInt("QUANTITY");

                String description =
                        rs.getString("DESCRIPTION");

                String imageName =
                        rs.getString("PRODUCT_IMAGE");

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
                        "<div class='product-image-container'>"
                );

                out.println(
                        "<img src='productImages/" +
                        imageName +
                        "' " +
                        "alt='" +
                        productName +
                        "' " +
                        "class='product-image'>"
                );

                // ==========================================
                // STOCK STATUS
                // ==========================================

                if (quantity > 0) {

                    out.println(
                            "<span class='status-badge'>" +
                            "Available" +
                            "</span>"
                    );

                } else {

                    out.println(
                            "<span class='status-badge' " +
                            "style='background:#ffebee;" +
                            "color:#c62828;" +
                            "border:1px solid #ef9a9a;'>" +
                            "OUT OF STOCK" +
                            "</span>"
                    );
                }

                out.println("</div>");

                // ==========================================
                // DETAILS
                // ==========================================

                out.println(
                        "<div class='product-details'>"
                );

                out.println(
                        "<h2>" +
                        productName +
                        "</h2>"
                );

                out.println(
                        "<p class='product-type'>" +
                        productType +
                        "</p>"
                );

                // ==========================================
                // PRICE
                // ==========================================

                out.println(
                        "<div class='price'>" +
                        "₹" +
                        String.format(
                                "%.2f",
                                price
                        ) +
                        "</div>"
                );

                // ==========================================
                // PRODUCT INFORMATION
                // ==========================================

                out.println(
                        "<div class='product-info'>"
                );

                out.println(
                        "<div>" +
                        "<span>Color</span>" +
                        "<strong>" +
                        color +
                        "</strong>" +
                        "</div>"
                );

                out.println(
                        "<div>" +
                        "<span>Quality</span>" +
                        "<strong>" +
                        quality +
                        "</strong>" +
                        "</div>"
                );

                out.println(
                        "<div>" +
                        "<span>Quantity</span>" +
                        "<strong>" +
                        quantity +
                        "</strong>" +
                        "</div>"
                );

                out.println("</div>");

                // ==========================================
                // OUT OF STOCK / AVAILABLE MESSAGE
                // ==========================================

                if (quantity == 0) {

                    out.println(
                            "<div class='company-out-of-stock'>" +
                            "OUT OF STOCK" +
                            "</div>"
                    );

                } else {

                    out.println(
                            "<div class='company-available'>" +
                            "AVAILABLE" +
                            "</div>"
                    );
                }

                // ==========================================
                // DESCRIPTION
                // ==========================================

                out.println(
                        "<p class='description'>" +
                        description +
                        "</p>"
                );

                // ==========================================
                // PLATFORM FEE
                // ==========================================

                out.println(
                        "<div class='fee-info'>"
                );

                out.println(
                        "<span>ⓘ</span>"
                );

                out.println(
                        "<p>" +
                        "5% platform fee will be deducted " +
                        "from the selling price when this " +
                        "product is purchased." +
                        "</p>"
                );

                out.println("</div>");

                // ==========================================
                // BUTTONS
                // ==========================================

                out.println(
                        "<div class='card-actions'>"
                );

                out.println(
                        "<button class='remove-btn' " +
                        "onclick='removeProduct(" +
                        productId +
                        ")'>" +
                        "Remove" +
                        "</button>"
                );

                out.println("</div>");

                out.println("</div>");

                out.println("</div>");
            }

            // ==========================================
            // NO PRODUCTS
            // ==========================================

            if (!hasProducts) {

                out.println(
                        "<div class='empty-state'>"
                );

                out.println(
                        "<div class='empty-icon'>📦</div>"
                );

                out.println(
                        "<h2>No Products Entered Yet</h2>"
                );

                out.println(
                        "<p>" +
                        "You have not added any products " +
                        "for sale yet." +
                        "</p>"
                );

                out.println(
                        "<a href='companysell.html' " +
                        "class='add-product-btn'>" +
                        "Add Your First Product" +
                        "</a>"
                );

                out.println("</div>");
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<div class='error-message'>"
            );

            out.println(
                    "<h2>Something went wrong!</h2>"
            );

            out.println(
                    "<p>" +
                    e.getMessage() +
                    "</p>"
            );

            out.println("</div>");

        } finally {

            try {

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

        out.println("</div>");

        out.println("</main>");

        // ==========================================
        // FOOTER
        // ==========================================

        out.println("<footer>");

        out.println(
                "<p>" +
                "© 2026 EcoCycle. Building a cleaner future." +
                "</p>"
        );

        out.println("</footer>");

        // ==========================================
        // JAVASCRIPT
        // ==========================================

        out.println("<script>");

        out.println(
                "function editProduct(id) {" +
                "alert('Edit product ID: ' + id);" +
                "}"
        );

        out.println(
                "function removeProduct(id) {" +

                "if(confirm(" +
                "'Are you sure you want to remove this product?'" +
                ")) {" +

                "var form = document.createElement('form');" +

                "form.method = 'POST';" +

                "form.action = 'removeProduct';" +

                "var input = document.createElement('input');" +

                "input.type = 'hidden';" +

                "input.name = 'productId';" +

                "input.value = id;" +

                "form.appendChild(input);" +

                "document.body.appendChild(form);" +

                "form.submit();" +

                "}" +

                "}"
        );

        out.println("</script>");

        out.println("</body>");

        out.println("</html>");
    }
}