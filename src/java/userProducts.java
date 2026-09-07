import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet("/userProducts")
public class userProducts extends HttpServlet {

    private static final long serialVersionUID = 1L;


    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        response.setContentType("text/html");

        PrintWriter out = response.getWriter();


        try {


            // =====================================
            // LOAD ORACLE DRIVER
            // =====================================

            Class.forName(
                    "oracle.jdbc.driver.OracleDriver"
            );


            // =====================================
            // CONNECT TO ORACLE DATABASE
            // =====================================

            Connection connection =
                    DriverManager.getConnection(
                            "jdbc:oracle:thin:@localhost:1521:xe",
                            "system",
                            "manager"
                    );


            // =====================================
            // SQL QUERY
            // =====================================

            String sql =
                    "SELECT * FROM product_information";


            PreparedStatement statement =
                    connection.prepareStatement(sql);


            ResultSet result =
                    statement.executeQuery();


            // =====================================
            // HTML START
            // =====================================

            out.println("<!DOCTYPE html>");

            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println(
                "<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>"
            );


            out.println(
                "<title>EcoCycle - Buy Products</title>"
            );


            // =====================================
            // CSS
            // =====================================

            out.println("<style>");


            out.println(

                "* {"
                + "margin: 0;"
                + "padding: 0;"
                + "box-sizing: border-box;"
                + "}"

            );


            out.println(

                "body {"
                + "font-family: Arial, sans-serif;"
                + "background: #f4f8f5;"
                + "color: #222;"
                + "}"

            );


            // =====================================
            // HEADER
            // =====================================

            out.println(

                ".navbar {"
                + "height: 72px;"
                + "background: white;"
                + "display: flex;"
                + "align-items: center;"
                + "justify-content: space-between;"
                + "padding: 0 7%;"
                + "border-bottom: 1px solid #dfe8e1;"
                + "}"

            );


            out.println(

                ".logo {"
                + "font-size: 28px;"
                + "font-weight: bold;"
                + "color: black;"
                + "}"

            );


            out.println(

                ".logo span {"
                + "color: green;"
                + "}"

            );


            out.println(

                ".nav-links {"
                + "display: flex;"
                + "gap: 28px;"
                + "}"

            );


            out.println(

                ".nav-links a {"
                + "text-decoration: none;"
                + "color: #444;"
                + "font-size: 14px;"
                + "font-weight: 600;"
                + "}"

            );


            out.println(

                ".nav-links a:hover {"
                + "color: green;"
                + "}"

            );


            // =====================================
            // MAIN
            // =====================================

            out.println(

                ".container {"
                + "width: 90%;"
                + "max-width: 1200px;"
                + "margin: auto;"
                + "padding: 45px 0;"
                + "}"

            );


            // =====================================
            // HEADING
            // =====================================

            out.println(

                ".heading {"
                + "text-align: center;"
                + "margin-bottom: 40px;"
                + "}"

            );


            out.println(

                ".heading h1 {"
                + "font-size: 36px;"
                + "margin-bottom: 10px;"
                + "}"

            );


            out.println(

                ".heading h1 span {"
                + "color: green;"
                + "}"

            );


            out.println(

                ".heading p {"
                + "color: #6d776f;"
                + "font-size: 15px;"
                + "}"

            );


            // =====================================
            // PRODUCT GRID
            // =====================================

            out.println(

                ".products {"
                + "display: grid;"
                + "grid-template-columns: repeat(3, 1fr);"
                + "gap: 25px;"
                + "}"

            );


            // =====================================
            // PRODUCT CARD
            // =====================================

            out.println(

                ".product-card {"
                + "background: white;"
                + "border-radius: 14px;"
                + "overflow: hidden;"
                + "border: 1px solid #e0e8e2;"
                + "box-shadow: 0 6px 18px rgba(0,0,0,0.06);"
                + "transition: 0.3s;"
                + "}"

            );


            out.println(

                ".product-card:hover {"
                + "transform: translateY(-5px);"
                + "box-shadow: 0 12px 25px rgba(0,0,0,0.10);"
                + "}"

            );


            // =====================================
            // IMAGE
            // =====================================

            out.println(

                ".product-image {"
                + "width: 100%;"
                + "height: 240px;"
                + "object-fit: cover;"
                + "background: #f1f4f2;"
                + "}"

            );


            // =====================================
            // DETAILS
            // =====================================

            out.println(

                ".details {"
                + "padding: 20px;"
                + "}"

            );


            out.println(

                ".details h2 {"
                + "font-size: 20px;"
                + "margin-bottom: 8px;"
                + "}"

            );


            out.println(

                ".type {"
                + "display: inline-block;"
                + "background: #e4f3e8;"
                + "color: #22763b;"
                + "padding: 5px 10px;"
                + "border-radius: 20px;"
                + "font-size: 11px;"
                + "font-weight: bold;"
                + "margin-bottom: 15px;"
                + "}"

            );


            // =====================================
            // PRODUCT INFO
            // =====================================

            out.println(

                ".info {"
                + "display: grid;"
                + "grid-template-columns: 1fr 1fr;"
                + "gap: 10px;"
                + "margin-bottom: 15px;"
                + "}"

            );


            out.println(

                ".info p {"
                + "font-size: 12px;"
                + "color: #777;"
                + "}"

            );


            out.println(

                ".info strong {"
                + "display: block;"
                + "color: #333;"
                + "font-size: 14px;"
                + "margin-top: 3px;"
                + "}"

            );


            // =====================================
            // DESCRIPTION
            // =====================================

            out.println(

                ".description {"
                + "font-size: 13px;"
                + "line-height: 1.5;"
                + "color: #777;"
                + "border-top: 1px solid #eee;"
                + "padding-top: 12px;"
                + "margin-bottom: 18px;"
                + "}"

            );


            // =====================================
            // BOTTOM
            // =====================================

            out.println(

                ".bottom {"
                + "display: flex;"
                + "justify-content: space-between;"
                + "align-items: center;"
                + "}"

            );


            // =====================================
            // PRICE
            // =====================================

            out.println(

                ".price {"
                + "font-size: 22px;"
                + "font-weight: bold;"
                + "color: #222;"
                + "}"

            );


            // =====================================
            // BUY BUTTON
            // =====================================

            out.println(

                ".buy-btn {"
                + "background: green;"
                + "color: white;"
                + "border: none;"
                + "padding: 10px 18px;"
                + "border-radius: 6px;"
                + "font-weight: bold;"
                + "cursor: pointer;"
                + "}"

            );


            out.println(

                ".buy-btn:hover {"
                + "background: #006b00;"
                + "}"

            );


            // =====================================
            // OUT OF STOCK
            // =====================================

            out.println(

                ".out-of-stock {"
                + "background: #ffebee;"
                + "color: #c62828;"
                + "border: 1px solid #ef9a9a;"
                + "padding: 10px 18px;"
                + "border-radius: 6px;"
                + "font-weight: bold;"
                + "text-align: center;"
                + "}"

            );


            // =====================================
            // FOOTER
            // =====================================

            out.println(

                ".footer {"
                + "text-align: center;"
                + "padding: 20px;"
                + "background: white;"
                + "border-top: 1px solid #ddd;"
                + "color: #777;"
                + "margin-top: 40px;"
                + "}"

            );


            // =====================================
            // RESPONSIVE
            // =====================================

            out.println(

                "@media(max-width: 900px) {"
                + ".products {"
                + "grid-template-columns: repeat(2, 1fr);"
                + "}"
                + "}"

            );


            out.println(

                "@media(max-width: 600px) {"
                + ".products {"
                + "grid-template-columns: 1fr;"
                + "}"
                + ".navbar {"
                + "flex-direction: column;"
                + "height: auto;"
                + "padding: 15px;"
                + "gap: 15px;"
                + "}"
                + "}"

            );


            out.println("</style>");

            out.println("</head>");


            // =====================================
            // BODY
            // =====================================

            out.println("<body>");


            // =====================================
            // HEADER
            // =====================================

            out.println(
                "<header class='navbar'>"
            );


            out.println(
                "<div class='logo'>"
                + "Eco<span>Cycle</span>"
                + "</div>"
            );


            out.println(
                "<nav class='nav-links'>"
                + "<a href='HomeUser.html'>Home</a>"
                + "<a href='usell.html'>Sell Waste</a>"
                + "<a href='userProducts'>Buy Products</a>"
                + "<a href='#'>My Activities</a>"
                + "<a href='#'>About Us</a>"
                + "</nav>"
            );


            out.println("</header>");


            // =====================================
            // MAIN
            // =====================================

            out.println(
                "<main class='container'>"
            );


            // =====================================
            // HEADING
            // =====================================

            out.println(
                "<div class='heading'>"
                + "<h1>Buy <span>Recycled Products</span></h1>"
                + "<p>"
                + "Give recycled products a new home."
                + "</p>"
                + "</div>"
            );


            // =====================================
            // PRODUCTS
            // =====================================

            out.println(
                "<div class='products'>"
            );


            boolean found = false;


            while (result.next()) {

                found = true;


                // =====================================
                // GET PRODUCT DATA
                // =====================================

                int productId =
                        result.getInt("product_id");


                String productName =
                        result.getString("product_name");


                String productType =
                        result.getString("product_type");


                String color =
                        result.getString("color");


                String quality =
                        result.getString("quality");


                double price =
                        result.getDouble("price");


                int quantity =
                        result.getInt("quantity");


                String description =
                        result.getString("description");


                String imageName =
                        result.getString("product_image");


                // =====================================
                // PRODUCT CARD
                // =====================================

                out.println(
                    "<div class='product-card'>"
                );


                // =====================================
                // IMAGE
                // =====================================

                out.println(
                    "<img "
                    + "src='productImages/"
                    + imageName
                    + "' "
                    + "class='product-image' "
                    + "alt='"
                    + productName
                    + "'>"
                );


                // =====================================
                // DETAILS
                // =====================================

                out.println(
                    "<div class='details'>"
                );


                out.println(
                    "<h2>"
                    + productName
                    + "</h2>"
                );


                out.println(
                    "<span class='type'>"
                    + productType
                    + "</span>"
                );


                // =====================================
                // INFO
                // =====================================

                out.println(
                    "<div class='info'>"
                );


                out.println(
                    "<p>Color"
                    + "<strong>"
                    + color
                    + "</strong></p>"
                );


                out.println(
                    "<p>Quality"
                    + "<strong>"
                    + quality
                    + "</strong></p>"
                );


                out.println(
                    "<p>Available"
                    + "<strong>"
                    + quantity
                    + "</strong></p>"
                );


                out.println("</div>");


                // =====================================
                // DESCRIPTION
                // =====================================

                out.println(
                    "<div class='description'>"
                    + description
                    + "</div>"
                );


                // =====================================
                // PRICE + BUY
                // =====================================

                out.println(
                    "<div class='bottom'>"
                );


                out.println(
                    "<div class='price'>"
                    + "₹"
                    + price
                    + "</div>"
                );


                // =====================================
                // CHECK STOCK
                // =====================================

                if (quantity > 0) {

                    out.println(
                        "<form action='buyProduct' "
                        + "method='post'>"
                    );


                    out.println(
                        "<input type='hidden' "
                        + "name='productId' "
                        + "value='"
                        + productId
                        + "'>"
                    );


                    out.println(
                        "<button "
                        + "type='submit' "
                        + "class='buy-btn'>"
                        + "Buy Now"
                        + "</button>"
                    );


                    out.println("</form>");


                } else {

                    out.println(
                        "<div class='out-of-stock'>"
                        + "OUT OF STOCK"
                        + "</div>"
                    );
                }


                out.println("</div>");

                out.println("</div>");

                out.println("</div>");
            }


            // =====================================
            // NO PRODUCTS
            // =====================================

            if (!found) {

                out.println(
                    "<div style='text-align:center; "
                    + "grid-column:1/-1; "
                    + "padding:50px;'>"
                    + "<h2>No Products Available</h2>"
                    + "<p>"
                    + "No recycled products have been added yet."
                    + "</p>"
                    + "</div>"
                );
            }


            out.println("</div>");

            out.println("</main>");


            // =====================================
            // FOOTER
            // =====================================

            out.println(
                "<footer class='footer'>"
                + "© 2026 EcoCycle. All rights reserved."
                + "</footer>"
            );


            out.println("</body>");

            out.println("</html>");


            // =====================================
            // CLOSE CONNECTION
            // =====================================

            result.close();

            statement.close();

            connection.close();


        } catch (Exception e) {

            e.printStackTrace();


            out.println(
                "<h2>Error loading products</h2>"
            );


            out.println(
                "<p>"
                + e.getMessage()
                + "</p>"
            );
        }
    }
}