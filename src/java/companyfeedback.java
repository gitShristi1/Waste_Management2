import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;

@WebServlet("/companyfeedback")
@MultipartConfig
public class companyfeedback extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        String name = req.getParameter("company");
        String email = req.getParameter("email");
        String rate = req.getParameter("rate");
        String quality = req.getParameter("quality");
        String seller = req.getParameter("seller");

        Connection con = null;
        PreparedStatement pw = null;
        PrintWriter out = res.getWriter();

        String feedbackId = "";

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "system",
                    "manager"
            );


            /* =========================================
               GENERATE UNIQUE 5 DIGIT FEEDBACK ID
            ========================================= */

            boolean exists = true;

            while (exists) {

                int num = 10000 + (int)(Math.random() * 90000);

                feedbackId = String.valueOf(num);

                String checkSql =
                        "SELECT feedback_id FROM companyfeedback "
                        + "WHERE feedback_id = ?";

                PreparedStatement checkPs =
                        con.prepareStatement(checkSql);

                checkPs.setString(1, feedbackId);

                ResultSet checkRs =
                        checkPs.executeQuery();

                if (checkRs.next()) {
                    exists = true;
                }
                else {
                    exists = false;
                }

                checkRs.close();
                checkPs.close();
            }


            /* =========================================
               INSERT FEEDBACK
            ========================================= */

            String sql =
                "INSERT INTO companyfeedback " +
                "(feedback_id, name, email, rate, quality, seller) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

            pw = con.prepareStatement(sql);

            pw.setString(1, feedbackId);
            pw.setString(2, name);
            pw.setString(3, email);
            pw.setString(4, rate);
            pw.setString(5, quality);
            pw.setString(6, seller);

            int result = pw.executeUpdate();


            /* =========================================
               HTML PAGE
            ========================================= */

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<title>EcoCycle - Feedback</title>");

            out.println("<link rel='preconnect' "
                    + "href='https://fonts.googleapis.com'>");

            out.println("<link rel='preconnect' "
                    + "href='https://fonts.gstatic.com' crossorigin>");

            out.println("<link href='https://fonts.googleapis.com/css2?"
                    + "family=Inter:wght@400;500;600;700;800&display=swap' "
                    + "rel='stylesheet'>");


            /* =========================================
               CSS
            ========================================= */

            out.println("<style>");

            out.println("*{");
            out.println("margin:0;");
            out.println("padding:0;");
            out.println("box-sizing:border-box;");
            out.println("}");

            out.println("body{");
            out.println("font-family:'Inter',sans-serif;");
            out.println("background:#f5f9f4;");
            out.println("color:#183c2b;");
            out.println("line-height:1.6;");
            out.println("}");

            out.println("a{");
            out.println("text-decoration:none;");
            out.println("color:inherit;");
            out.println("}");

            /* HEADER */

            out.println(".header{");
            out.println("height:78px;");
            out.println("padding:0 7%;");
            out.println("display:flex;");
            out.println("align-items:center;");
            out.println("justify-content:space-between;");
            out.println("background:rgba(255,255,255,0.95);");
            out.println("border-bottom:1px solid #e4eee5;");
            out.println("}");

            out.println(".logo{");
            out.println("display:flex;");
            out.println("align-items:center;");
            out.println("gap:8px;");
            out.println("font-size:24px;");
            out.println("font-weight:800;");
            out.println("}");

            out.println(".logo-icon{");
            out.println("color:#3c9b63;");
            out.println("font-size:30px;");
            out.println("}");

            out.println(".eco{");
            out.println("color:black;");
            out.println("}");

            out.println(".cycle{");
            out.println("color:#31975b;");
            out.println("}");

            /* NAVIGATION */

            out.println(".navbar{");
            out.println("display:flex;");
            out.println("gap:35px;");
            out.println("}");

            out.println(".navbar a{");
            out.println("font-size:14px;");
            out.println("font-weight:600;");
            out.println("color:#52685b;");
            out.println("transition:0.3s;");
            out.println("}");

            out.println(".navbar a:hover{");
            out.println("color:#2e9659;");
            out.println("}");

            /* SECTION */

            out.println(".form-section{");
            out.println("min-height:calc(100vh - 78px);");
            out.println("display:flex;");
            out.println("justify-content:center;");
            out.println("align-items:center;");
            out.println("padding:60px 20px;");
            out.println("background:radial-gradient("
                    + "circle at 80% 20%,"
                    + "#dcefdc 0%,"
                    + "#f5f9f4 45%);");
            out.println("}");

            /* CARD */

            out.println(".form-card{");
            out.println("width:100%;");
            out.println("max-width:550px;");
            out.println("padding:45px;");
            out.println("background:rgba(255,255,255,0.97);");
            out.println("border:1px solid #e1ebe3;");
            out.println("border-radius:20px;");
            out.println("box-shadow:0 25px 60px "
                    + "rgba(45,86,57,0.15);");
            out.println("text-align:center;");
            out.println("}");

            /* ICON */

            out.println(".form-icon{");
            out.println("width:65px;");
            out.println("height:65px;");
            out.println("display:flex;");
            out.println("align-items:center;");
            out.println("justify-content:center;");
            out.println("margin:0 auto 20px;");
            out.println("background:#e5f4e6;");
            out.println("border-radius:50%;");
            out.println("font-size:30px;");
            out.println("color:#31975b;");
            out.println("}");

            /* SUCCESS */

            out.println(".success h1{");
            out.println("font-size:28px;");
            out.println("color:#183c2b;");
            out.println("margin-bottom:10px;");
            out.println("}");

            out.println(".success h1 span{");
            out.println("color:#31975b;");
            out.println("}");

            out.println(".success p{");
            out.println("font-size:14px;");
            out.println("color:#718078;");
            out.println("margin-bottom:25px;");
            out.println("}");

            /* FEEDBACK ID BOX */

            out.println(".feedback-box{");
            out.println("padding:15px;");
            out.println("background:#f5f9f4;");
            out.println("border:1px solid #d7e4da;");
            out.println("border-radius:10px;");
            out.println("margin-bottom:15px;");
            out.println("font-size:14px;");
            out.println("color:#355542;");
            out.println("}");

            out.println(".feedback-box strong{");
            out.println("color:#2f9659;");
            out.println("font-size:18px;");
            out.println("}");

            /* EMAIL BOX */

            out.println(".email-box{");
            out.println("padding:15px;");
            out.println("background:#f5f9f4;");
            out.println("border:1px solid #d7e4da;");
            out.println("border-radius:10px;");
            out.println("margin-bottom:25px;");
            out.println("font-size:14px;");
            out.println("color:#355542;");
            out.println("}");

            out.println(".email-box strong{");
            out.println("color:#2f9659;");
            out.println("}");

            /* BUTTON */

            out.println(".back-btn{");
            out.println("display:inline-block;");
            out.println("padding:12px 25px;");
            out.println("background:#2f9659;");
            out.println("color:white;");
            out.println("border-radius:8px;");
            out.println("font-size:14px;");
            out.println("font-weight:700;");
            out.println("box-shadow:0 8px 20px "
                    + "rgba(47,150,89,0.20);");
            out.println("transition:0.3s;");
            out.println("}");

            out.println(".back-btn:hover{");
            out.println("background:#237846;");
            out.println("transform:translateY(-2px);");
            out.println("}");

            /* ERROR */

            out.println(".error h1{");
            out.println("font-size:28px;");
            out.println("color:#183c2b;");
            out.println("margin-bottom:10px;");
            out.println("}");

            out.println(".error p{");
            out.println("font-size:14px;");
            out.println("color:#718078;");
            out.println("margin-bottom:25px;");
            out.println("}");

            /* FOOTER */

            out.println(".footer{");
            out.println("padding:25px;");
            out.println("background:#102d20;");
            out.println("color:#aec1b3;");
            out.println("text-align:center;");
            out.println("font-size:12px;");
            out.println("}");

            /* MOBILE */

            out.println("@media(max-width:600px){");

            out.println(".header{");
            out.println("padding:0 5%;");
            out.println("}");

            out.println(".navbar{");
            out.println("display:none;");
            out.println("}");

            out.println(".form-card{");
            out.println("padding:35px 25px;");
            out.println("}");

            out.println("}");

            out.println("</style>");

            out.println("</head>");


            /* =========================================
               BODY
            ========================================= */

            out.println("<body>");


            /* HEADER */

            out.println("<header class='header'>");

            out.println("<div class='logo'>");

            out.println("<span class='logo-icon'>&#9851;</span>");

            out.println("<span>");
            out.println("<span class='eco'>Eco</span>"
                    + "<span class='cycle'>Cycle</span>");
            out.println("</span>");

            out.println("</div>");


            out.println("<nav class='navbar'>");

            out.println("<a href='#'>Home</a>");
            out.println("<a href='#'>About</a>");
            out.println("<a href='#'>Services</a>");
            out.println("<a href='#'>Contact</a>");

            out.println("</nav>");

            out.println("</header>");


            /* SECTION */

            out.println("<section class='form-section'>");

            out.println("<div class='form-card'>");


            if (result > 0) {

                /* SUCCESS */

                out.println("<div class='success'>");

                out.println("<div class='form-icon'>");
                out.println("&#10003;");
                out.println("</div>");

                out.println("<h1>Feedback "
                        + "<span>Submitted!</span></h1>");

                out.println("<p>");
                out.println("Your feedback has been submitted "
                        + "successfully.");
                out.println("</p>");


                /* FEEDBACK ID */

                out.println("<div class='feedback-box'>");

                out.println("Your Feedback ID: ");

                out.println("<strong>");
                out.println(feedbackId);
                out.println("</strong>");

                out.println("</div>");


                /* EMAIL */

                out.println("<div class='email-box'>");

                out.println("Feedback submitted by: ");

                out.println("<strong>");
                out.println(email);
                out.println("</strong>");

                out.println("</div>");


                out.println("<a href='companyhome.html' "
                        + "class='back-btn'>");

                out.println("Back to Home Page");

                out.println("</a>");

                out.println("</div>");


            }
            else {

                /* ERROR */

                out.println("<div class='error'>");

                out.println("<div class='form-icon'>");
                out.println("!");
                out.println("</div>");

                out.println("<h1>Feedback "
                        + "<span>Failed</span></h1>");

                out.println("<p>");
                out.println("Your feedback could not be submitted.");
                out.println("</p>");

                out.println("<a href='companyhome.html' "
                        + "class='back-btn'>");

                out.println("&#8592; Back to Home Page");

                out.println("</a>");

                out.println("</div>");
            }


            out.println("</div>");

            out.println("</section>");


            /* FOOTER */

            out.println("<footer class='footer'>");

            out.println("© 2026 EcoWaste Management System");

            out.println(" | Building a Cleaner Future &#9851;");

            out.println("</footer>");


            out.println("</body>");
            out.println("</html>");

        }

        catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Error while storing feedback details.</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }

        finally {

            try {

                if (pw != null)
                    pw.close();

                if (con != null)
                    con.close();

            }
            catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}