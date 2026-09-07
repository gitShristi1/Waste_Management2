import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/logout")
public class companylogout extends HttpServlet {

    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
            throws ServletException, IOException {

        // Get the current session
        HttpSession session = req.getSession(false);

        // Destroy the session
        if (session != null) {
            session.invalidate();
        }

        // Redirect to company sign-in page
        res.sendRedirect("companySignin.html");
    }
}

