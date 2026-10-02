import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class requestfetch extends HttpServlet {
    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    doPost(req, res);
}

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pwl = res.getWriter();

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "system",
                    "manager"
            );

            Statement stmt = con.createStatement();

            String ql = "select * from wastedetails where lower(trim(status))='false'";

            ResultSet rs = stmt.executeQuery(ql);

            pwl.println("<html>");
            pwl.println("<head>");
            pwl.println("<meta charset='UTF-8'>");
            pwl.println("<title>EcoCycle - Waste Requests</title>");

            pwl.println("<style>");

            /* ================= HEADER ================= */

            pwl.println("body{");
            pwl.println("font-family:Arial;");
            pwl.println("background:#f5f9f4;");
            pwl.println("padding:20px;");
            pwl.println("margin:0;");
            pwl.println("}");

            pwl.println(".header{");
            pwl.println("display:flex;");
            pwl.println("justify-content:space-between;");
            pwl.println("align-items:center;");
            pwl.println("padding:18px 35px;");
            pwl.println("background:white;");
            pwl.println("border-bottom:1px solid #d7e4da;");
            pwl.println("margin:-20px -20px 25px -20px;");
            pwl.println("}");

            pwl.println(".logo{");
            pwl.println("display:flex;");
            pwl.println("align-items:center;");
            pwl.println("gap:10px;");
            pwl.println("font-size:25px;");
            pwl.println("font-weight:800;");
            pwl.println("}");

            pwl.println(".logo-icon{");
            pwl.println("font-size:30px;");
            pwl.println("color:green;");
            pwl.println("}");

            pwl.println(".navbar{");
            pwl.println("display:flex;");
            pwl.println("gap:30px;");
            pwl.println("}");

            pwl.println(".navbar a{");
            pwl.println("text-decoration:none;");
            pwl.println("color:#355542;");
            pwl.println("font-weight:600;");
            pwl.println("}");

            pwl.println(".navbar a:hover{");
            pwl.println("color:#2f9659;");
            pwl.println("}");

            /* ================= TABLE ================= */

            pwl.println("table{");
            pwl.println("width:100%;");
            pwl.println("border-collapse:collapse;");
            pwl.println("background:white;");
            pwl.println("}");

            pwl.println("th{");
            pwl.println("background:#2f9659;");
            pwl.println("color:white;");
            pwl.println("padding:12px;");
            pwl.println("}");

            pwl.println("td{");
            pwl.println("padding:10px;");
            pwl.println("border:1px solid #d7e4da;");
            pwl.println("text-align:center;");
            pwl.println("}");

            pwl.println("img{");
            pwl.println("width:120px;");
            pwl.println("height:100px;");
            pwl.println("object-fit:cover;");
            pwl.println("border-radius:8px;");
            pwl.println("}");

            /* ================= ACCEPT BOX ================= */

            pwl.println(".accept-box{");
            pwl.println("background:white;");
            pwl.println("padding:25px;");
            pwl.println("margin-bottom:25px;");
            pwl.println("border-radius:12px;");
            pwl.println("border:1px solid #d7e4da;");
            pwl.println("box-shadow:0 10px 25px rgba(45,86,57,0.10);");
            pwl.println("}");

            pwl.println(".accept-box label{");
            pwl.println("display:block;");
            pwl.println("font-weight:700;");
            pwl.println("color:#355542;");
            pwl.println("margin-bottom:10px;");
            pwl.println("}");

            pwl.println(".accept-box input[type=text]{");
            pwl.println("width:350px;");
            pwl.println("height:42px;");
            pwl.println("padding:0 12px;");
            pwl.println("border:1px solid #d7e4da;");
            pwl.println("border-radius:8px;");
            pwl.println("background:#f8fbf8;");
            pwl.println("font-size:14px;");
            pwl.println("outline:none;");
            pwl.println("}");

            pwl.println(".accept-box input[type=text]:focus{");
            pwl.println("border-color:#3c9b63;");
            pwl.println("box-shadow:0 0 0 3px rgba(60,155,99,0.10);");
            pwl.println("}");

            pwl.println(".accept-btn{");
            pwl.println("margin-left:10px;");
            pwl.println("height:42px;");
            pwl.println("padding:0 20px;");
            pwl.println("border:none;");
            pwl.println("border-radius:8px;");
            pwl.println("background:#2f9659;");
            pwl.println("color:white;");
            pwl.println("font-weight:700;");
            pwl.println("cursor:pointer;");
            pwl.println("}");

            pwl.println(".accept-btn:hover{");
            pwl.println("background:#237846;");
            pwl.println("}");

            pwl.println("</style>");
            pwl.println("</head>");

            pwl.println("<body>");

            /* ================= HEADER ================= */

            pwl.println("<header class='header'>");

            pwl.println("<div class='logo'>");

            pwl.println("<span class='logo-icon'>");
            pwl.println("&#9851;");
            pwl.println("</span>");

            pwl.println("<span>");
            pwl.println("<font color='black'>Eco</font>");
            pwl.println("<font color='green'>Cycle</font>");
            pwl.println("</span>");

            pwl.println("</div>");

            pwl.println("<nav class='navbar'>");

            pwl.println("<a href='#'>Home</a>");
            pwl.println("<a href='#'>About</a>");
            pwl.println("<a href='#'>Services</a>");
            pwl.println("<a href='#'>Contact</a>");

            pwl.println("</nav>");

            pwl.println("</header>");

            /* ================= PAGE TITLE ================= */

            pwl.println("<h1>Waste Collection Requests</h1>");

            /* ================= ACCEPT WASTE ================= */

            pwl.println("<div class='accept-box'>");

            HttpSession session = req.getSession(false);

String companyName = "";
String companyId = "";

if (session != null) {
    if (session.getAttribute("companyName") != null) {
        companyName = (String) session.getAttribute("companyName");
    }

    if (session.getAttribute("companyId") != null) {
        companyId = (String) session.getAttribute("companyId");
    }
}

pwl.println("<form method='post' action='acceptwaste'>");

pwl.println("<input type='hidden' name='companyName' value='" 
        + companyName + "'>");

pwl.println("<input type='hidden' name='companyId' value='" 
        + companyId + "'>");

            pwl.println(
                "<label>Enter the request id of the waste that you want to accept</label>"
            );

            pwl.println(
                "<input type='text' name='n1' placeholder='Request_ID' required>"
            );

            pwl.println(
                "<input type='submit' value='Accept Waste' class='accept-btn'>"
            );

            pwl.println("</form>");

            pwl.println("</div>");

            /* ================= TABLE ================= */

            pwl.println("<table>");

            pwl.println("<tr>");
            pwl.println("<th>Request ID</th>");
            pwl.println("<th>Email ID</th>");
            pwl.println("<th>Waste Type</th>");
            pwl.println("<th>Weight</th>");
            pwl.println("<th>Image</th>");
            pwl.println("<th>Location</th>");
            pwl.println("<th>Extra Information</th>");
            pwl.println("<th>Company Cost</th>");
            pwl.println("</tr>");

            while (rs.next()) {

                String reqid = rs.getString(1);
                String email = rs.getString(2);
                String type = rs.getString(3);
                String weight = rs.getString(4);
                String imageVideo = rs.getString(5);
                String location = rs.getString(6);
                String extra = rs.getString(7);
                String company_cost = rs.getString(11);

                pwl.println("<tr>");

                pwl.println("<td>" + reqid + "</td>");

                pwl.println("<td>" + email + "</td>");

                pwl.println("<td>" + type + "</td>");

                pwl.println("<td>" + weight + " g</td>");

                /* ================= IMAGE ================= */

                pwl.println("<td>");

                if (imageVideo != null && !imageVideo.equals("")) {

                    String imageName = imageVideo.split(",")[0];

                    pwl.println(
                        "<img src='uploads/" +
                        imageName +
                        "' alt='Waste Image'>"
                    );

                }
                else {

                    pwl.println("No Image");

                }

                pwl.println("</td>");

                pwl.println("<td>" + location + "</td>");

                pwl.println("<td>" + extra + "</td>");

                pwl.println("<td>" + company_cost + "</td>");

                pwl.println("</tr>");
            }

            pwl.println("</table>");

            pwl.println("</body>");
            pwl.println("</html>");

            con.close();

        }
        catch (Exception e) {

            e.printStackTrace();

            pwl.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
    }
}