package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class ViewWasteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "system",
                    "manager");

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM wastedetails");

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Waste Details</title>");
            out.println("<link rel='stylesheet' href='wasteDetails.css'>");
            out.println("</head>");
            out.println("<body>");

out.println("<div class='header'>");

out.println("<div class='logo'>");
out.println("♻ EcoCycle");
out.println("</div>");
out.println("<a href='adminUserOptions.html' class='btn'>");
out.println("Back");
out.println("</a>");
out.println("<a href='ViewPaymentsServlet' class='btn'>");
out.println("View User Transactions");
out.println("</a>");

out.println("</div>");

out.println("<div class='container'>");

out.println("<h1>Waste Details</h1>");

out.println("<table>");

            out.println("<tr>");
            out.println("<th>Request ID</th>");
            out.println("<th>Email</th>");
            out.println("<th>Type</th>");
            out.println("<th>Weight</th>");
            out.println("<th>Location</th>");
            out.println("<th>Cost</th>");
            out.println("</tr>");

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>" + rs.getString("REQUEST_ID") + "</td>");
                out.println("<td>" + rs.getString("EMAIL") + "</td>");
                out.println("<td>" + rs.getString("TYPE") + "</td>");
                out.println("<td>" + rs.getString("WEIGHT") + "</td>");


                out.println("<td>" + rs.getString("LOCATION") + "</td>");

                out.println("<td>₹ "
                        + rs.getString("COST")
                        + "</td>");


                out.println("</tr>");

            }

            out.println("</table>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {

            out.println(e);

        }

    }
    @Override
protected void doPost(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    doGet(request, response);

}

}