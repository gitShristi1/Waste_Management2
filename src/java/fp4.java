

import java.io.*;//PrintWriter
import jakarta.servlet.*;//GenericServlet
import jakarta.servlet.http.*;//HttpServlet
import java.sql.*;

public class fp4 extends HttpServlet{
    public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException{
        res.setContentType("text/html");
        String cid;
        
        PrintWriter pwl=res.getWriter();
        String npass=req.getParameter("password");
        
        
        try
    {
        HttpSession ses=req.getSession();
        cid=(String)ses.getAttribute("company_id");
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //registering type4 driver
        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
        Statement stmt=con.createStatement();
        String ql="update company set password='"+npass+"' where company_id='"+cid+"'";
        int x=stmt.executeUpdate(ql);
            if(x>0){
    pwl.println("<html>");
    pwl.println("<head>");
    pwl.println("<script>");
    pwl.println("alert('Password successfully changed');");
    pwl.println("window.location.href='companySignin.html';");
    pwl.println("</script>");
    pwl.println("</head>");
    pwl.println("<body></body>");
    pwl.println("</html>");
            }
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