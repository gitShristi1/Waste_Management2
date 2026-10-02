import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/acceptwaste")
public class acceptwaste extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        PrintWriter pw = res.getWriter();

        String requestId = req.getParameter("n1");

        // Get logged-in company name
       // Get the existing HTTP session
HttpSession session = req.getSession(false);

if (session == null || session.getAttribute("companyName") == null) {
    pw.println("<h2>Company session not found. Please sign in again.</h2>");
    return;
}

String companyName = (String) session.getAttribute("companyName");
String companyId = (String) session.getAttribute("companyId");

        Connection con = null;
        PreparedStatement ps = null;

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "system",
                    "manager"
            );

            // Start transaction
            con.setAutoCommit(false);

            /*
             * =========================================
             * GET DATA FROM WASTEDETAILS
             * =========================================
             */

            String getData =
                    "SELECT email, cost, commission, company_cost " +
                    "FROM wastedetails " +
                    "WHERE request_id = ? " +
                    "AND LOWER(TRIM(status)) = 'false'";

            PreparedStatement psData = con.prepareStatement(getData);

            psData.setString(1, requestId);

            ResultSet rs = psData.executeQuery();

            int result = 0;

            String email = "";
            String cost = "";
            String commission = "";
            String companyCost = "";
            String transactionId = "";

            if (rs.next()) {

                email = rs.getString("email");
                cost = rs.getString("cost");
                commission = rs.getString("commission");
                companyCost = rs.getString("company_cost");
                session.setAttribute("userEmail", email);
                /*
                 * =========================================
                 * GENERATE UNIQUE TRANSACTION ID
                 * =========================================
                 */

                String transactionQuery =
                        "SELECT 'T' || LPAD(transaction_seq.NEXTVAL, 9, '0') " +
                        "FROM dual";

                Statement stmt = con.createStatement();

                ResultSet rs2 = stmt.executeQuery(transactionQuery);

                if (rs2.next()) {
                    transactionId = rs2.getString(1);
                }


                /*
                 * =========================================
                 * INSERT INTO TRANSACTIONCOMPANY
                 * =========================================
                 */

                String transactionSQL =
                        "INSERT INTO transactioncompany " +
                        "(transaction_id, from_pay, to_pay, commision, u_receive, c_pay) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement psTransaction =
                        con.prepareStatement(transactionSQL);

                psTransaction.setString(1, transactionId);

                // Company buying the waste
                psTransaction.setString(2, companyName);

                // User selling the waste
                psTransaction.setString(3, email);

                // Commission
                psTransaction.setString(4, commission);

                // Amount received by user
                psTransaction.setString(5, cost);

                // Total amount paid by company
                psTransaction.setString(6, companyCost);

                psTransaction.executeUpdate();


                /*
                 * =========================================
                 * UPDATE WASTE REQUEST STATUS
                 * =========================================
                 */

                String sql =
                        "UPDATE wastedetails " +
                        "SET status = 'true' " +
                        "WHERE request_id = ?";

                ps = con.prepareStatement(sql);

                ps.setString(1, requestId);

                result = ps.executeUpdate();


                // Commit only when both operations are successful
                if (result > 0) {
                    con.commit();
                }
                else {
                    con.rollback();
                }
            }


            /* =========================================
               HTML
            ========================================= */

            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");

            pw.println("<meta charset='UTF-8'>");

            pw.println("<title>EcoCycle - Accept Waste</title>");

            pw.println("<link rel='preconnect' href='https://fonts.googleapis.com'>");

            pw.println("<link rel='preconnect' "
                    + "href='https://fonts.gstatic.com' crossorigin>");

            pw.println("<link href='https://fonts.googleapis.com/css2?"
                    + "family=Inter:wght@400;500;600;700;800&display=swap' "
                    + "rel='stylesheet'>");


            /* =========================================
               CSS
            ========================================= */

            pw.println("<style>");

            pw.println("*{");
            pw.println("margin:0;");
            pw.println("padding:0;");
            pw.println("box-sizing:border-box;");
            pw.println("}");

            pw.println("body{");
            pw.println("font-family:'Inter',sans-serif;");
            pw.println("background:#f5f9f4;");
            pw.println("color:#183c2b;");
            pw.println("line-height:1.6;");
            pw.println("}");

            pw.println("a{");
            pw.println("text-decoration:none;");
            pw.println("color:inherit;");
            pw.println("}");

            /* HEADER */

            pw.println(".header{");
            pw.println("height:78px;");
            pw.println("padding:0 7%;");
            pw.println("display:flex;");
            pw.println("align-items:center;");
            pw.println("justify-content:space-between;");
            pw.println("background:rgba(255,255,255,0.95);");
            pw.println("border-bottom:1px solid #e4eee5;");
            pw.println("}");

            pw.println(".logo{");
            pw.println("display:flex;");
            pw.println("align-items:center;");
            pw.println("gap:8px;");
            pw.println("font-size:24px;");
            pw.println("font-weight:800;");
            pw.println("}");

            pw.println(".logo-icon{");
            pw.println("color:#3c9b63;");
            pw.println("font-size:30px;");
            pw.println("}");

            pw.println(".eco{");
            pw.println("color:black;");
            pw.println("}");

            pw.println(".cycle{");
            pw.println("color:#31975b;");
            pw.println("}");

            /* NAVIGATION */

            pw.println(".navbar{");
            pw.println("display:flex;");
            pw.println("gap:35px;");
            pw.println("}");

            pw.println(".navbar a{");
            pw.println("font-size:14px;");
            pw.println("font-weight:600;");
            pw.println("color:#52685b;");
            pw.println("transition:0.3s;");
            pw.println("}");

            pw.println(".navbar a:hover{");
            pw.println("color:#2e9659;");
            pw.println("}");

            /* SECTION */

            pw.println(".form-section{");
            pw.println("min-height:calc(100vh - 78px);");
            pw.println("display:flex;");
            pw.println("justify-content:center;");
            pw.println("align-items:center;");
            pw.println("padding:60px 20px;");
            pw.println("background:radial-gradient("
                    + "circle at 80% 20%,"
                    + "#dcefdc 0%,"
                    + "#f5f9f4 45%);");
            pw.println("}");

            /* CARD */

            pw.println(".form-card{");
            pw.println("width:100%;");
            pw.println("max-width:550px;");
            pw.println("padding:45px;");
            pw.println("background:rgba(255,255,255,0.97);");
            pw.println("border:1px solid #e1ebe3;");
            pw.println("border-radius:20px;");
            pw.println("box-shadow:0 25px 60px "
                    + "rgba(45,86,57,0.15);");
            pw.println("text-align:center;");
            pw.println("}");

            /* ICON */

            pw.println(".form-icon{");
            pw.println("width:65px;");
            pw.println("height:65px;");
            pw.println("display:flex;");
            pw.println("align-items:center;");
            pw.println("justify-content:center;");
            pw.println("margin:0 auto 20px;");
            pw.println("background:#e5f4e6;");
            pw.println("border-radius:50%;");
            pw.println("font-size:30px;");
            pw.println("color:#31975b;");
            pw.println("}");

            /* SUCCESS */

            pw.println(".success h1{");
            pw.println("font-size:28px;");
            pw.println("color:#183c2b;");
            pw.println("margin-bottom:10px;");
            pw.println("}");

            pw.println(".success h1 span{");
            pw.println("color:#31975b;");
            pw.println("}");

            pw.println(".success p{");
            pw.println("font-size:14px;");
            pw.println("color:#718078;");
            pw.println("margin-bottom:25px;");
            pw.println("}");

            /* EMAIL BOX */

            pw.println(".email-box{");
            pw.println("padding:15px;");
            pw.println("background:#f5f9f4;");
            pw.println("border:1px solid #d7e4da;");
            pw.println("border-radius:10px;");
            pw.println("margin-bottom:25px;");
            pw.println("font-size:14px;");
            pw.println("color:#355542;");
            pw.println("}");

            pw.println(".email-box strong{");
            pw.println("color:#2f9659;");
            pw.println("}");

            /* BUTTON */

            pw.println(".back-btn{");
            pw.println("display:inline-block;");
            pw.println("padding:12px 25px;");
            pw.println("background:#2f9659;");
            pw.println("color:white;");
            pw.println("border-radius:8px;");
            pw.println("font-size:14px;");
            pw.println("font-weight:700;");
            pw.println("box-shadow:0 8px 20px "
                    + "rgba(47,150,89,0.20);");
            pw.println("transition:0.3s;");
            pw.println("}");

            pw.println(".back-btn:hover{");
            pw.println("background:#237846;");
            pw.println("transform:translateY(-2px);");
            pw.println("}");

            /* ERROR */

            pw.println(".error h1{");
            pw.println("font-size:28px;");
            pw.println("color:#183c2b;");
            pw.println("margin-bottom:10px;");
            pw.println("}");

            pw.println(".error p{");
            pw.println("font-size:14px;");
            pw.println("color:#718078;");
            pw.println("margin-bottom:25px;");
            pw.println("}");

            /* FOOTER */

            pw.println(".footer{");
            pw.println("padding:25px;");
            pw.println("background:#102d20;");
            pw.println("color:#aec1b3;");
            pw.println("text-align:center;");
            pw.println("font-size:12px;");
            pw.println("}");

            /* MOBILE */

            pw.println("@media(max-width:600px){");

            pw.println(".header{");
            pw.println("padding:0 5%;");
            pw.println("}");

            pw.println(".navbar{");
            pw.println("display:none;");
            pw.println("}");

            pw.println(".form-card{");
            pw.println("padding:35px 25px;");
            pw.println("}");

            pw.println("}");

            pw.println("</style>");

            pw.println("</head>");


            /* =========================================
               BODY
            ========================================= */

            pw.println("<body>");

            /* HEADER */

            pw.println("<header class='header'>");

            pw.println("<div class='logo'>");

            pw.println("<span class='logo-icon'>&#9851;</span>");

            pw.println("<span>");
            pw.println("<span class='eco'>Eco</span>"
                    + "<span class='cycle'>Cycle</span>");
            pw.println("</span>");

            pw.println("</div>");

            pw.println("<nav class='navbar'>");

            pw.println("<a href='#'>Home</a>");
            pw.println("<a href='#'>About</a>");
            pw.println("<a href='#'>Services</a>");
            pw.println("<a href='#'>Contact</a>");

            pw.println("</nav>");

            pw.println("</header>");


            /* SECTION */

            pw.println("<section class='form-section'>");

            pw.println("<div class='form-card'>");


            if (result > 0) {

                /* SUCCESS */

                pw.println("<div class='success'>");

                pw.println("<div class='form-icon'>");
                pw.println("&#10003;");
                pw.println("</div>");

                pw.println("<h1>Waste <span>Accepted!</span></h1>");

                pw.println("<p>");
                pw.println("The waste collection request has been "
                        + "accepted successfully.");
                pw.println("</p>");

                pw.println("<div class='email-box'>");
                pw.println("Request associated with: ");
                pw.println("<strong>" + requestId + "</strong>");
                pw.println("</div>");

                /*
                 * SHOW TRANSACTION ID
                 */

                pw.println("<div class='email-box'>");
                pw.println("Transaction ID: ");
                pw.println("<strong>" + transactionId + "</strong>");
                pw.println("</div>");

                pw.println("<a href='companyfeedback.html' class='back-btn'>");
                pw.println("Feedback");
                pw.println("</a>");

                pw.println("</div>");

            }
            else {

                /* NOT FOUND */

                pw.println("<div class='error'>");

                pw.println("<div class='form-icon'>");
                pw.println("!");
                pw.println("</div>");

                pw.println("<h1>Request <span>Not Found</span></h1>");

                pw.println("<p>");
                pw.println("No pending waste request was found for the "
                        + "given Request ID.");
                pw.println("</p>");

                pw.println("<a href='c_request.html' class='back-btn'>");
                pw.println("&#8592; Back to Requests");
                pw.println("</a>");

                pw.println("</div>");
            }


            pw.println("</div>");

            pw.println("</section>");


            /* FOOTER */

            pw.println("<footer class='footer'>");

            pw.println("© 2026 EcoWaste Management System");
            pw.println(" | Building a Cleaner Future &#9851;");

            pw.println("</footer>");

            pw.println("</body>");
            pw.println("</html>");


        }
        catch (Exception e) {

            try {
                if (con != null) {
                    con.rollback();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();

            pw.println("<h2>Error while accepting waste request.</h2>");
            pw.println("<p>" + e.getMessage() + "</p>");

        }
        finally {

            try {

                if (ps != null)
                    ps.close();

                if (con != null)
                    con.close();

            }
            catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}