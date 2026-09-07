import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class forget_password extends HttpServlet{
    public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException{
        res.setContentType("text/html");
        PrintWriter pw=res.getWriter();
        String cid=req.getParameter("regno");
        try{
            HttpSession ses=req.getSession();
        ses.setAttribute("company_id", cid);
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
            Statement stmt=con.createStatement();
            String ql="select * from company where company_id='"+cid+"'";
            ResultSet rs=stmt.executeQuery(ql);
            if(rs.next()){
                pw.println(
    "<!DOCTYPE html>" +
    "<html>" +

    "<head>" +
    "<title>EcoCycle - Verify Company</title>" +
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

    "<h1>Verify <span>Company</span></h1>" +

    "<p>Enter your company name to reset your password.</p>" +

    "</div>" +

    "<form method=\"post\" action=\"fp3\">" +

    "<div class=\"form-group\">" +
    "<label>Email ID</label>" +
    "<input type=\"text\" value=\"" + rs.getString(3) + "\" readonly>" +
    "</div>" +

    "<div class=\"form-group\">" +
    "<label>Company Name</label>" +
    "<input type=\"text\" name=\"company_name\" " +
    "placeholder=\"Enter company name\" required>" +
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
    "<a href=\"companySignin.html\" class=\"back-link\">" +
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