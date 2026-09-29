package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class AdminUserProductsBoughtServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rsTotal = null;

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "system",
                    "manager");

            st = con.createStatement();

            rsTotal = st.executeQuery(
                    "SELECT SUM(TOTAL_AMOUNT), SUM(COMMISSION) FROM transactions");

            double totalSales = 0.0;
            double totalCommission = 0.0;

            if (rsTotal.next()) {

                totalSales = rsTotal.getDouble(1);
                totalCommission = rsTotal.getDouble(2);

            }

            rs = st.executeQuery(
                    "SELECT * FROM transactions ORDER BY TRANSACTION_DATE DESC");

            out.println("<html>");

            out.println("<head>");

            out.println("<title>Products Bought</title>");

            out.println("<link rel='stylesheet' href='productsBought.css'>");

            out.println("</head>");

            out.println("<body>");

            /* HEADER */

            out.println("<div class='header'>");

            out.println("<div class='logo'>♻ EcoCycle</div>");

            out.println("<div class='header-right'>");

            out.println("<div class='summary-box'>");
            out.println("Total Sales : ₹ " + totalSales);
            out.println("</div>");

            out.println("<div class='summary-box'>");
            out.println("Total Commission : ₹ " + totalCommission);
            out.println("</div>");

            out.println("<a href='adminUserOptions.html' class='btn'>Back</a>");

            out.println("</div>");

            out.println("</div>");

            /* CONTENT */

            out.println("<div class='container'>");

            out.println("<h1>Products Bought</h1>");

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Transaction ID</th>");

            out.println("<th>Buyer Email</th>");

            out.println("<th>Company ID</th>");

            out.println("<th>Product</th>");

            out.println("<th>Quantity</th>");

            out.println("<th>Total Amount</th>");

            out.println("<th>Commission</th>");

            out.println("<th>Company Amount</th>");

            out.println("<th>Date</th>");

            out.println("<th>Status</th>");

            out.println("</tr>");

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>" + rs.getInt("TRANSACTION_ID") + "</td>");

                out.println("<td>" + rs.getString("BUYER_EMAIL") + "</td>");

                out.println("<td>" + rs.getString("COMPANY_ID") + "</td>");

                out.println("<td>" + rs.getString("PRODUCT_NAME") + "</td>");

                out.println("<td>" + rs.getInt("QUANTITY") + "</td>");

                out.println("<td>₹ " + rs.getDouble("TOTAL_AMOUNT") + "</td>");

                out.println("<td>₹ " + rs.getDouble("COMMISSION") + "</td>");

                out.println("<td>₹ " + rs.getDouble("COMPANY_AMOUNT") + "</td>");

                out.println("<td>" + rs.getTimestamp("TRANSACTION_DATE") + "</td>");

                out.println("<td>" + rs.getString("STATUS") + "</td>");

                out.println("</tr>");

            }

            out.println("</table>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            rs.close();
            rsTotal.close();
            st.close();
            con.close();

        }

        catch (Exception e) {

            out.println("<h2>" + e.getMessage() + "</h2>");

            e.printStackTrace();

        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);

    }

}