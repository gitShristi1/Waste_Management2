import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class Insert_user extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 res.setContentType("text/html");
		 PrintWriter pw1=res.getWriter();
		 String name = req.getParameter("name");
		 String email = req.getParameter("email");
		 String password = req.getParameter("password");
		 String address = req.getParameter("address");
		 String contact = req.getParameter("contact");
		// pw1.println("<html><body bgcolor=yellow> <h1>Welcome<h1> <br> E MAIL ID - "+mail+"<br> PASSWORD IS - "+pass+
		//		"<br> NAME - "+name+"<br> ADDRESS - "+add+"<br> GENDER - "+gen+ "<br>SEQURITYQUEATION - "+sq+ "<br> ANS - "
		//		+ans+" <br> CONTACT INFO - "+cno+"</body></html>");
	    	 try {
	    		 Class.forName("oracle.jdbc.driver.OracleDriver");
	    		//registering type 4 driver 
	    		 Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","manager");
	    		 		
	    		 Statement stmt=con.createStatement();
	    		 String q1="insert into UserSignUp values('"+name+"','"+email+"','"+password+"','"+address+"','"+contact+"')";
	    				 
	    	    int x=stmt.executeUpdate(q1);
	    		 if(x>0) {
	    			 res.sendRedirect("HomeUser.html");
	    		 }
	    		 else {
	    			 pw1.println("Insert Unsuccess");
	    		 }
	    		con.close();
	    	 }
	    	 catch(Exception e) {
	    		 pw1.println(e);
	    	 
	     }
	}

}