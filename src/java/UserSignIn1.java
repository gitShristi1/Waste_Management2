import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserSignIn1 extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        PrintWriter pw1 = res.getWriter();

        // Get data entered by user
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {

            // Load JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to database
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "system",
                    "manager"
            );

            // Create statement
            Statement stmt = con.createStatement();

            // Query database
            String q1 = "SELECT * FROM UserSignUp "
                    + "WHERE email='" + email + "' "
                    + "AND password='" + password + "'";

            ResultSet rs = stmt.executeQuery(q1);

            // Check whether user exists
            if (rs.next()) {

                // Create session
                HttpSession session = req.getSession();

                // Store user's email in session
                session.setAttribute("email", email);

                // Go to user home page
                res.sendRedirect("HomeUser.html");

            } else {

                pw1.println("<h2>Not Registered!</h2>");

            }

            con.close();

        } catch (Exception e) {

            pw1.println(e);

        }
    }
}