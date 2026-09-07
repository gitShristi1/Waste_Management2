import java.io.*;//PrintWriter
import jakarta.servlet.*;//GenericServlet
import jakarta.servlet.http.*;//HttpServlet
import java.sql.*;

public class companySignin extends HttpServlet{
    public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException{
        res.setContentType("text/html");
        PrintWriter pwl=res.getWriter();
        String cid=req.getParameter("regno");
        String pss=req.getParameter("password");
        
        try
    {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //registering type4 driver
        Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","manager");
        Statement stmt=conn.createStatement();
        String ql="select * from company where company_id='"+cid+"' and password='"+pss+"'";
        ResultSet rs=stmt.executeQuery(ql);
            if(rs.next()){
    String companyName = rs.getString("company_name");

    HttpSession session = req.getSession();
    session.setAttribute("companyName", companyName);
    session.setAttribute("companyId", cid);

    res.sendRedirect("companyhome.html");
}
            else{
                pwl.println("Login Failed!!!");
            }
        
        conn.close();
    }
    catch(Exception e){
        pwl.println(e);
    }
    }
}