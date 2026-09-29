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

@WebServlet("/companyproducts")
public class companyproducts extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        // ==============================
        // GET SESSION
        // ==============================

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("companySignin.html");
            return;
        }

        String companyId =
                (String) session.getAttribute("companyId");

        if (companyId == null || companyId.trim().isEmpty()) {
            response.sendRedirect("companySignin.html");
            return;
        }

        // ==============================
        // HTML START
        // ==============================

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");

        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>EcoCycle - My Products</title>");

        // ==============================
        // CSS
        // ==============================

        out.println("<style>");

        out.println("* {");
        out.println("    margin: 0;");
        out.println("    padding: 0;");
        out.println("    box-sizing: border-box;");
        out.println("}");

        out.println("body {");
        out.println("    font-family: Arial, Helvetica, sans-serif;");
        out.println("    background: #f5f8f5;");
        out.println("    color: #26382b;");
        out.println("}");

        // NAVBAR
        out.println(".navbar {");
        out.println("    height: 72px;");
        out.println("    background: white;");
        out.println("    border-bottom: 1px solid #e1e8e2;");
        out.println("    display: flex;");
        out.println("    align-items: center;");
        out.println("    justify-content: space-between;");
        out.println("    padding: 0 7%;");
        out.println("}");

        // LOGO
        out.println(".logo {");
        out.println("    font-size: 28px;");
        out.println("    font-weight: 800;");
        out.println("}");

        out.println(".logo .eco {");
        out.println("    color: #111111;");
        out.println("}");

        out.println(".logo .cycle {");
        out.println("    color: green;");
        out.println("}");

        // NAV LINKS
        out.println(".navbar nav {");
        out.println("    display: flex;");
        out.println("    align-items: center;");
        out.println("    gap: 30px;");
        out.println("}");

        out.println(".navbar nav a {");
        out.println("    text-decoration: none;");
        out.println("    color: #4d514e;");
        out.println("    font-size: 14px;");
        out.println("    font-weight: 600;");
        out.println("}");

        out.println(".navbar nav a:hover {");
        out.println("    color: green;");
        out.println("}");

        // MAIN
        out.println(".container {");
        out.println("    width: 82%;");
        out.println("    max-width: 1250px;");
        out.println("    margin: 45px auto 70px;");
        out.println("}");

        // HEADING
        out.println(".page-heading {");
        out.println("    text-align: center;");
        out.println("    margin-bottom: 40px;");
        out.println("}");

        out.println(".page-heading h1 {");
        out.println("    font-size: 38px;");
        out.println("    color: #222222;");
        out.println("    margin-bottom: 10px;");
        out.println("}");

        out.println(".page-heading h1 span {");
        out.println("    color: green;");
        out.println("}");

        out.println(".page-heading p {");
        out.println("    color: #737c76;");
        out.println("    font-size: 15px;");
        out.println("}");

        // TOP BAR
        out.println(".top-bar {");
        out.println("    display: flex;");
        out.println("    justify-content: space-between;");
        out.println("    align-items: center;");
        out.println("    margin-bottom: 25px;");
        out.println("}");

        out.println(".top-bar h2 {");
        out.println("    font-size: 23px;");
        out.println("    color: #29352d;");
        out.println("}");

        out.println(".top-bar p {");
        out.println("    color: #7a837d;");
        out.println("    font-size: 13px;");
        out.println("    margin-top: 5px;");
        out.println("}");

        // ADD PRODUCT BUTTON
        out.println(".add-product-btn {");
        out.println("    background: green;");
        out.println("    color: white;");
        out.println("    text-decoration: none;");
        out.println("    padding: 12px 18px;");
        out.println("    border-radius: 7px;");
        out.println("    font-size: 14px;");
        out.println("    font-weight: bold;");
        out.println("}");

        out.println(".add-product-btn:hover {");
        out.println("    background: #006f00;");
        out.println("}");

        // PRODUCT GRID
        out.println(".product-grid {");
        out.println("    display: grid;");
        out.println("    grid-template-columns: repeat(3, 1fr);");
        out.println("    gap: 26px;");
        out.println("}");

        // PRODUCT CARD
        out.println(".product-card {");
        out.println("    background: white;");
        out.println("    border: 1px solid #e1e8e2;");
        out.println("    border-radius: 14px;");
        out.println("    overflow: hidden;");
        out.println("    box-shadow: 0 5px 18px rgba(35,65,42,0.07);");
        out.println("}");

        out.println(".product-card:hover {");
        out.println("    transform: translateY(-4px);");
        out.println("    transition: 0.2s;");
        out.println("}");

        // IMAGE
        out.println(".product-image-container {");
        out.println("    height: 250px;");
        out.println("    background: #eef4ef;");
        out.println("    overflow: hidden;");
        out.println("}");

        out.println(".product-image {");
        out.println("    width: 100%;");
        out.println("    height: 100%;");
        out.println("    object-fit: cover;");
        out.println("}");

        // PRODUCT CONTENT
        out.println(".product-details {");
        out.println("    padding: 21px;");
        out.println("}");

        out.println(".product-details h2 {");
        out.println("    font-size: 21px;");
        out.println("    color: #27372c;");
        out.println("    margin-bottom: 7px;");
        out.println("}");

        // TYPE
        out.println(".product-type {");
        out.println("    display: inline-block;");
        out.println("    background: #e4f4e7;");
        out.println("    color: #287238;");
        out.println("    padding: 5px 11px;");
        out.println("    border-radius: 20px;");
        out.println("    font-size: 11px;");
        out.println("    font-weight: bold;");
        out.println("    margin-bottom: 17px;");
        out.println("}");

        // PRODUCT INFO
        out.println(".product-info {");
        out.println("    display: grid;");
        out.println("    grid-template-columns: repeat(3, 1fr);");
        out.println("    gap: 8px;");
        out.println("    margin-bottom: 17px;");
        out.println("}");

        out.println(".product-info div {");
        out.println("    background: #f5f8f5;");
        out.println("    border-radius: 8px;");
        out.println("    padding: 10px 8px;");
        out.println("}");

        out.println(".product-info span {");
        out.println("    display: block;");
        out.println("    color: #89948d;");
        out.println("    font-size: 10px;");
        out.println("    margin-bottom: 5px;");
        out.println("}");

        out.println(".product-info strong {");
        out.println("    color: #3d4e43;");
        out.println("    font-size: 12px;");
        out.println("}");

        // DESCRIPTION
        out.println(".description {");
        out.println("    color: #69766e;");
        out.println("    font-size: 13px;");
        out.println("    line-height: 1.5;");
        out.println("    min-height: 42px;");
        out.println("    padding-bottom: 15px;");
        out.println("    border-bottom: 1px solid #e5e8e5;");
        out.println("    margin-bottom: 15px;");
        out.println("}");

        // PRICE
        out.println(".price-row {");
        out.println("    display: flex;");
        out.println("    align-items: center;");
        out.println("    justify-content: space-between;");
        out.println("}");

        out.println(".price {");
        out.println("    color: #222222;");
        out.println("    font-size: 23px;");
        out.println("    font-weight: 800;");
        out.println("}");

        // BUTTONS
        out.println(".card-actions {");
        out.println("    display: flex;");
        out.println("    gap: 8px;");
        out.println("}");

        out.println(".edit-btn, .remove-btn {");
        out.println("    border: none;");
        out.println("    padding: 10px 15px;");
        out.println("    border-radius: 7px;");
        out.println("    cursor: pointer;");
        out.println("    font-size: 12px;");
        out.println("    font-weight: bold;");
        out.println("}");

        out.println(".edit-btn {");
        out.println("    background: #edf4ee;");
        out.println("    color: #2f7d3c;");
        out.println("}");

        out.println(".remove-btn {");
        out.println("    background: #fff0f0;");
        out.println("    color: #c24b4b;");
        out.println("}");

        // EMPTY
        out.println(".empty-state {");
        out.println("    background: white;");
        out.println("    border: 1px solid #e1e8e2;");
        out.println("    border-radius: 14px;");
        out.println("    padding: 80px 20px;");
        out.println("    text-align: center;");
        out.println("    grid-column: 1 / -1;");
        out.println("}");

        out.println(".empty-icon {");
        out.println("    font-size: 50px;");
        out.println("    margin-bottom: 15px;");
        out.println("}");

        out.println(".empty-state h2 {");
        out.println("    color: #34463a;");
        out.println("    margin-bottom: 10px;");
        out.println("}");

        out.println(".empty-state p {");
        out.println("    color: #7a867e;");
        out.println("    margin-bottom: 20px;");
        out.println("}");

        out.println(".empty-state a {");
        out.println("    display: inline-block;");
        out.println("    background: green;");
        out.println("    color: white;");
        out.println("    text-decoration: none;");
        out.println("    padding: 11px 18px;");
        out.println("    border-radius: 7px;");
        out.println("    font-weight: bold;");
        out.println("}");

        // ERROR
        out.println(".error-message {");
        out.println("    grid-column: 1 / -1;");
        out.println("    background: #fff0f0;");
        out.println("    border: 1px solid #f0caca;");
        out.println("    border-radius: 10px;");
        out.println("    padding: 30px;");
        out.println("    text-align: center;");
        out.println("    color: #a94444;");
        out.println("}");

        // FOOTER
        out.println("footer {");
        out.println("    background: #26382b;");
        out.println("    color: #dce5de;");
        out.println("    text-align: center;");
        out.println("    padding: 25px;");
        out.println("    font-size: 13px;");
        out.println("}");

        // RESPONSIVE
        out.println("@media(max-width:1000px) {");
        out.println("    .product-grid {");
        out.println("        grid-template-columns: repeat(2, 1fr);");
        out.println("    }");
        out.println("}");

        out.println("@media(max-width:700px) {");
        out.println("    .navbar {");
        out.println("        height: auto;");
        out.println("        padding: 18px 5%;");
        out.println("        flex-direction: column;");
        out.println("        gap: 15px;");
        out.println("    }");

        out.println("    .navbar nav {");
        out.println("        flex-wrap: wrap;");
        out.println("        justify-content: center;");
        out.println("    }");

        out.println("    .product-grid {");
        out.println("        grid-template-columns: 1fr;");
        out.println("    }");

        out.println("    .container {");
        out.println("        width: 92%;");
        out.println("    }");

        out.println("    .top-bar {");
        out.println("        flex-direction: column;");
        out.println("        align-items: flex-start;");
        out.println("        gap: 15px;");
        out.println("    }");
        out.println("}");

        out.println("</style>");
        out.println("</head>");

        // ==============================
        // BODY
        // ==============================

        out.println("<body>");

        // NAVBAR
        out.println("<header class='navbar'>");

        out.println("<div class='logo'>");
        out.println("<span class='eco'>Eco</span><span class='cycle'>Cycle</span>");
        out.println("</div>");

        out.println("<nav>");
        out.println("<a href='companyHome.html'>Home</a>");
        out.println("<a href='companyproducts' class='active'>My Products</a>");
        out.println("<a href='companySell.html'>Sell Product</a>");
        out.println("<a href='#'>Orders</a>");
        out.println("<a href='#'>Contact</a>");
        out.println("<a href='companySignin.html'>Logout</a>");
        out.println("</nav>");

        out.println("</header>");

        // MAIN
        out.println("<main class='container'>");

        // HEADING
        out.println("<div class='page-heading'>");
        out.println("<h1>My <span>Products</span></h1>");
        out.println("<p>Manage the recycled products you have listed on EcoCycle.</p>");
        out.println("</div>");

        // TOP BAR
        out.println("<div class='top-bar'>");

        out.println("<div>");
        out.println("<h2>Your Listed Products</h2>");
        out.println("<p>Products added by your company</p>");
        out.println("</div>");

        out.println("<a href='companySell.html' class='add-product-btn'>");
        out.println("+ Add Product");
        out.println("</a>");

        out.println(
    "<a href='companySellingStatus' " +
    "class='add-product-btn'>" +
    "Selling Status" +
    "</a>"
);
        out.println("</div>");

        // PRODUCT GRID
        out.println("<div class='product-grid'>");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            // ==============================
            // DATABASE CONNECTION
            // ==============================

            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "system",
                    "manager"
            );

            String sql =
                    "SELECT * FROM product_information " +
                    "WHERE COMPANY_ID = ? " +
                    "ORDER BY PRODUCT_ID DESC";

            ps = con.prepareStatement(sql);

            ps.setString(1, companyId);

            rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                String productId =
                        rs.getString("PRODUCT_ID");

                String productName =
                        rs.getString("PRODUCT_NAME");

                String productType =
                        rs.getString("PRODUCT_TYPE");

                String color =
                        rs.getString("COLOR");

                String quality =
                        rs.getString("QUALITY");

                String price =
                        rs.getString("PRICE");

                String quantity =
                        rs.getString("QUANTITY");

                String description =
                        rs.getString("DESCRIPTION");

                String image =
                        rs.getString("PRODUCT_IMAGE");

                // ==============================
                // PRODUCT CARD
                // ==============================

                out.println("<div class='product-card'>");

                // IMAGE
                out.println("<div class='product-image-container'>");

                if (image != null && !image.trim().isEmpty()) {

                    out.println("<img class='product-image' "
        + "src='uploads/" + image + "' "
        + "alt='Product Image'>");

                } else {

                    out.println("<div style='height:100%;"
                            + "display:flex;"
                            + "align-items:center;"
                            + "justify-content:center;"
                            + "font-size:50px;'>"
                            + "♻"
                            + "</div>");
                }

                out.println("</div>");

                // DETAILS
                out.println("<div class='product-details'>");

                out.println("<h2>"
                        + productName
                        + "</h2>");

                out.println("<div class='product-type'>"
                        + productType
                        + "</div>");

                // INFO
                out.println("<div class='product-info'>");

                out.println("<div>");
                out.println("<span>Color</span>");
                out.println("<strong>"
                        + color
                        + "</strong>");
                out.println("</div>");

                out.println("<div>");
                out.println("<span>Quality</span>");
                out.println("<strong>"
                        + quality
                        + "</strong>");
                out.println("</div>");

                out.println("<div>");
                out.println("<span>Available</span>");
                out.println("<strong>"
                        + quantity
                        + "</strong>");
                out.println("</div>");

                out.println("</div>");

                // DESCRIPTION
                out.println("<div class='description'>");
                out.println(description);
                out.println("</div>");

                // PRICE + BUTTONS
                out.println("<div class='price-row'>");

                out.println("<div class='price'>");
                out.println("₹" + price);
                out.println("</div>");

                out.println("<div class='card-actions'>");

                

                out.println("<form action='removeProduct' method='post' style='display:inline;'>");

out.println("<input type='hidden' name='productId' value='"
        + productId + "'>");

out.println("<button type='submit' class='remove-btn' "
        + "onclick=\"return confirm('Are you sure you want to remove this product?');\">");

out.println("Remove");

out.println("</button>");

out.println("</form>");

                out.println("</div>");

                out.println("</div>");

                out.println("</div>");

                out.println("</div>");
            }

            // NO PRODUCTS
            if (!found) {

                out.println("<div class='empty-state'>");

                out.println("<div class='empty-icon'>♻</div>");

                out.println("<h2>No Products Yet</h2>");

                out.println("<p>");
                out.println("You haven't added any products for sale yet.");
                out.println("</p>");

                out.println("<a href='companySell.html'>");
                out.println("Add Your First Product");
                out.println("</a>");

                out.println("</div>");
            }

        } catch (Exception e) {

            out.println("<div class='error-message'>");

            out.println("<h3>Unable to load products</h3>");

            out.println("<p>"
                    + e.getMessage()
                    + "</p>");

            out.println("</div>");

            e.printStackTrace();

        } finally {

            try {
                if (rs != null)
                    rs.close();

                if (ps != null)
                    ps.close();

                if (con != null)
                    con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        out.println("</div>");

        out.println("</main>");

        // FOOTER
        out.println("<footer>");
        out.println("© 2026 EcoWaste Management System "
                + "<span>|</span> Building a Cleaner Future ♻");
        out.println("</footer>");

        out.println("</body>");
        out.println("</html>");
    }
}