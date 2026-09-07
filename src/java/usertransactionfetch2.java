import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/usertransactionfetch2")
public class usertransactionfetch2 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        PrintWriter out = res.getWriter();

        /*
         * =========================================
         * GET USER SESSION
         * =========================================
         */

        HttpSession session = req.getSession(false);

        if (session == null) {

            out.println("<h2>User session not found.</h2>");
            out.println("<p>Please sign in again.</p>");

            return;
        }

        String userEmail =
                (String) session.getAttribute("email");

        if (userEmail == null) {

            out.println("<h2>User session not found.</h2>");
            out.println("<p>Please sign in again.</p>");

            return;
        }


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            /*
             * =========================================
             * CONNECT TO DATABASE
             * =========================================
             */

            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "system",
                    "manager"
            );


            /*
             * =========================================
             * GET ONLY USER'S TRANSACTIONS
             * =========================================
             *
             * TO_PAY contains the user's email.
             */

            String sql =
                    "SELECT transaction_id, from_pay, to_pay, " +
                    "commision, u_receive, c_pay " +
                    "FROM transactioncompany " +
                    "WHERE LOWER(TRIM(to_pay)) = LOWER(TRIM(?))";


            ps = con.prepareStatement(sql);

            ps.setString(1, userEmail);

            rs = ps.executeQuery();


            /*
             * =========================================
             * HTML PAGE
             * =========================================
             */

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<meta name='viewport' " +
                    "content='width=device-width, initial-scale=1.0'>");

            out.println("<title>EcoCycle - My Transactions</title>");

            out.println("<link rel='stylesheet' href='success.css'>");

            out.println("<link rel='preconnect' " +
                    "href='https://fonts.googleapis.com'>");

            out.println("<link rel='preconnect' " +
                    "href='https://fonts.gstatic.com' crossorigin>");

            out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap' " +
                    "rel='stylesheet'>");


            /*
             * =========================================
             * EXTRA CSS
             * =========================================
             */

            out.println("<style>");

            out.println("body{");
            out.println("font-family:Inter,Arial,sans-serif;");
            out.println("background:#f5f9f4;");
            out.println("margin:0;");
            out.println("}");

            out.println(".header{");
            out.println("display:flex;");
            out.println("justify-content:space-between;");
            out.println("align-items:center;");
            out.println("padding:18px 7%;");
            out.println("background:white;");
            out.println("box-shadow:0 2px 10px rgba(0,0,0,0.08);");
            out.println("}");

            out.println(".logo{");
            out.println("display:flex;");
            out.println("align-items:center;");
            out.println("font-size:25px;");
            out.println("font-weight:800;");
            out.println("}");

            out.println(".logo-icon{");
            out.println("font-size:30px;");
            out.println("margin-right:8px;");
            out.println("}");

            out.println(".navbar{");
            out.println("display:flex;");
            out.println("gap:25px;");
            out.println("}");

            out.println(".navbar a{");
            out.println("text-decoration:none;");
            out.println("color:#355542;");
            out.println("font-weight:600;");
            out.println("}");

            out.println(".navbar a:hover{");
            out.println("color:#2f9659;");
            out.println("}");

            out.println(".transaction-section{");
            out.println("padding:50px 6%;");
            out.println("}");

            out.println(".transaction-card{");
            out.println("background:white;");
            out.println("padding:30px;");
            out.println("border-radius:15px;");
            out.println("box-shadow:0 10px 25px rgba(45,86,57,0.10);");
            out.println("overflow-x:auto;");
            out.println("}");

            out.println(".transaction-card h1{");
            out.println("text-align:center;");
            out.println("color:#355542;");
            out.println("margin-bottom:10px;");
            out.println("}");

            out.println(".transaction-card h1 span{");
            out.println("color:#2f9659;");
            out.println("}");

            out.println(".user-email{");
            out.println("text-align:center;");
            out.println("color:#666;");
            out.println("margin-bottom:30px;");
            out.println("}");

            out.println("table{");
            out.println("width:100%;");
            out.println("border-collapse:collapse;");
            out.println("}");

            out.println("th{");
            out.println("background:#2f9659;");
            out.println("color:white;");
            out.println("padding:13px;");
            out.println("}");

            out.println("td{");
            out.println("padding:12px;");
            out.println("border:1px solid #d7e4da;");
            out.println("text-align:center;");
            out.println("}");

            out.println("tr:nth-child(even){");
            out.println("background:#f8fbf8;");
            out.println("}");

            out.println(".back-btn{");
            out.println("display:inline-block;");
            out.println("margin-top:25px;");
            out.println("padding:12px 22px;");
            out.println("background:#2f9659;");
            out.println("color:white;");
            out.println("text-decoration:none;");
            out.println("border-radius:8px;");
            out.println("font-weight:700;");
            out.println("}");

            out.println(".back-btn:hover{");
            out.println("background:#237846;");
            out.println("}");

            out.println(".no-transaction{");
            out.println("text-align:center;");
            out.println("padding:30px;");
            out.println("color:#666;");
            out.println("font-size:17px;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");


            /*
             * =========================================
             * BODY
             * =========================================
             */

            out.println("<body>");


            /*
             * HEADER
             */

            out.println("<header class='header'>");

            out.println("<div class='logo'>");

            out.println("<span class='logo-icon'>&#9851;</span>");

            out.println("<span>");

            out.println("<font color='black'>Eco</font>");
            out.println("<font color='green'>Cycle</font>");

            out.println("</span>");

            out.println("</div>");


            out.println("<nav class='navbar'>");

            out.println("<a href='HomeUser.html'>Home</a>");

            out.println("<a href='usell.html'>Sell Waste</a>");

            out.println("<a href='buy-products.html'>Buy Products</a>");

            out.println("<a href='requeststatus.html'>My Activities</a>");

            out.println("<a href='#'>About Us</a>");

            out.println("</nav>");

            out.println("</header>");


            /*
             * =========================================
             * TRANSACTION SECTION
             * =========================================
             */

            out.println("<section class='transaction-section'>");

            out.println("<div class='transaction-card'>");

            out.println("<h1>My <span>Transactions</span></h1>");

            out.println("<p class='user-email'>");

            out.println("Transactions for: " + userEmail);

            out.println("</p>");


            boolean found = false;

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Transaction ID</th>");
            out.println("<th>Company Name</th>");
            out.println("<th>User Email</th>");
            out.println("<th>Commission</th>");
            out.println("<th>User Receive</th>");
            out.println("<th>Company Pay</th>");

            out.println("</tr>");


            /*
             * =========================================
             * DISPLAY TRANSACTIONS
             * =========================================
             */

            while (rs.next()) {

                found = true;

                String transactionId =
                        rs.getString("transaction_id");

                String companyName =
                        rs.getString("from_pay");

                String email =
                        rs.getString("to_pay");

                String commission =
                        rs.getString("commision");

                String userReceive =
                        rs.getString("u_receive");

                String companyPay =
                        rs.getString("c_pay");


                out.println("<tr>");

                out.println("<td>" + transactionId + "</td>");

                out.println("<td>" + companyName + "</td>");

                out.println("<td>" + email + "</td>");

                out.println("<td>₹" + commission + "</td>");

                out.println("<td>₹" + userReceive + "</td>");

                out.println("<td>₹" + companyPay + "</td>");

                out.println("</tr>");
            }

            out.println("</table>");


            /*
             * =========================================
             * NO TRANSACTION
             * =========================================
             */

            if (!found) {

                out.println("<div class='no-transaction'>");

                out.println("<h3>No Transactions Found</h3>");

                out.println("<p>");
                out.println("You do not have any completed transactions yet.");
                out.println("</p>");

                out.println("</div>");
            }


            /*
             * BACK BUTTON
             */

            out.println("<div style='text-align:center;'>");

            out.println("<a href='HomeUser.html' class='back-btn'>");

            out.println("Back to Home");

            out.println("</a>");

            out.println("</div>");


            out.println("</div>");

            out.println("</section>");


            /*
             * FOOTER
             */

            out.println("<footer class='footer'>");

            out.println("© 2026 EcoWaste Management System");

            out.println(" | Building a Cleaner Future &#9851;");

            out.println("</footer>");


            out.println("</body>");

            out.println("</html>");


        }
        catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Error while fetching transactions.</h2>");

            out.println("<p>" + e.getMessage() + "</p>");
        }
        finally {

            try {

                if (rs != null)
                    rs.close();

                if (ps != null)
                    ps.close();

                if (con != null)
                    con.close();

            }
            catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    /*
     * Also allow GET requests
     */

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        doGet(req, res);
    }
}