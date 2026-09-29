

import java.io.*;//PrintWriter
import jakarta.servlet.*;//GenericServlet
import jakarta.servlet.http.*;//HttpServlet
import java.sql.*;

public class fp5 extends HttpServlet{
    public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException{
        res.setContentType("text/html");
        String eid;
        
        PrintWriter pwl=res.getWriter();
        String uname=req.getParameter("name");
        
        
        try
    {
        HttpSession ses=req.getSession();
        eid=(String)ses.getAttribute("email");
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //registering type4 driver
        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
        Statement stmt=con.createStatement();
        //pwl.println("Company ID:"+cid);
        //pwl.println("Company name:"+cmp_name);
        String ql="select * from usersignup where email='"+eid+"' and name='"+uname+"'";
        ResultSet rs=stmt.executeQuery(ql);
            if(rs.next()){
                pwl.println("<html>\n" +
"<head>\n" +
"<title>EcoCycle - Reset Password</title>\n" +
"<link rel=\"stylesheet\" href=\"fp4.css\">\n" +
"</head>\n" +

"<body>\n" +

"<!-- HEADER -->\n" +
"<header class=\"header\">\n" +

"<div class=\"logo\">\n" +
"<span class=\"logo-icon\">&#9851;</span>\n" +
"<span><font color=\"black\">Eco</font><font color=\"green\">Cycle</font></span>\n" +
"</div>\n" +

"<nav class=\"navbar\">\n" +
"<a href=\"Landing.html\">Home</a>\n" +
"<a href=\"#\">About</a>\n" +
"<a href=\"#\">Services</a>\n" +
"<a href=\"#\">Contact</a>\n" +
"</nav>\n" +

"</header>\n" +


"<!-- FORM SECTION -->\n" +
"<section class=\"form-section\">\n" +

"<div class=\"form-card\">\n" +

"<div class=\"form-heading\">\n" +

"<div class=\"form-icon\">&#9851;</div>\n" +

"<h1>Reset <span>Password</span></h1>\n" +

"<p>Enter your new password to secure your account.</p>\n" +

"</div>\n" +


"<form method=\"post\" action=\"fp6\">\n" +

"<div class=\"form-group\">\n" +

"<label>New Password</label>\n" +

"<input type=\"password\" " +
"name=\"password\" " +
"placeholder=\"Enter new password\" " +
"required>\n" +

"</div>\n" +


"<div class=\"form-buttons\">\n" +

"<input type=\"submit\" " +
"value=\"Submit\" " +
"class=\"submit-btn\">\n" +

"<input type=\"reset\" " +
"value=\"Reset\" " +
"class=\"reset-btn\">\n" +

"</div>\n" +

"</form>\n" +


"<div class=\"back\">\n" +

"<a href=\"User_signin.html\" class=\"back-link\">" +
"&#8592; Back to Sign In" +
"</a>\n" +

"</div>\n" +

"</div>\n" +

"</section>\n" +


"</body>\n" +
"</html>");            }
            else{
                pwl.println("Failure");
            }
        
        con.close();
    }
    catch(Exception e){
        pwl.println(e);
    }
    }
}