
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class companySignin extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pwl = res.getWriter();

        String cid = req.getParameter("regno");
        String pss = req.getParameter("password");

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "system",
                    "manager"
            );

            String ql = "SELECT * FROM company WHERE company_id=? AND password=?";

            PreparedStatement ps = conn.prepareStatement(ql);

            ps.setString(1, cid);
            ps.setString(2, pss);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String companyName = rs.getString("company_name");

                /*
                 * Create a fresh session after successful login
                 */
                HttpSession oldSession = req.getSession(false);

                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession session = req.getSession(true);

                session.setAttribute("companyName", companyName);
                session.setAttribute("companyId", cid);

                /*
                 * Redirect to company home
                 */
                res.sendRedirect(
                        res.encodeRedirectURL("companyhome.html")
                );

            } else {

                pwl.println("<h2>Login Failed!!!</h2>");
            }

            rs.close();
            ps.close();
            conn.close();

        }
        catch (Exception e) {

            e.printStackTrace();

            pwl.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
    }
}
