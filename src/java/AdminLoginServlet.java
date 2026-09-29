import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {


private static final long serialVersionUID = 1L;

protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html");

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {

        // Load Oracle JDBC Driver
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // Database connection
        con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE",
                "system",
                "manager"
        );

        // Check admin credentials
        String sql = "SELECT admin_id, email FROM admin "
                   + "WHERE email = ? AND password = ?";

        ps = con.prepareStatement(sql);

        ps.setString(1, email);
        ps.setString(2, password);

        rs = ps.executeQuery();

        if (rs.next()) {

            // Login successful
            int adminId = rs.getInt("admin_id");

            HttpSession session = request.getSession();

            session.setAttribute("admin_id", adminId);
            session.setAttribute("admin_email", email);
            session.setAttribute("role", "ADMIN");

            response.sendRedirect("adminDashboard.html");

        } else {

            // Login failed
            response.sendRedirect("adminSignin.html?error=invalid");

        }

    } catch (Exception e) {

        e.printStackTrace();

        response.getWriter().println(
                "<h3>Database Error: " + e.getMessage() + "</h3>"
        );

    } finally {

        try {
            if (rs != null)
                rs.close();

            if (ps != null)
                ps.close();

            if (con != null)
                con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


}
