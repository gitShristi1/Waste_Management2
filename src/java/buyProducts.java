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

@WebServlet("/buyProduct")
public class buyProducts extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ORACLE DATABASE DETAILS
    private static final String DB_URL =
            "jdbc:oracle:thin:@localhost:1521:XE";

    private static final String DB_USER = "system";

    private static final String DB_PASSWORD = "manager";


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        showProductDetails(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("payment".equals(action)) {

            processPayment(request, response);

        } else {

            showProductDetails(request, response);
        }
    }


    private void showProductDetails(HttpServletRequest request,
                                    HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String productIdString = request.getParameter("productId");

        if (productIdString == null || productIdString.trim().isEmpty()) {

            out.println("<h2>Product not found.</h2>");
            out.println("<a href='userProducts'>Back to Products</a>");

            return;
        }

        int productId;

        try {

            productId = Integer.parseInt(productIdString);

        } catch (NumberFormatException e) {

            out.println("<h2>Invalid Product ID.</h2>");
            out.println("<a href='userProducts'>Back to Products</a>");

            return;
        }


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;


        try {

            // ORACLE DRIVER
            Class.forName("oracle.jdbc.driver.OracleDriver");


            // ORACLE CONNECTION
            connection = DriverManager.getConnection(
                    DB_URL,
                    DB_USER,
                    DB_PASSWORD
            );


            String sql =
        "SELECT p.*, c.COMPANY_NAME AS company_name " +
        "FROM product_information p " +
        "JOIN company c " +
        "ON p.COMPANY_ID = c.COMPANY_ID " +
        "WHERE p.PRODUCT_ID = ?";


            statement = connection.prepareStatement(sql);

            statement.setInt(1, productId);

            result = statement.executeQuery();


            if (!result.next()) {

                out.println("<h2>Product not found.</h2>");

                out.println(
                        "<a href='userProducts'>Back to Products</a>"
                );

                return;
            }


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

            String companyName =
                    result.getString("company_name");


            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println(
                    "<meta name='viewport' " +
                    "content='width=device-width, initial-scale=1.0'>"
            );

            out.println("<title>EcoCycle - Buy Product</title>");


            out.println("<style>");

            out.println(
                    "*{margin:0;padding:0;box-sizing:border-box;}"
            );

            out.println(
                    "body{" +
                    "font-family:Arial,sans-serif;" +
                    "background:#f4f8f5;" +
                    "color:#183c2b;" +
                    "min-height:100vh;" +
                    "}"
            );

            out.println("a{text-decoration:none;}");

            out.println(
                    ".header{" +
                    "display:flex;" +
                    "justify-content:space-between;" +
                    "align-items:center;" +
                    "padding:18px 7%;" +
                    "background:white;" +
                    "box-shadow:0 2px 10px rgba(0,0,0,0.08);" +
                    "}"
            );

            out.println(
                    ".logo{" +
                    "font-size:26px;" +
                    "font-weight:800;" +
                    "color:#183c2b;" +
                    "}"
            );

            out.println(
                    ".logo span{color:green;}"
            );

            out.println(
                    ".navbar{" +
                    "display:flex;" +
                    "gap:25px;" +
                    "}"
            );

            out.println(
                    ".navbar a{" +
                    "color:#355542;" +
                    "font-weight:600;" +
                    "}"
            );

            out.println(
                    ".navbar a:hover{color:#2f9659;}"
            );

            out.println(
                    ".main{" +
                    "padding:50px 7%;" +
                    "}"
            );

            out.println(
                    ".product-card{" +
                    "max-width:900px;" +
                    "margin:auto;" +
                    "background:white;" +
                    "border-radius:18px;" +
                    "padding:35px;" +
                    "box-shadow:0 10px 30px rgba(45,86,57,0.12);" +
                    "}"
            );

            out.println(
                    ".product-image{" +
                    "display:block;" +
                    "width:100%;" +
                    "max-width:450px;" +
                    "height:320px;" +
                    "object-fit:cover;" +
                    "border-radius:14px;" +
                    "margin:0 auto 30px auto;" +
                    "}"
            );

            out.println(".details{text-align:center;}");

            out.println(
                    ".details h1{" +
                    "font-size:34px;" +
                    "color:#355542;" +
                    "margin-bottom:10px;" +
                    "}"
            );

            out.println(
                    ".product-type{" +
                    "color:#2f9659;" +
                    "font-size:18px;" +
                    "font-weight:bold;" +
                    "margin-bottom:20px;" +
                    "}"
            );

            out.println(
                    ".company{" +
                    "font-size:16px;" +
                    "color:#666;" +
                    "margin-bottom:25px;" +
                    "}"
            );

            out.println(
                    ".info{" +
                    "display:grid;" +
                    "grid-template-columns:repeat(3,1fr);" +
                    "gap:15px;" +
                    "margin:25px 0;" +
                    "}"
            );

            out.println(
                    ".info-item{" +
                    "background:#f5f9f4;" +
                    "padding:18px;" +
                    "border-radius:10px;" +
                    "}"
            );

            out.println(
                    ".info-item p{" +
                    "color:#777;" +
                    "margin-bottom:6px;" +
                    "}"
            );

            out.println(
                    ".info-item strong{" +
                    "color:#183c2b;" +
                    "font-size:17px;" +
                    "}"
            );

            out.println(
                    ".description{" +
                    "text-align:left;" +
                    "background:#f8fbf8;" +
                    "padding:20px;" +
                    "border-radius:10px;" +
                    "margin:25px 0;" +
                    "}"
            );

            out.println(
                    ".description h3{" +
                    "margin-bottom:8px;" +
                    "color:#355542;" +
                    "}"
            );

            out.println(
                    ".description p{" +
                    "color:#666;" +
                    "line-height:1.6;" +
                    "}"
            );

            out.println(
                    ".payment-box{" +
                    "text-align:center;" +
                    "border-top:1px solid #ddd;" +
                    "padding-top:25px;" +
                    "margin-top:25px;" +
                    "}"
            );

            out.println(
                    ".price-label{" +
                    "font-size:16px;" +
                    "color:#777;" +
                    "margin-bottom:8px;" +
                    "}"
            );

            out.println(
                    ".price{" +
                    "font-size:35px;" +
                    "font-weight:bold;" +
                    "color:#2f9659;" +
                    "margin-bottom:20px;" +
                    "}"
            );

            out.println(
                    ".payment-btn{" +
                    "border:none;" +
                    "background:#2f9659;" +
                    "color:white;" +
                    "padding:14px 35px;" +
                    "border-radius:8px;" +
                    "font-size:17px;" +
                    "font-weight:bold;" +
                    "cursor:pointer;" +
                    "}"
            );

            out.println(
                    ".payment-btn:hover{background:#237846;}"
            );

            out.println(
                    ".back-btn{" +
                    "display:inline-block;" +
                    "margin-top:20px;" +
                    "padding:12px 25px;" +
                    "background:#e7efe9;" +
                    "color:#355542;" +
                    "border-radius:8px;" +
                    "font-weight:bold;" +
                    "}"
            );

            out.println(
                    ".out-stock{" +
                    "background:#ffeaea;" +
                    "color:#b42318;" +
                    "padding:15px;" +
                    "border-radius:8px;" +
                    "font-weight:bold;" +
                    "margin-bottom:20px;" +
                    "}"
            );

            out.println(
                    ".footer{" +
                    "text-align:center;" +
                    "padding:25px;" +
                    "margin-top:30px;" +
                    "color:#666;" +
                    "}"
            );

            out.println(
                    "@media(max-width:700px){" +
                    ".header{flex-direction:column;gap:15px;}" +
                    ".navbar{flex-wrap:wrap;justify-content:center;}" +
                    ".info{grid-template-columns:1fr;}" +
                    ".main{padding:25px 4%;}" +
                    ".product-card{padding:20px;}" +
                    "}"
            );

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");


            out.println("<header class='header'>");

            out.println(
                    "<div class='logo'>Eco<span>Cycle</span></div>"
            );

            out.println("<nav class='navbar'>");

            out.println("<a href='HomeUser.html'>Home</a>");

            out.println("<a href='usell.html'>Sell Waste</a>");

            out.println(
                    "<a href='userProducts'>Buy Products</a>"
            );

            out.println(
                    "<a href='requeststatus.html'>My Activities</a>"
            );

            out.println("<a href='#'>About Us</a>");

            out.println("</nav>");

            out.println("</header>");


            out.println("<main class='main'>");

            out.println("<div class='product-card'>");


            if (imageName != null &&
                    !imageName.trim().isEmpty()) {

                out.println(
        "<img src='" +
        request.getContextPath() +
        "/uploads/" +
        imageName +
        "' class='product-image' " +
        "alt='" + productName + "'>"
);
            }


            out.println("<div class='details'>");

            out.println(
                    "<h1>" + productName + "</h1>"
            );

            out.println(
                    "<div class='product-type'>" +
                    productType +
                    "</div>"
            );

            out.println(
                    "<div class='company'>" +
                    "Sold by: <strong>" +
                    companyName +
                    "</strong></div>"
            );


            out.println("<div class='info'>");

            out.println(
                    "<div class='info-item'>" +
                    "<p>Color</p>" +
                    "<strong>" + color + "</strong>" +
                    "</div>"
            );

            out.println(
                    "<div class='info-item'>" +
                    "<p>Quality</p>" +
                    "<strong>" + quality + "</strong>" +
                    "</div>"
            );

            out.println(
                    "<div class='info-item'>" +
                    "<p>Available</p>" +
                    "<strong>" + quantity + "</strong>" +
                    "</div>"
            );

            out.println("</div>");


            out.println("<div class='description'>");

            out.println("<h3>Description</h3>");

            out.println(
                    "<p>" + description + "</p>"
            );

            out.println("</div>");


            out.println("<div class='payment-box'>");


            if (quantity > 0) {

                out.println(
                        "<div class='price-label'>Total Amount</div>"
                );

                out.println(
                        "<div class='price'>₹" +
                        String.format("%.2f", price) +
                        "</div>"
                );


                out.println(
                        "<form action='buyProducts' method='post'>"
                );

                out.println(
                        "<input type='hidden' name='action' value='payment'>"
                );

                out.println(
                        "<input type='hidden' name='productId' value='" +
                        productId +
                        "'>"
                );

                out.println(
                        "<button type='submit' class='payment-btn'>" +
                        "Confirm Payment" +
                        "</button>"
                );

                out.println("</form>");

            } else {

                out.println(
                        "<div class='out-stock'>OUT OF STOCK</div>"
                );
            }


            out.println(
                    "<a href='userProducts' class='back-btn'>" +
                    "Back to Products</a>"
            );

            out.println("</div>");

            out.println("</div>");

            out.println("</div>");

            out.println("</main>");


            out.println(
                    "<footer class='footer'>" +
                    "© 2026 EcoCycle. All rights reserved." +
                    "</footer>"
            );

            out.println("</body>");

            out.println("</html>");


        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Error loading product</h2>");

            out.println(
                    "<p>" + e.getMessage() + "</p>"
            );

            out.println("<br>");

            out.println(
                    "<a href='userProducts'>Back to Products</a>"
            );


        } finally {

            try {

                if (result != null)
                    result.close();

                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    private void processPayment(HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();


        String productIdString =
                request.getParameter("productId");


        if (productIdString == null ||
                productIdString.trim().isEmpty()) {

            out.println("<h2>Product ID is missing.</h2>");

            out.println(
                    "<a href='userProducts'>Back to Products</a>"
            );

            return;
        }


        int productId;

        try {

            productId =
                    Integer.parseInt(productIdString);

        } catch (NumberFormatException e) {

            out.println("<h2>Invalid Product ID.</h2>");

            out.println(
                    "<a href='userProducts'>Back to Products</a>"
            );

            return;
        }


        HttpSession session =
                request.getSession(false);


        if (session == null) {

            response.sendRedirect(
                    "User_signin.html"
            );

            return;
        }


        String buyerEmail =
                (String) session.getAttribute("email");


        if (buyerEmail == null ||
                buyerEmail.trim().isEmpty()) {

            response.sendRedirect(
                    "User_signin.html"
            );

            return;
        }


        Connection connection = null;

        PreparedStatement productStatement = null;
        PreparedStatement updateStatement = null;
        PreparedStatement transactionStatement = null;

        ResultSet productResult = null;


        try {

            // ORACLE DRIVER
            Class.forName(
                    "oracle.jdbc.driver.OracleDriver"
            );


            // ORACLE CONNECTION
            connection =
                    DriverManager.getConnection(
                            DB_URL,
                            DB_USER,
                            DB_PASSWORD
                    );


            connection.setAutoCommit(false);


            String productSQL =
                    "SELECT product_name, price, quantity, " +
                    "COMPANY_ID " +
                    "FROM product_information " +
                    "WHERE product_id = ? " +
                    "FOR UPDATE";


            productStatement =
                    connection.prepareStatement(
                            productSQL
                    );

            productStatement.setInt(
                    1,
                    productId
            );


            productResult =
                    productStatement.executeQuery();


            if (!productResult.next()) {

                connection.rollback();

                out.println(
                        "<h2>Product not found.</h2>"
                );

                out.println(
                        "<a href='userProducts'>" +
                        "Back to Products</a>"
                );

                return;
            }


            String productName =
                    productResult.getString(
                            "product_name"
                    );

            double price =
                    productResult.getDouble(
                            "price"
                    );

            int availableQuantity =
                    productResult.getInt(
                            "quantity"
                    );

            String companyid =
                    productResult.getString(
                            "COMPANY_ID"
                    );


            int purchaseQuantity = 1;


            if (availableQuantity < purchaseQuantity) {

                connection.rollback();

                out.println(
                        "<h2>Sorry, this product is out of stock.</h2>"
                );

                out.println(
                        "<a href='userProducts'>" +
                        "Back to Products</a>"
                );

                return;
            }


            double totalAmount =
                    price * purchaseQuantity;


            double commission =
                    totalAmount * 0.05;


            double companyAmount =
                    totalAmount - commission;


            String updateSQL =
                    "UPDATE product_information " +
                    "SET quantity = quantity - ? " +
                    "WHERE product_id = ? " +
                    "AND quantity >= ?";


            updateStatement =
                    connection.prepareStatement(
                            updateSQL
                    );


            updateStatement.setInt(
                    1,
                    purchaseQuantity
            );

            updateStatement.setInt(
                    2,
                    productId
            );

            updateStatement.setInt(
                    3,
                    purchaseQuantity
            );


            int updatedRows =
                    updateStatement.executeUpdate();


            if (updatedRows != 1) {

                connection.rollback();

                out.println(
                        "<h2>Product is no longer available.</h2>"
                );

                out.println(
                        "<a href='userProducts'>" +
                        "Back to Products</a>"
                );

                return;
            }


            // ORACLE:
            // transaction_id is generated using sequence

            String transactionSQL =
                    "INSERT INTO transactions " +
                    "(transaction_id, product_id, COMPANY_ID, " +
                    "buyer_email, product_name, quantity, " +
                    "total_amount, commission, company_amount, status) " +
                    "VALUES " +
                    "(transaction_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


            transactionStatement =
        connection.prepareStatement(
                transactionSQL,
                new String[]{"TRANSACTION_ID"}
        );


            transactionStatement.setInt(
                    1,
                    productId
            );

            transactionStatement.setString(
                    2,
                    companyid
            );

            transactionStatement.setString(
                    3,
                    buyerEmail
            );

            transactionStatement.setString(
                    4,
                    productName
            );

            transactionStatement.setInt(
                    5,
                    purchaseQuantity
            );

            transactionStatement.setDouble(
                    6,
                    totalAmount
            );

            transactionStatement.setDouble(
                    7,
                    commission
            );

            transactionStatement.setDouble(
                    8,
                    companyAmount
            );

            transactionStatement.setString(
                    9,
                    "SUCCESS"
            );


            int transactionRows =
        transactionStatement.executeUpdate();


if (transactionRows != 1) {

    connection.rollback();

    out.println(
            "<h2>Transaction could not be completed.</h2>"
    );

    out.println(
            "<a href='userProducts'>" +
            "Back to Products</a>"
    );

    return;
}


// GET GENERATED TRANSACTION ID
int transactionId = 0;

ResultSet generatedKeys =
        transactionStatement.getGeneratedKeys();

if (generatedKeys.next()) {

    transactionId =
            generatedKeys.getInt(1);
}

generatedKeys.close();


            connection.commit();


// STORE PRODUCT ID AND TRANSACTION ID IN SESSION
session.setAttribute(
        "purchasedProductId",
        productId
);

session.setAttribute(
        "purchasedTransactionId",
        transactionId
);


showPaymentSuccess(
        request,
        response
);


        } catch (Exception e) {

            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception rollbackError) {

                rollbackError.printStackTrace();
            }


            e.printStackTrace();


            out.println(
                    "<h2>Payment failed.</h2>"
            );

            out.println(
                    "<p>" +
                    e.getMessage() +
                    "</p>"
            );

            out.println(
                    "<a href='userProducts'>" +
                    "Back to Products</a>"
            );


        } finally {

            try {

                if (productResult != null)
                    productResult.close();

                if (productStatement != null)
                    productStatement.close();

                if (updateStatement != null)
                    updateStatement.close();

                if (transactionStatement != null)
                    transactionStatement.close();

                if (connection != null)
                    connection.close();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    private void showPaymentSuccess(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        PrintWriter out =
                response.getWriter();


        int productId = 0;


        try {

            String productIdString =
                    request.getParameter("productId");

            if (productIdString != null &&
                    !productIdString.trim().isEmpty()) {

                productId =
                        Integer.parseInt(
                                productIdString
                        );
            }

        } catch (NumberFormatException e) {

            productId = 0;
        }


        out.println("<!DOCTYPE html>");

        out.println("<html>");

        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println(
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>"
        );

        out.println(
                "<title>Payment Successful</title>"
        );


        out.println("<style>");

        out.println(
                "*{" +
                "margin:0;" +
                "padding:0;" +
                "box-sizing:border-box;" +
                "}"
        );

        out.println(
                "body{" +
                "font-family:Arial,sans-serif;" +
                "background:#f4f8f5;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "min-height:100vh;" +
                "}"
        );

        out.println(
                ".success-box{" +
                "background:white;" +
                "width:90%;" +
                "max-width:550px;" +
                "padding:50px;" +
                "border-radius:18px;" +
                "text-align:center;" +
                "box-shadow:0 10px 30px rgba(0,0,0,0.08);" +
                "}"
        );

        out.println(
                ".check{" +
                "width:80px;" +
                "height:80px;" +
                "border-radius:50%;" +
                "background:#e4f3e8;" +
                "color:green;" +
                "font-size:45px;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "margin:0 auto 25px auto;" +
                "}"
        );

        out.println(
                "h1{" +
                "margin-bottom:15px;" +
                "color:#183c2b;" +
                "}"
        );

        out.println(
                "p{" +
                "color:#666;" +
                "line-height:1.6;" +
                "margin-bottom:15px;" +
                "}"
        );

        out.println(
                ".order-id{" +
                "background:#f4f8f5;" +
                "padding:15px;" +
                "border-radius:8px;" +
                "margin:25px 0;" +
                "}"
        );

        out.println(
                ".feedback-section{" +
                "margin:25px 0;" +
                "padding:20px;" +
                "background:#f8fbf8;" +
                "border-radius:10px;" +
                "}"
        );

        out.println(
                ".feedback-btn{" +
                "display:inline-block;" +
                "background:#2f9659;" +
                "color:white;" +
                "text-decoration:none;" +
                "padding:12px 22px;" +
                "border-radius:7px;" +
                "font-weight:bold;" +
                "margin-top:10px;" +
                "}"
        );

        out.println(
                ".home-btn{" +
                "display:inline-block;" +
                "background:#2f9659;" +
                "color:white;" +
                "text-decoration:none;" +
                "padding:13px 25px;" +
                "border-radius:7px;" +
                "font-weight:bold;" +
                "margin-top:10px;" +
                "}"
        );

        out.println(
                ".thanks{" +
                "margin-top:20px;" +
                "}"
        );

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        out.println(
                "<div class='success-box'>"
        );

        out.println(
                "<div class='check'>✓</div>"
        );

        out.println(
                "<h1>Payment Successful!</h1>"
        );

        out.println(
                "<p>" +
                "Your product purchase has been confirmed successfully." +
                "</p>"
        );


        if (productId > 0) {

            out.println(
                    "<div class='order-id'>" +
                    "<strong>Product ID: </strong>" +
                    productId +
                    "</div>"
            );
        }


        out.println(
                "<div class='feedback-section'>"
        );

        out.println(
                "<h2>Your feedback means a lot to us ❤️</h2>"
        );

        out.println(
                "<p>" +
                "We would love to know about your experience. " +
                "Your feedback helps us improve EcoCycle." +
                "</p>"
        );

        out.println(
                "<a class='feedback-btn' " +
                "href='userBuyFeedback.html'>" +
                "Give Feedback" +
                "</a>"
        );

        out.println("</div>");


        out.println(
                "<a class='home-btn' " +
                "href='userProducts'>" +
                "Continue Shopping" +
                "</a>"
        );


        out.println(
                "<p class='thanks'>" +
                "Thank you for choosing EcoCycle 🌱" +
                "</p>"
        );


        out.println("</div>");

        out.println("</body>");

        out.println("</html>");
    }
}