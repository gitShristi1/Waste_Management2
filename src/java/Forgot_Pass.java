import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class Forgot_Pass extends HttpServlet{
    public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException{
        res.setContentType("text/html");
        PrintWriter pw=res.getWriter();
        String eid=req.getParameter("email");
        try{
            HttpSession ses=req.getSession();
        ses.setAttribute("email", eid);
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
            Statement stmt=con.createStatement();
            String ql="select * from usersignup where email='"+eid+"'";
            ResultSet rs=stmt.executeQuery(ql);
            if(rs.next()){
                pw.println(
    "<!DOCTYPE html>" +
    "<html>" +

    "<head>" +
    "<title>EcoCycle - Verify User</title>" +
    "<link rel=\"stylesheet\" href=\"fp3.css\">" +
    "</head>" +

    "<body>" +

    "<header class=\"header\">" +

    "<div class=\"logo\">" +
    "<span class=\"logo-icon\">&#9851;</span>" +
    "<span>" +
    "<font color=\"black\">Eco</font><font color=\"green\">Cycle</font>" +
    "</span>" +
    "</div>" +

    "<nav class=\"navbar\">" +
    "<a href=\"Landing.html\">Home</a>" +
    "<a href=\"#\">About</a>" +
    "<a href=\"#\">Services</a>" +
    "<a href=\"#\">Contact</a>" +
    "</nav>" +

    "</header>" +

    "<section class=\"form-section\">" +

    "<div class=\"form-card\">" +

    "<div class=\"form-heading\">" +

    "<div class=\"form-icon\">&#9851;</div>" +

    "<h1>Verify <span>User</span></h1>" +

    "<p>Enter your user name to reset your password.</p>" +

    "</div>" +

    "<form method=\"post\" action=\"fp5\">" +

    "<div class=\"form-group\">" +
    "<label>Contact Number</label>" +
    "<input type=\"text\" value=\"" + rs.getString(5) + "\" readonly>" +
    "</div>" +

    "<div class=\"form-group\">" +
    "<label>User Name</label>" +
    "<input type=\"text\" name=\"name\" " +
    "placeholder=\"Enter User name\" required>" +
    "</div>" +

    "<div class=\"form-buttons\">" +

    "<input type=\"submit\" " +
    "value=\"Submit\" " +
    "class=\"submit-btn\">" +

    "<input type=\"reset\" " +
    "value=\"Reset\" " +
    "class=\"reset-btn\">" +

    "</div>" +

    "</form>" +

    "<div class=\"back\">" +
    "<a href=\"User_signin.html\" class=\"back-link\">" +
    "&#8592; Back to Sign In" +
    "</a>" +
    "</div>" +

    "</div>" +

    "</section>" +

    "</body>" +
    "</html>"
);
            }
            else{
                pw.println("Failure");
            }
            con.close();
        }
        catch(Exception e){
        pw.println(e);
    }
    }
}