import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/removeProduct")
public class removeProduct extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // ==========================================
        // GET SESSION
        // ==========================================

        HttpSession session =
                request.getSession(false);

        // ==========================================
        // CHECK LOGIN
        // ==========================================

        if (session == null) {

            response.sendRedirect(
                    "companySignin.html"
            );

            return;
        }

        // ==========================================
        // GET COMPANY REGISTRATION NUMBER
        // ==========================================

        String companyRegno =
                (String) session.getAttribute(
                        "companyRegno"
                );

        if (companyRegno == null ||
                companyRegno.trim().isEmpty()) {

            response.sendRedirect(
                    "companySignin.html"
            );

            return;
        }

        // ==========================================
        // GET PRODUCT ID
        // ==========================================

        String productIdString =
                request.getParameter("productId");

        if (productIdString == null ||
                productIdString.trim().isEmpty()) {

            response.sendRedirect(
                    "companyProducts"
            );

            return;
        }

        int productId;

        try {

            productId =
                    Integer.parseInt(productIdString);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "companyProducts"
            );

            return;
        }

        Connection connection = null;
        PreparedStatement checkStatement = null;
        PreparedStatement deleteStatement = null;
        ResultSet rs = null;

        try {

            // ==========================================
            // ORACLE DATABASE CONNECTION
            // ==========================================

            Class.forName(
                    "oracle.jdbc.driver.OracleDriver"
            );

            connection =
                    DriverManager.getConnection(
                            "jdbc:oracle:thin:@localhost:1521:xe",
                            "system",
                            "manager"
                    );

            // ==========================================
            // CHECK PRODUCT BELONGS TO COMPANY
            // ==========================================

            String checkSql =
                    "SELECT product_id " +
                    "FROM product_information " +
                    "WHERE product_id = ? " +
                    "AND COMPANY_REGNO = ?";

            checkStatement =
                    connection.prepareStatement(
                            checkSql
                    );

            checkStatement.setInt(
                    1,
                    productId
            );

            checkStatement.setString(
                    2,
                    companyRegno
            );

            rs =
                    checkStatement.executeQuery();

            // ==========================================
            // PRODUCT NOT FOUND
            // ==========================================

            if (!rs.next()) {

                response.sendRedirect(
                        "companyProducts"
                );

                return;
            }

            rs.close();
            rs = null;

            checkStatement.close();
            checkStatement = null;

            // ==========================================
            // DELETE PRODUCT
            // ==========================================

            String deleteSql =
                    "DELETE FROM product_information " +
                    "WHERE product_id = ? " +
                    "AND COMPANY_REGNO = ?";

            deleteStatement =
                    connection.prepareStatement(
                            deleteSql
                    );

            deleteStatement.setInt(
                    1,
                    productId
            );

            deleteStatement.setString(
                    2,
                    companyRegno
            );

            int rowsDeleted =
                    deleteStatement.executeUpdate();

            // ==========================================
            // CHECK DELETE RESULT
            // ==========================================

            if (rowsDeleted > 0) {

                System.out.println(
                        "Product deleted successfully."
                );

                System.out.println(
                        "Product ID = " + productId
                );

                System.out.println(
                        "Company Regno = " + companyRegno
                );

            } else {

                System.out.println(
                        "Product could not be deleted."
                );
            }

            // ==========================================
            // RETURN TO MY PRODUCTS
            // ==========================================

            response.sendRedirect(
                    "companyProducts"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println(
                    "<html>" +
                    "<body>" +
                    "<h2>Unable to remove product</h2>" +
                    "<p>" +
                    e.getMessage() +
                    "</p>" +
                    "<a href='companyProducts'>" +
                    "Back to My Products" +
                    "</a>" +
                    "</body>" +
                    "</html>"
            );

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

                if (checkStatement != null) {
                    checkStatement.close();
                }

                if (deleteStatement != null) {
                    deleteStatement.close();
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