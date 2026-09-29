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
    @Override
protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    doPost(request, response);
}
@Override

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

        String companyid =
                (String) session.getAttribute(
                        "companyId"
                );

        if (companyid == null ||
                companyid.trim().isEmpty()) {

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
                    "companyproducts"
            );

            return;
        }

        int productId;

        try {

            productId =
                    Integer.parseInt(productIdString);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "companyproducts"
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
                    "AND COMPANY_ID = ?";

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
                    companyid
            );

            rs =
                    checkStatement.executeQuery();

            // ==========================================
            // PRODUCT NOT FOUND
            // ==========================================

            if (!rs.next()) {

                response.sendRedirect(
                        "companyproducts"
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
                    "AND COMPANY_ID = ?";

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
                    companyid
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
                        "Company ID = " + companyid
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
                    "companyproducts"
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
                    "<a href='companyproducts'>" +
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