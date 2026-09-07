import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.util.UUID;

@WebServlet("/usell")
@MultipartConfig
public class usell extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html");

        String emailid = req.getParameter("id");
        String waste = req.getParameter("waste");
        String weight = req.getParameter("weight");
        String location = req.getParameter("loc");
        String extraInfo = req.getParameter("ei");

        Part image = req.getPart("waste_pic");

        String imageFile = "";

        /*
         * =========================================
         * GENERATE UNIQUE REQUEST ID
         * =========================================
         */

        String requestId = "REQ-" + UUID.randomUUID().toString();


        /*
         * =========================================
         * SAVE IMAGE
         * =========================================
         */

        if (image != null && image.getSize() > 0) {

            String fileName = image.getSubmittedFileName();

            String uploadPath = getServletContext()
                    .getRealPath("/uploads");

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            File file = new File(uploadDir, fileName);

            image.write(file.getAbsolutePath());

            imageFile = fileName;
        }


        /*
         * =========================================
         * CALCULATE COST
         * =========================================
         */

        double w = Double.parseDouble(weight);

        double rate = 0;

        if (waste.equals("ScrapMetal")) {

            rate = 0.30;

        }
        else if (waste.equals("PaperAndCardboard")) {

            rate = 0.05;

        }
        else if (waste.equals("ElectronicWaste")) {

            rate = 0.70;

        }
        else if (waste.equals("PlasticAndGlass")) {

            rate = 0.15;

        }


        /*
         * =========================================
         * COST
         * =========================================
         */

        double cost = w * rate;

        String costString = String.format("%.2f", cost);


        /*
         * =========================================
         * STATUS
         * =========================================
         */

        String status = "FALSE";
String displayStatus;

if (status.equals("FALSE")) {
    displayStatus = "Pending";
} else {
    displayStatus = status;
}


        /*
         * =========================================
         * COMMISSION
         *
         * Commission = 5% of Cost
         * =========================================
         */

        double userCost = Double.parseDouble(costString);

        double commission = userCost * 0.05;


        /*
         * =========================================
         * COMPANY COST
         *
         * Company Cost = Cost + Commission
         * =========================================
         */

        double companyCost = userCost + commission;


        String commissionString =
                String.format("%.2f", commission);

        String companyCostString =
                String.format("%.2f", companyCost);


        /*
         * =========================================
         * DATABASE
         * =========================================
         */

        Connection con = null;
        PreparedStatement ps = null;

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "system",
                    "manager"
            );


            String sql =
                "INSERT INTO wastedetails " +
                "(request_id, email, type, weight, image, location, " +
                "extra_info, cost, status, commission, company_cost) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


            ps = con.prepareStatement(sql);


            ps.setString(1, requestId);
            ps.setString(2, emailid);
            ps.setString(3, waste);
            ps.setString(4, weight);
            ps.setString(5, imageFile);
            ps.setString(6, location);
            ps.setString(7, extraInfo);
            ps.setString(8, costString);
            ps.setString(9, status);
            ps.setString(10, commissionString);
            ps.setString(11, companyCostString);


            int result = ps.executeUpdate();


            /*
             * =========================================
             * SUCCESS
             * =========================================
             */

            if (result > 0) {

                PrintWriter out = res.getWriter();

                out.println("<!DOCTYPE html>");
                out.println("<html>");

                out.println("<head>");

                out.println("<meta charset='UTF-8'>");

                out.println(
                    "<title>EcoCycle - Submission Successful</title>"
                );

                out.println(
                    "<link rel='stylesheet' href='success.css'>"
                );

                out.println(
                    "<link rel='preconnect' href='https://fonts.googleapis.com'>"
                );

                out.println(
                    "<link rel='preconnect' " +
                    "href='https://fonts.gstatic.com' crossorigin>"
                );

                out.println(
                    "<link href='https://fonts.googleapis.com/css2?" +
                    "family=Inter:wght@400;500;600;700;800&display=swap' " +
                    "rel='stylesheet'>"
                );

                out.println("</head>");

                out.println("<body>");


                /*
                 * =========================================
                 * HEADER
                 * =========================================
                 */

                out.println("<header class='header'>");

                out.println("<div class='logo'>");

                out.println(
                    "<span class='logo-icon'>&#9851;</span>"
                );

                out.println("<span>");

                out.println(
                    "<font color='black'>Eco</font>"
                );

                out.println(
                    "<font color='green'>Cycle</font>"
                );

                out.println("</span>");

                out.println("</div>");


                out.println("<nav class='navbar'>");

                out.println("<a href='#'>Home</a>");
                out.println("<a href='#'>About</a>");
                out.println("<a href='#'>Services</a>");
                out.println("<a href='#'>Contact</a>");

                out.println("</nav>");

                out.println("</header>");


                /*
                 * =========================================
                 * SUCCESS SECTION
                 * =========================================
                 */

                out.println(
                    "<section class='success-section'>"
                );

                out.println(
                    "<div class='success-card'>"
                );


                /*
                 * SUCCESS ICON
                 */

                out.println(
                    "<div class='success-icon'>&#10003;</div>"
                );


                /*
                 * HEADING
                 */

                out.println("<h1>");

                out.println(
                    "Waste Details <span>Submitted!</span>"
                );

                out.println("</h1>");


                /*
                 * INFORMATION
                 */

                out.println("<div class='info-box'>");


                /*
                 * REQUEST ID
                 */

                out.println("<div class='info-row'>");

                out.println(
                    "<span class='info-label'>Request ID</span>"
                );

                out.println(
                    "<span class='info-value'>" +
                    requestId +
                    "</span>"
                );

                out.println("</div>");


                /*
                 * EMAIL
                 */

                out.println("<div class='info-row'>");

                out.println(
                    "<span class='info-label'>Email ID</span>"
                );

                out.println(
                    "<span class='info-value'>" +
                    emailid +
                    "</span>"
                );

                out.println("</div>");


                /*
                 * WASTE TYPE
                 */

                out.println("<div class='info-row'>");

                out.println(
                    "<span class='info-label'>Waste Type</span>"
                );

                out.println(
                    "<span class='info-value'>" +
                    waste +
                    "</span>"
                );

                out.println("</div>");


                /*
                 * WEIGHT
                 */

                out.println("<div class='info-row'>");

                out.println(
                    "<span class='info-label'>Weight</span>"
                );

                out.println(
                    "<span class='info-value'>" +
                    weight +
                    " grams</span>"
                );

                out.println("</div>");


                /*
                 * LOCATION
                 */

                out.println("<div class='info-row'>");

                out.println(
                    "<span class='info-label'>Pickup Location</span>"
                );

                out.println(
                    "<span class='info-value'>" +
                    location +
                    "</span>"
                );

                out.println("</div>");


                /*
                 * STATUS
                 */

                out.println("<div class='info-row'>");

                out.println(
                    "<span class='info-label'>Status</span>"
                );

                out.println(
    "<span class='info-value'>" +
    displayStatus +
    "</span>"
);

                out.println("</div>");

                out.println("</div>");


                /*
                 * =========================================
                 * COST
                 * =========================================
                 */

                out.println("<div class='cost-row'>");

                out.println(
                    "<span>Estimated Value</span>"
                );

                out.println(
                    "<strong>₹" +
                    costString +
                    "</strong>"
                );

                out.println("</div>");


                /*
                 * =========================================
                 * COMMISSION
                 * =========================================
                 */

                out.println("<div class='cost-row'>");

                out.println(
                    "<span>Commission (5%)</span>"
                );

                out.println(
                    "<strong>₹" +
                    commissionString +
                    "</strong>"
                );

                out.println("</div>");


                /*
                 * =========================================
                 * COMPANY COST
                 * =========================================
                 */

                out.println("<div class='cost-row'>");

                out.println(
                    "<span>Company Cost</span>"
                );

                out.println(
                    "<strong>₹" +
                    companyCostString +
                    "</strong>"
                );

                out.println("</div>");


                /*
                 * =========================================
                 * IMAGE
                 * =========================================
                 */

                out.println("<div class='image-name'>");

                out.println(
                    "Uploaded Image: <strong>"
                );

                out.println(imageFile);

                out.println("</strong>");

                out.println("</div>");


                /*
                 * =========================================
                 * BUTTON
                 * =========================================
                 */

                out.println(
                    "<a href='HomeUser.html' class='home-btn'>"
                );

                out.println("Back to Home");

                out.println("</a>");


                out.println("</div>");

                out.println("</section>");


                /*
                 * =========================================
                 * FOOTER
                 * =========================================
                 */

                out.println("<footer class='footer'>");

                out.println(
                    "© 2026 EcoWaste Management System"
                );

                out.println(
                    " | Building a Cleaner Future &#9851;"
                );

                out.println("</footer>");


                out.println("</body>");

                out.println("</html>");
            }


            /*
             * =========================================
             * INSERT FAILED
             * =========================================
             */

            else {

                PrintWriter out = res.getWriter();

                out.println("<!DOCTYPE html>");
                out.println("<html>");

                out.println("<head>");

                out.println("<meta charset='UTF-8'>");

                out.println(
                    "<title>EcoCycle - Submission Failed</title>"
                );

                out.println(
                    "<link rel='stylesheet' href='success.css'>"
                );

                out.println("</head>");

                out.println("<body>");


                out.println("<header class='header'>");

                out.println("<div class='logo'>");

                out.println(
                    "<span class='logo-icon'>&#9851;</span>"
                );

                out.println("<span>");

                out.println(
                    "<font color='black'>Eco</font>"
                );

                out.println(
                    "<font color='green'>Cycle</font>"
                );

                out.println("</span>");

                out.println("</div>");


                out.println("<nav class='navbar'>");

                out.println("<a href='#'>Home</a>");
                out.println("<a href='#'>About</a>");
                out.println("<a href='#'>Services</a>");
                out.println("<a href='#'>Contact</a>");

                out.println("</nav>");

                out.println("</header>");


                out.println(
                    "<section class='success-section'>"
                );

                out.println(
                    "<div class='success-card'>"
                );


                out.println(
                    "<div class='error-icon'>&#10007;</div>"
                );


                out.println(
                    "<h1 class='error-title'>" +
                    "Submission <span>Failed</span>" +
                    "</h1>"
                );


                out.println(
                    "<p class='error-message'>" +
                    "Sorry, your waste details could not be submitted." +
                    "<br>Please try again." +
                    "</p>"
                );


                out.println(
                    "<a href='usell.html' class='retry-btn'>" +
                    "Try Again</a>"
                );


                out.println("</div>");

                out.println("</section>");


                out.println("<footer class='footer'>");

                out.println(
                    "© 2026 EcoWaste Management System"
                );

                out.println(
                    " | Building a Cleaner Future &#9851;"
                );

                out.println("</footer>");


                out.println("</body>");

                out.println("</html>");
            }

        }


        /*
         * =========================================
         * ERROR
         * =========================================
         */

        catch (Exception e) {

            e.printStackTrace();

            res.getWriter().println(
                "<h2>Error while storing waste details.</h2>"
            );

            res.getWriter().println(
                "<p>" + e.getMessage() + "</p>"
            );
        }


        /*
         * =========================================
         * CLOSE DATABASE
         * =========================================
         */

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