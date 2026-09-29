package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class AdminUserFeedbackServlet extends HttpServlet {

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

            ResultSet rs = st.executeQuery(
                    "SELECT * FROM USER_BUY_FEEDBACK ORDER BY FEEDBACK_DATE DESC");

            out.println("<html>");

            out.println("<head>");

            out.println("<title>User Feedback</title>");

            out.println("<link rel='stylesheet' href='userFeedback.css'>");

            out.println("</head>");

            out.println("<body>");

            out.println("<div class='header'>");

            out.println("<h2>User Feedback</h2>");

            out.println("<a href='adminUserOptions.html' class='btn'>Back</a>");

            out.println("</div>");

            out.println("<div class='container'>");

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Feedback ID</th>");

            out.println("<th>Name</th>");

            out.println("<th>Rating</th>");

            out.println("<th>Feedback</th>");

            out.println("<th>Date</th>");

            out.println("</tr>");

            while(rs.next()){

                out.println("<tr>");

                out.println("<td>"+rs.getString("FEEDBACK_ID")+"</td>");

                out.println("<td>"+rs.getString("NAME")+"</td>");

                out.println("<td>"+rs.getString("RATING")+" ⭐</td>");

                out.println("<td>"+rs.getString("FEEDBACK")+"</td>");

                out.println("<td>"+rs.getTimestamp("FEEDBACK_DATE")+"</td>");

                out.println("</tr>");

            }

            out.println("</table>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            rs.close();
            st.close();
            con.close();

        }

        catch(Exception e){

            out.println(e);

        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request,response);

    }

}