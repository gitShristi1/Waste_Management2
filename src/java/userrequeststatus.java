import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/requeststatus")
public class userrequeststatus extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        PrintWriter out = res.getWriter();

        /*
         * =========================================
         * GET REQUEST ID FROM HTML FORM
         * =========================================
         */

        String requestId = req.getParameter("request_id");


        /*
         * =========================================
         * GET LOGGED-IN USER EMAIL FROM SESSION
         * =========================================
         */

        HttpSession session = req.getSession(false);

        if (session == null) {

            out.println("<h2>User session not found.</h2>");
            out.println("<p>Please sign in again.</p>");

            return;
        }

        String userEmail =
                (String) session.getAttribute("email");


        if (userEmail == null) {

            out.println("<h2>User session not found.</h2>");
            out.println("<p>Please sign in again.</p>");

            return;
        }


        /*
         * =========================================
         * DATABASE VARIABLES
         * =========================================
         */

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {

            /*
             * =========================================
             * CONNECT TO ORACLE
             * =========================================
             */

            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "system",
                    "manager"
            );


            /*
             * =========================================
             * SQL QUERY
             * =========================================
             *
             * Check BOTH:
             *
             * 1. Request ID
             * 2. Logged-in user's email
             *
             * This prevents one user from checking
             * another user's request.
             */

            String sql =
                "SELECT request_id, email, type, weight, " +
                "image, location, extra_info, cost, status " +
                "FROM wastedetails " +
                "WHERE request_id = ? " +
                "AND email = ?";


            ps = con.prepareStatement(sql);

            ps.setString(1, requestId);
            ps.setString(2, userEmail);


            /*
             * =========================================
             * EXECUTE QUERY
             * =========================================
             */

            rs = ps.executeQuery();


            /*
             * =========================================
             * REQUEST FOUND
             * =========================================
             */

            if (rs.next()) {

                String email = rs.getString("email");
                String waste = rs.getString("type");
                String weight = rs.getString("weight");
                String image = rs.getString("image");
                String location = rs.getString("location");
                String extraInfo = rs.getString("extra_info");
                String cost = rs.getString("cost");
                String status = rs.getString("status");


                /*
                 * =========================================
                 * HTML PAGE
                 * =========================================
                 */

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");

                out.println("<meta charset='UTF-8'>");

                out.println("<meta name='viewport' " +
                            "content='width=device-width, initial-scale=1.0'>");

                out.println("<title>EcoCycle - Request Status</title>");

                out.println("<link rel='stylesheet' href='success.css'>");

                out.println("<link rel='preconnect' " +
                            "href='https://fonts.googleapis.com'>");

                out.println("<link rel='preconnect' " +
                            "href='https://fonts.gstatic.com' crossorigin>");

                out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap' " +
                            "rel='stylesheet'>");

                out.println("</head>");

                out.println("<body>");


                /*
                 * =========================================
                 * HEADER
                 * =========================================
                 */

                out.println("<header class='header'>");

                out.println("<div class='logo'>");

                out.println("<span class='logo-icon'>&#9851;</span>");

                out.println("<span>");

                out.println("<font color='black'>Eco</font>");
                out.println("<font color='green'>Cycle</font>");

                out.println("</span>");

                out.println("</div>");


                out.println("<nav class='navbar'>");

                out.println("<a href='HomeUser.html'>Home</a>");

                out.println("<a href='usell.html'>Sell Waste</a>");

                out.println("<a href='buy-products.html'>Buy Products</a>");

                out.println("<a href='requeststatus.html'>My Activities</a>");

                out.println("<a href='#'>About Us</a>");

                out.println("</nav>");

                out.println("</header>");


                /*
                 * =========================================
                 * STATUS SECTION
                 * =========================================
                 */

                out.println("<section class='success-section'>");

                out.println("<div class='success-card'>");


                /*
                 * STATUS ICON
                 */

                if (status != null &&
                    status.equalsIgnoreCase("TRUE")) {

                    out.println("<div class='success-icon'>");
                    out.println("&#10003;");
                    out.println("</div>");

                }
                else {

                    out.println("<div class='error-icon'>");
                    out.println("&#8987;");
                    out.println("</div>");
                }


                /*
                 * HEADING
                 */

                out.println("<h1>");

                out.println("Request <span>Status</span>");

                out.println("</h1>");


                /*
                 * INFORMATION BOX
                 */

                out.println("<div class='info-box'>");


                /*
                 * REQUEST ID
                 */

                out.println("<div class='info-row'>");

                out.println("<span class='info-label'>Request ID</span>");

                out.println("<span class='info-value'>");

                out.println(requestId);

                out.println("</span>");

                out.println("</div>");


                /*
                 * EMAIL
                 */

                out.println("<div class='info-row'>");

                out.println("<span class='info-label'>Email ID</span>");

                out.println("<span class='info-value'>");

                out.println(email);

                out.println("</span>");

                out.println("</div>");


                /*
                 * WASTE TYPE
                 */

                out.println("<div class='info-row'>");

                out.println("<span class='info-label'>Waste Type</span>");

                out.println("<span class='info-value'>");

                out.println(waste);

                out.println("</span>");

                out.println("</div>");


                /*
                 * WEIGHT
                 */

                out.println("<div class='info-row'>");

                out.println("<span class='info-label'>Weight</span>");

                out.println("<span class='info-value'>");

                out.println(weight + " grams");

                out.println("</span>");

                out.println("</div>");


                /*
                 * LOCATION
                 */

                out.println("<div class='info-row'>");

                out.println("<span class='info-label'>Pickup Location</span>");

                out.println("<span class='info-value'>");

                out.println(location);

                out.println("</span>");

                out.println("</div>");


                /*
                 * COST
                 */

                out.println("<div class='info-row'>");

                out.println("<span class='info-label'>Estimated Value</span>");

                out.println("<span class='info-value'>");

                out.println("₹" + cost);

                out.println("</span>");

                out.println("</div>");


                /*
                 * STATUS
                 */

                out.println("<div class='info-row'>");

                out.println("<span class='info-label'>Status</span>");

                if (status != null &&
                    status.equalsIgnoreCase("TRUE")) {

                    out.println("<span class='info-value'>");
                    out.println("ACCEPTED");
                    out.println("</span>");

                }
                else {

                    out.println("<span class='info-value'>");
                    out.println("PENDING");
                    out.println("</span>");
                }

                out.println("</div>");


                out.println("</div>");


                /*
                 * =========================================
                 * STATUS MESSAGE
                 * =========================================
                 */

                if (status != null &&
                    status.equalsIgnoreCase("TRUE")) {

                    out.println("<p class='error-message' " +
                                "style='color:green;'>");

                    out.println("Your waste request has been accepted " +
                                "by the company.");

                    out.println("</p>");

                }
                else {

                    out.println("<p class='error-message'>");

                    out.println("Your waste request is currently pending. " +
                                "Please wait for the company to accept it.");

                    out.println("</p>");
                }


                /*
                 * =========================================
                 * BACK BUTTON
                 * =========================================
                 */

                out.println("<a href='HomeUser.html' " +
                            "class='home-btn'>");

                out.println("Back to Home");

                out.println("</a>");
                out.println("<a href='usertransactionfetch2' class='home-btn'>");
                out.println("View Transaction Details");
                out.println("</a>");


                out.println("</div>");

                out.println("</section>");


                /*
                 * =========================================
                 * FOOTER
                 * =========================================
                 */

                out.println("<footer class='footer'>");

                out.println("© 2026 EcoWaste Management System");

                out.println(" | Building a Cleaner Future &#9851;");

                out.println("</footer>");


                out.println("</body>");

                out.println("</html>");

            }


            /*
             * =========================================
             * REQUEST NOT FOUND
             * =========================================
             *
             * This also happens if the request ID belongs
             * to another user.
             */

            else {

                out.println("<!DOCTYPE html>");

                out.println("<html>");

                out.println("<head>");

                out.println("<meta charset='UTF-8'>");

                out.println("<title>EcoCycle - Request Not Found</title>");

                out.println("<link rel='stylesheet' href='success.css'>");

                out.println("</head>");

                out.println("<body>");

                out.println("<section class='success-section'>");

                out.println("<div class='success-card'>");

                out.println("<div class='error-icon'>");

                out.println("&#10007;");

                out.println("</div>");

                out.println("<h1 class='error-title'>");

                out.println("Request <span>Not Found</span>");

                out.println("</h1>");

                out.println("<p class='error-message'>");

                out.println("No request was found with this Request ID " +
                            "for your account.");

                out.println("<br><strong>");

                out.println(requestId);

                out.println("</strong>");

                out.println("</p>");

                out.println("<a href='userrequeststatus.html' " +
                            "class='retry-btn'>");

                out.println("Try Again");

                out.println("</a>");

                out.println("</div>");

                out.println("</section>");

                out.println("</body>");

                out.println("</html>");
            }


        }
        catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Error while fetching request status.</h2>");

            out.println("<p>" + e.getMessage() + "</p>");

        }
        finally {

            try {

                if (rs != null)
                    rs.close();

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