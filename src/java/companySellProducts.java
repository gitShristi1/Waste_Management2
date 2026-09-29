import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/companySellProducts")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class companySellProducts extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // ==========================================
        // GET DATA FROM HTML FORM
        // ==========================================

        String productName =
                request.getParameter("productName");

        String productType =
                request.getParameter("productType");

        String color =
                request.getParameter("color");

        String quality =
                request.getParameter("quality");

        String price =
                request.getParameter("price");

        String quantity =
                request.getParameter("quantity");

        String description =
                request.getParameter("description");


        // ==========================================
        // GET LOGGED-IN COMPANY FROM SESSION
        // ==========================================

        HttpSession session = request.getSession(false);

        if (session == null) {

            out.println("<h2>Session not found!</h2>");
            out.println("<p>Please login again.</p>");

            return;
        }

        String companyid =
                (String) session.getAttribute("companyId");

        System.out.println(
                "COMPANY Id = " + companyid
        );


        // ==========================================
        // CHECK COMPANY SESSION
        // ==========================================

        if (companyid == null ||
                companyid.trim().isEmpty()) {

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Session Error</title>");
            out.println("</head>");

            out.println("<body>");

            out.println("<h2>Company session not found!</h2>");

            out.println(
                    "<p>Please login again before adding a product.</p>"
            );

            out.println(
                    "<a href='companySignin.html'>Go to Login</a>"
            );

            out.println("</body>");
            out.println("</html>");

            return;
        }


        // ==========================================
        // GET PRODUCT IMAGE
        // ==========================================

        Part imagePart =
                request.getPart("productImage");

        if (imagePart == null ||
                imagePart.getSize() == 0) {

            out.println("<h2>Please select a product image.</h2>");

            return;
        }

        String imageName =
                imagePart.getSubmittedFileName();


        // ==========================================
        // CREATE IMAGE FOLDER
        // ==========================================

        String uploadPath =
                getServletContext().getRealPath("")
                + File.separator
                + "uploads";

        File uploadDirectory =
                new File(uploadPath);

        if (!uploadDirectory.exists()) {

            uploadDirectory.mkdirs();
        }


        // ==========================================
        // SAVE IMAGE
        // ==========================================

        String imagePath =
                uploadPath
                + File.separator
                + imageName;

        imagePart.write(imagePath);


        // ==========================================
        // DATABASE CONNECTION - ORACLE
        // ==========================================

        Connection connection = null;
        PreparedStatement statement = null;

        try {

            // Load Oracle driver
            Class.forName(
                    "oracle.jdbc.driver.OracleDriver"
            );


            // Connect to Oracle database
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "system",
                    "manager"
            );


            // ==========================================
            // INSERT QUERY
            // ==========================================

            String sql =
                    "INSERT INTO product_information "
                    + "(product_name, product_type, color, "
                    + "quality, price, quantity, description, "
                    + "product_image, company_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


            statement =
                    connection.prepareStatement(sql);


            // ==========================================
            // SET VALUES
            // ==========================================

            statement.setString(
                    1,
                    productName
            );

            statement.setString(
                    2,
                    productType
            );

            statement.setString(
                    3,
                    color
            );

            statement.setString(
                    4,
                    quality
            );

            statement.setDouble(
                    5,
                    Double.parseDouble(price)
            );

            statement.setInt(
                    6,
                    Integer.parseInt(quantity)
            );

            statement.setString(
                    7,
                    description
            );

            statement.setString(
                    8,
                    imageName
            );

            // Logged-in company's registration number
            statement.setString(
                    9,
                    companyid
            );


            // ==========================================
            // EXECUTE INSERT
            // ==========================================

            int result =
                    statement.executeUpdate();


            // ==========================================
            // SUCCESS
            // ==========================================

            if (result > 0) {

                response.sendRedirect(
                        "companyproducts"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            // Show error in browser
            out.println("<html>");

            out.println("<head>");
            out.println("<title>Error</title>");
            out.println("</head>");

            out.println("<body>");

            out.println(
                    "<h2>Something went wrong!</h2>"
            );

            out.println("<p>");
            out.println(e.getMessage());
            out.println("</p>");

            out.println("</body>");
            out.println("</html>");
        }


        // ==========================================
        // CLOSE DATABASE RESOURCES
        // ==========================================

        finally {

            try {

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