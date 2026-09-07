import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/transactionfetch")
public class transactionfetch extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        PrintWriter pw = res.getWriter();

        HttpSession session = req.getSession(false);

        // Check company session
        if (session == null) {
            pw.println("<h2>Company session not found. Please sign in again.</h2>");
            return;
        }

        String companyName = (String) session.getAttribute("companyName");

        if (companyName == null) {
            pw.println("<h2>Company session not found. Please sign in again.</h2>");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "system",
                    "manager"
            );

            /*
             * FROM_PAY contains the company name.
             * Therefore only transactions of the
             * logged-in company will be displayed.
             */
            String ql =
                    "SELECT * FROM transactioncompany " +
                    "WHERE FROM_PAY = ?";

            ps = con.prepareStatement(ql);

            ps.setString(1, companyName);

            rs = ps.executeQuery();

            // ================= HTML =================

            pw.println("<html>");
            pw.println("<head>");

            pw.println("<title>EcoCycle - Transactions</title>");

            pw.println("<style>");

            pw.println("*{");
            pw.println("box-sizing:border-box;");
            pw.println("margin:0;");
            pw.println("padding:0;");
            pw.println("}");

            pw.println("body{");
            pw.println("font-family:Arial, sans-serif;");
            pw.println("background:#f5f9f4;");
            pw.println("color:#183c2b;");
            pw.println("}");

            // Header

            pw.println(".header{");
            pw.println("display:flex;");
            pw.println("justify-content:space-between;");
            pw.println("align-items:center;");
            pw.println("padding:18px 60px;");
            pw.println("background:white;");
            pw.println("box-shadow:0 2px 10px rgba(0,0,0,0.08);");
            pw.println("}");

            // Logo

            pw.println(".logo{");
            pw.println("display:flex;");
            pw.println("align-items:center;");
            pw.println("font-size:26px;");
            pw.println("font-weight:bold;");
            pw.println("}");

            pw.println(".logo-icon{");
            pw.println("font-size:30px;");
            pw.println("margin-right:8px;");
            pw.println("}");

            pw.println(".eco{");
            pw.println("color:#222;");
            pw.println("}");

            pw.println(".cycle{");
            pw.println("color:#2f9659;");
            pw.println("}");

            // Navbar

            pw.println(".navbar{");
            pw.println("display:flex;");
            pw.println("align-items:center;");
            pw.println("gap:30px;");
            pw.println("}");

            pw.println(".navbar a{");
            pw.println("text-decoration:none;");
            pw.println("color:#355542;");
            pw.println("font-weight:600;");
            pw.println("}");

            pw.println(".navbar a:hover{");
            pw.println("color:#2f9659;");
            pw.println("}");

            // Main

            pw.println(".main{");
            pw.println("padding:45px 60px;");
            pw.println("}");

            pw.println("h1{");
            pw.println("text-align:center;");
            pw.println("margin-bottom:10px;");
            pw.println("color:#183c2b;");
            pw.println("}");

            pw.println(".company-name{");
            pw.println("text-align:center;");
            pw.println("font-size:18px;");
            pw.println("color:#557263;");
            pw.println("margin-bottom:30px;");
            pw.println("}");

            // Table container

            pw.println(".table-box{");
            pw.println("background:white;");
            pw.println("padding:25px;");
            pw.println("border-radius:12px;");
            pw.println("border:1px solid #d7e4da;");
            pw.println("box-shadow:0 10px 25px rgba(45,86,57,0.10);");
            pw.println("overflow-x:auto;");
            pw.println("}");

            pw.println("table{");
            pw.println("width:100%;");
            pw.println("border-collapse:collapse;");
            pw.println("}");

            pw.println("th{");
            pw.println("background:#2f9659;");
            pw.println("color:white;");
            pw.println("padding:14px;");
            pw.println("}");

            pw.println("td{");
            pw.println("padding:12px;");
            pw.println("border:1px solid #d7e4da;");
            pw.println("text-align:center;");
            pw.println("}");

            pw.println("tr:nth-child(even){");
            pw.println("background:#f8fbf8;");
            pw.println("}");

            // Back button

            pw.println(".back{");
            pw.println("display:inline-block;");
            pw.println("margin-top:25px;");
            pw.println("padding:11px 22px;");
            pw.println("background:#2f9659;");
            pw.println("color:white;");
            pw.println("text-decoration:none;");
            pw.println("border-radius:8px;");
            pw.println("font-weight:bold;");
            pw.println("}");

            pw.println(".back:hover{");
            pw.println("background:#237846;");
            pw.println("}");

            // Footer

            pw.println("footer{");
            pw.println("text-align:center;");
            pw.println("padding:25px;");
            pw.println("margin-top:30px;");
            pw.println("background:white;");
            pw.println("color:#557263;");
            pw.println("}");

            // Responsive

            pw.println("@media(max-width:768px){");

            pw.println(".header{");
            pw.println("padding:18px 25px;");
            pw.println("flex-direction:column;");
            pw.println("gap:15px;");
            pw.println("}");

            pw.println(".navbar{");
            pw.println("gap:15px;");
            pw.println("flex-wrap:wrap;");
            pw.println("justify-content:center;");
            pw.println("}");

            pw.println(".main{");
            pw.println("padding:30px 20px;");
            pw.println("}");

            pw.println("}");

            pw.println("</style>");

            pw.println("</head>");

            pw.println("<body>");

            // ================= HEADER =================

            pw.println("<header class='header'>");

            pw.println("<div class='logo'>");
            pw.println("<span class='logo-icon'>&#9851;</span>");
            pw.println("<span class='eco'>Eco</span>");
            pw.println("<span class='cycle'>Cycle</span>");
            pw.println("</div>");

            pw.println("<nav class='navbar'>");
            pw.println("<a href='companyhome.html'>Home</a>");
            pw.println("<a href='#'>About</a>");
            pw.println("<a href='#'>Services</a>");
            pw.println("<a href='#'>Contact</a>");
            pw.println("</nav>");

            pw.println("</header>");

            // ================= MAIN =================

            pw.println("<div class='main'>");

            pw.println("<h1>Transaction History</h1>");

            pw.println("<div class='company-name'>");
            pw.println("Company: " + companyName);
            pw.println("</div>");

            pw.println("<div class='table-box'>");

            pw.println("<table>");

            pw.println("<tr>");
            pw.println("<th>Transaction ID</th>");
            pw.println("<th>Company Name</th>");
            pw.println("<th>User Email</th>");
            pw.println("<th>Commission</th>");
            pw.println("<th>User Receive</th>");
            pw.println("<th>Company Pay</th>");
            pw.println("</tr>");

            boolean found = false;

            while (rs.next()) {

                found = true;

                String transactionId =
                        rs.getString("transaction_id");

                String fromPay =
                        rs.getString("from_pay");

                String toPay =
                        rs.getString("to_pay");

                String commission =
                        rs.getString("commision");

                String userReceive =
                        rs.getString("u_receive");

                String companyPay =
                        rs.getString("c_pay");

                pw.println("<tr>");

                pw.println("<td>" + transactionId + "</td>");

                pw.println("<td>" + fromPay + "</td>");

                pw.println("<td>" + toPay + "</td>");

                pw.println("<td>₹" + commission + "</td>");

                pw.println("<td>₹" + userReceive + "</td>");

                pw.println("<td>₹" + companyPay + "</td>");

                pw.println("</tr>");
            }

            if (!found) {

                pw.println("<tr>");

                pw.println("<td colspan='6'>");
                pw.println("No transactions found for your company.");
                pw.println("</td>");

                pw.println("</tr>");
            }

            pw.println("</table>");

            pw.println("</div>");

            // Back button

            pw.println("<a href='companyhome.html' class='back'>");
            pw.println("← Back to Company Home");
            pw.println("</a>");

            pw.println("</div>");

            // ================= FOOTER =================

            pw.println("<footer>");
            pw.println("© 2026 EcoCycle Management System | Building a Cleaner Future &#9851;");
            pw.println("</footer>");

            pw.println("</body>");
            pw.println("</html>");

        }
        catch (Exception e) {

            e.printStackTrace();

            pw.println("<h2>Error while fetching transactions.</h2>");
            pw.println("<p>" + e.getMessage() + "</p>");

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

    // If the page is accessed using POST
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        doGet(req, res);
    }
}