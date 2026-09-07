import java.io.*;//PrintWriter
import jakarta.servlet.*;//GenericServlet
import jakarta.servlet.http.*;//HttpServlet
import java.sql.*;

public class companySignup extends HttpServlet{
    public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException{
        res.setContentType("text/html");
        PrintWriter pwl=res.getWriter();
        String nm=req.getParameter("company");
        String cid=req.getParameter("regno");
        String pss=req.getParameter("password");
        String add=req.getParameter("location");
        String eid=req.getParameter("email");
        String con=req.getParameter("contact");
        try
    {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //registering type4 driver
        Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
        Statement stmt=conn.createStatement();
        String ql="insert into company values('"+cid+"','"+nm+"','"+eid+"','"+con+"','"+add+"','"+pss+"')";
        int x=stmt.executeUpdate(ql);
        if(x>0){
            pwl.println("<html><body>Registration Success<br>"+" <a href=companySignup.html>Go to Sign Up Page</a></body></html>");
        }
        else{
            pwl.println("Insert unsuccess");
        }
        conn.close();
    }
    catch(Exception e){
        pwl.println(e);
    }
    }
}