import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/userCustomerFeedback")
public class userCustomerFeedback extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        PrintWriter out = res.getWriter();

        HttpSession session = req.getSession();

        // Get logged-in user's email
        String email = (String) session.getAttribute("email");

        if (email == null) {

            out.println("<h2>User session not found.</h2>");
            out.println("<p>Please sign in again.</p>");

            return;
        }

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "system",
                    "manager"
            );

            /*
             * IMPORTANT:
             *
             * EMAIL_ID = user's email
             * EMAIL    = company's email
             *
             * Therefore we search using EMAIL_ID.
             */

            String sql =
                    "SELECT feedback_id, email, name, rate, quality, seller " +
                    "FROM companyfeedback " +
                    "WHERE email_id = ? " +
                    "ORDER BY feedback_id DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");

            out.println("<meta charset='UTF-8'>");
            out.println("<title>My Feedback</title>");

            out.println("<style>");

            out.println("body{");
            out.println("font-family:Arial,sans-serif;");
            out.println("background:#f5f9f4;");
            out.println("margin:0;");
            out.println("padding:40px;");
            out.println("color:#183c2b;");
            out.println("}");

            out.println("h1{");
            out.println("text-align:center;");
            out.println("margin-bottom:10px;");
            out.println("}");

            out.println(".email{");
            out.println("text-align:center;");
            out.println("color:#2f9659;");
            out.println("margin-bottom:30px;");
            out.println("}");

            out.println("table{");
            out.println("width:90%;");
            out.println("margin:auto;");
            out.println("border-collapse:collapse;");
            out.println("background:white;");
            out.println("}");

            out.println("th{");
            out.println("background:#2f9659;");
            out.println("color:white;");
            out.println("padding:14px;");
            out.println("}");

            out.println("td{");
            out.println("padding:12px;");
            out.println("border:1px solid #d7e4da;");
            out.println("text-align:center;");
            out.println("}");

            out.println(".no-feedback{");
            out.println("text-align:center;");
            out.println("background:white;");
            out.println("padding:30px;");
            out.println("width:70%;");
            out.println("margin:30px auto;");
            out.println("border-radius:10px;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<h1>My Feedback</h1>");

            out.println("<div class='email'>");
            out.println("Feedback submitted using: <b>" + email + "</b>");
            out.println("</div>");

            boolean found = false;

            while (rs.next()) {

                found = true;

                String feedbackId =
                        rs.getString("feedback_id");

                String companyEmail =
                        rs.getString("email");

                String companyName =
                        rs.getString("name");

                String rate =
                        rs.getString("rate");

                String quality =
                        rs.getString("quality");

                String seller =
                        rs.getString("seller");

                if (found && rs.isFirst()) {

                    out.println("<table>");

                    out.println("<tr>");
                    out.println("<th>Feedback ID</th>");
                    out.println("<th>Company Email</th>");
                    out.println("<th>Company Name</th>");
                    out.println("<th>Rate</th>");
                    out.println("<th>Quality</th>");
                    out.println("<th>Seller</th>");
                    out.println("</tr>");
                }

                out.println("<tr>");

                out.println("<td>" + feedbackId + "</td>");

                out.println("<td>" + companyEmail + "</td>");

                out.println("<td>" + companyName + "</td>");

                out.println("<td>" + rate + "/5</td>");

                out.println("<td>" + quality + "/5</td>");

                out.println("<td>" + seller + "/5</td>");

                out.println("</tr>");
            }

            if (found) {

                out.println("</table>");

            } else {

                out.println("<div class='no-feedback'>");

                out.println("<h2>No Feedback Found</h2>");

                out.println("<p>");
                out.println("There is no company feedback associated "
                        + "with your email address.");
                out.println("</p>");

                out.println("</div>");
            }

            out.println("</body>");
            out.println("</html>");

            rs.close();
            ps.close();
            con.close();

        }
        catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Error loading feedback</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}