import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/userlogout")
public class userlogout extends HttpServlet {

    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
            throws ServletException, IOException {

        // Get the existing session
        HttpSession session = req.getSession(false);

        // Destroy the session
        if (session != null) {
            session.invalidate();
        }

        // Go back to user sign-in page
        res.sendRedirect("User_signin.html");
    }
}

