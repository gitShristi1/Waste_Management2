package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class ViewPaymentsServlet extends HttpServlet {

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

            // Calculate total commission
            rsTotal = st.executeQuery("SELECT SUM(COMMISION) FROM transactioncompany");

            double totalCommission = 0.0;

            if (rsTotal.next()) {
                totalCommission = rsTotal.getDouble(1);
            }

            rs = st.executeQuery("SELECT * FROM transactioncompany");

            out.println("<html>");

            out.println("<head>");

            out.println("<title>User Transactions</title>");

            out.println("<link rel='stylesheet' href='payments.css'>");

            out.println("</head>");

            out.println("<body>");

            /*================ HEADER ================*/

            out.println("<div class='header'>");

            out.println("<div class='logo'>♻ EcoCycle</div>");

            out.println("<div class='header-right'>");

            out.println("<div class='commission-box'>");

            out.println("Total Commission Earned : ₹ " + totalCommission);

            out.println("</div>");

            out.println("<a href='ViewWasteServlet' class='btn'>Back</a>");

            out.println("</div>");

            out.println("</div>");

            /*================ CONTAINER ================*/

            out.println("<div class='container'>");

            out.println("<h1>User Transactions</h1>");

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Transaction ID</th>");

            out.println("<th>From</th>");

            out.println("<th>To</th>");

            out.println("<th>Commission</th>");

            out.println("<th>User Receives</th>");

            out.println("<th>Company Pays</th>");

            out.println("</tr>");

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>" + rs.getString("TRANSACTION_ID") + "</td>");

                out.println("<td>" + rs.getString("FROM_PAY") + "</td>");

                out.println("<td>" + rs.getString("TO_PAY") + "</td>");

                out.println("<td>₹ " + rs.getString("COMMISION") + "</td>");

                out.println("<td>₹ " + rs.getString("U_RECEIVE") + "</td>");

                out.println("<td>₹ " + rs.getString("C_PAY") + "</td>");

                out.println("</tr>");

            }

            out.println("</table>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

        } catch (Exception e) {

            out.println("<h2>" + e.getMessage() + "</h2>");
            e.printStackTrace();

        } finally {

            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
            }

            try {
                if (rsTotal != null)
                    rsTotal.close();
            } catch (Exception e) {
            }

            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
            }

            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
            }

        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);

    }

}