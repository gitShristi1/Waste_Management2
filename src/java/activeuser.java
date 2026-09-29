import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/adminUsers")
public class activeuser extends HttpServlet {


protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");

    PrintWriter pw = res.getWriter();

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
         * usersignup table contains:
         *
         * EMAIL
         * NAME
         * PASSWORD
         * ADDRESS
         * CONTACT
         *
         * There is no ACTIVE column,
         * so all registered users are displayed.
         */

        String sql =
                "SELECT EMAIL, NAME, ADDRESS, CONTACT " +
                "FROM usersignup";

        ps = con.prepareStatement(sql);

        rs = ps.executeQuery();


        // ================= HTML =================

        pw.println("<html>");

        pw.println("<head>");

        pw.println("<title>Users | EcoCycle</title>");

        pw.println("<style>");

        pw.println("*{");
        pw.println("box-sizing:border-box;");
        pw.println("margin:0;");
        pw.println("padding:0;");
        pw.println("}");

        pw.println("body{");
        pw.println("font-family:Arial,sans-serif;");
        pw.println("background:#f5f9f4;");
        pw.println("color:#183c2b;");
        pw.println("}");


        /* ================= HEADER ================= */

        pw.println(".header{");
        pw.println("display:flex;");
        pw.println("justify-content:space-between;");
        pw.println("align-items:center;");
        pw.println("padding:18px 60px;");
        pw.println("background:white;");
        pw.println("box-shadow:0 2px 10px rgba(0,0,0,0.08);");
        pw.println("}");


        pw.println(".logo-section{");
        pw.println("display:flex;");
        pw.println("align-items:center;");
        pw.println("gap:8px;");
        pw.println("}");

        pw.println(".logo{");
        pw.println("font-size:30px;");
        pw.println("}");

        pw.println(".logo-section h2{");
        pw.println("font-size:26px;");
        pw.println("color:#2f9659;");
        pw.println("}");


        /* ================= ADMIN ================= */

        pw.println(".admin-section{");
        pw.println("display:flex;");
        pw.println("align-items:center;");
        pw.println("gap:25px;");
        pw.println("}");

        pw.println(".admin-info{");
        pw.println("display:flex;");
        pw.println("flex-direction:column;");
        pw.println("align-items:flex-end;");
        pw.println("}");

        pw.println(".admin-name{");
        pw.println("font-weight:bold;");
        pw.println("}");

        pw.println(".admin-role{");
        pw.println("font-size:13px;");
        pw.println("color:#557263;");
        pw.println("}");

        pw.println(".logout-btn{");
        pw.println("text-decoration:none;");
        pw.println("background:#2f9659;");
        pw.println("color:white;");
        pw.println("padding:10px 20px;");
        pw.println("border-radius:7px;");
        pw.println("font-weight:bold;");
        pw.println("}");

        pw.println(".logout-btn:hover{");
        pw.println("background:#237846;");
        pw.println("}");


        /* ================= CONTAINER ================= */

        pw.println(".container{");
        pw.println("display:flex;");
        pw.println("min-height:calc(100vh - 75px);");
        pw.println("}");


        /* ================= SIDEBAR ================= */

        pw.println(".sidebar{");
        pw.println("width:230px;");
        pw.println("background:#183c2b;");
        pw.println("padding:30px 15px;");
        pw.println("}");

        pw.println(".sidebar h3{");
        pw.println("color:white;");
        pw.println("font-size:20px;");
        pw.println("margin-bottom:25px;");
        pw.println("padding-left:15px;");
        pw.println("}");

        pw.println(".sidebar a{");
        pw.println("display:flex;");
        pw.println("align-items:center;");
        pw.println("gap:12px;");
        pw.println("text-decoration:none;");
        pw.println("color:#dce9df;");
        pw.println("padding:14px 15px;");
        pw.println("margin-bottom:8px;");
        pw.println("border-radius:7px;");
        pw.println("font-weight:600;");
        pw.println("}");

        pw.println(".sidebar a:hover{");
        pw.println("background:#2f9659;");
        pw.println("color:white;");
        pw.println("}");

        pw.println(".sidebar a.active{");
        pw.println("background:#2f9659;");
        pw.println("color:white;");
        pw.println("}");


        /* ================= MAIN CONTENT ================= */

        pw.println(".main-content{");
        pw.println("flex:1;");
        pw.println("padding:45px 60px;");
        pw.println("}");

        pw.println(".page-heading{");
        pw.println("margin-bottom:30px;");
        pw.println("}");

        pw.println(".page-heading h1{");
        pw.println("font-size:32px;");
        pw.println("margin-bottom:8px;");
        pw.println("}");

        pw.println(".page-heading p{");
        pw.println("color:#557263;");
        pw.println("font-size:16px;");
        pw.println("}");


        /* ================= TABLE ================= */

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
        pw.println("text-align:center;");
        pw.println("}");

        pw.println("td{");
        pw.println("padding:12px;");
        pw.println("border:1px solid #d7e4da;");
        pw.println("text-align:center;");
        pw.println("}");

        pw.println("tr:nth-child(even){");
        pw.println("background:#f8fbf8;");
        pw.println("}");


        /* ================= ACTIVE STATUS ================= */

        pw.println(".active-badge{");
        pw.println("display:inline-block;");
        pw.println("background:#e2f5e8;");
        pw.println("color:#237846;");
        pw.println("padding:6px 12px;");
        pw.println("border-radius:20px;");
        pw.println("font-weight:bold;");
        pw.println("font-size:13px;");
        pw.println("}");


        /* ================= BACK BUTTON ================= */

        pw.println(".back-btn{");
        pw.println("display:inline-block;");
        pw.println("margin-top:25px;");
        pw.println("padding:11px 22px;");
        pw.println("background:#2f9659;");
        pw.println("color:white;");
        pw.println("text-decoration:none;");
        pw.println("border-radius:8px;");
        pw.println("font-weight:bold;");
        pw.println("}");

        pw.println(".back-btn:hover{");
        pw.println("background:#237846;");
        pw.println("}");


        /* ================= FOOTER ================= */

        pw.println("footer{");
        pw.println("text-align:center;");
        pw.println("padding:22px;");
        pw.println("background:white;");
        pw.println("color:#557263;");
        pw.println("border-top:1px solid #d7e4da;");
        pw.println("}");


        pw.println("</style>");

        pw.println("</head>");


        // ================= BODY =================

        pw.println("<body>");


        // ================= HEADER =================

        pw.println("<header class='header'>");

        pw.println("<div class='logo-section'>");

        pw.println("<div class='logo'>♻</div>");

        pw.println("<h2>EcoCycle</h2>");

        pw.println("</div>");


        pw.println("<div class='admin-section'>");

        pw.println("<div class='admin-info'>");

        pw.println("<span class='admin-name'>Admin</span>");

        pw.println("<span class='admin-role'>Administrator</span>");

        pw.println("</div>");


        pw.println("<a href='AdminLogoutServlet' class='logout-btn'>");
        pw.println("Logout");
        pw.println("</a>");

        pw.println("</div>");

        pw.println("</header>");


        // ================= CONTAINER =================

        pw.println("<div class='container'>");


        // ================= SIDEBAR =================

        pw.println("<aside class='sidebar'>");

        pw.println("<h3>Admin Panel</h3>");


        pw.println("<a href='adminDashboard.html'>");
        pw.println("<span>▣</span>");
        pw.println("Dashboard");
        pw.println("</a>");


        pw.println("<a href='adminUserOptions.html' class='active'>");
        pw.println("<span>👤</span>");
        pw.println("Users");
        pw.println("</a>");


        pw.println("<a href='adminCompanyOptions.html'>");
        pw.println("<span>🏢</span>");
        pw.println("Companies");
        pw.println("</a>");


        pw.println("</aside>");


        // ================= MAIN CONTENT =================

        pw.println("<main class='main-content'>");


        pw.println("<div class='page-heading'>");

        pw.println("<h1>Active Users</h1>");

        pw.println("<p>");
        pw.println("All registered users of the EcoCycle platform.");
        pw.println("</p>");

        pw.println("</div>");


        // ================= TABLE =================

        pw.println("<div class='table-box'>");

        pw.println("<table>");


        pw.println("<tr>");

        pw.println("<th>Email</th>");
        pw.println("<th>Name</th>");
        pw.println("<th>Address</th>");
        pw.println("<th>Contact</th>");
        pw.println("<th>Status</th>");

        pw.println("</tr>");


        boolean found = false;


        while (rs.next()) {

            found = true;

            String email =
                    rs.getString("EMAIL");

            String name =
                    rs.getString("NAME");

            String address =
                    rs.getString("ADDRESS");

            String contact =
                    rs.getString("CONTACT");


            pw.println("<tr>");

            pw.println("<td>" + email + "</td>");

            pw.println("<td>" + name + "</td>");

            pw.println("<td>" + address + "</td>");

            pw.println("<td>" + contact + "</td>");

            pw.println("<td>");

            pw.println("<span class='active-badge'>");
            pw.println("ACTIVE");
            pw.println("</span>");

            pw.println("</td>");

            pw.println("</tr>");
        }


        if (!found) {

            pw.println("<tr>");

            pw.println("<td colspan='5'>");

            pw.println("No users found.");

            pw.println("</td>");

            pw.println("</tr>");
        }


        pw.println("</table>");

        pw.println("</div>");


        // ================= BACK BUTTON =================

        pw.println("<a href='adminUserOptions.html' class='back-btn'>");

        pw.println("← Back to User Management");

        pw.println("</a>");


        pw.println("</main>");

        pw.println("</div>");


        // ================= FOOTER =================

        pw.println("<footer>");

        pw.println("© 2026 EcoCycle Management System | Building a Cleaner Future ♻");

        pw.println("</footer>");


        pw.println("</body>");

        pw.println("</html>");


    }
    catch (Exception e) {

        e.printStackTrace();

        pw.println("<h2>Error while fetching users.</h2>");

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


}
