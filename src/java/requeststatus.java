
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/requeststatus")
public class requeststatus extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ==========================================
    // ORACLE DATABASE
    // ==========================================

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521:xe";

    private static final String USER =
            "system";

    private static final String PASSWORD =
            "manager";


    // ==========================================
    // DO GET
    // ==========================================

    @Override
    protected void doGet(HttpServletRequest req,
                          HttpServletResponse res)
            throws ServletException, IOException {

        String requestId = req.getParameter("request_id");

        if (requestId != null &&
                !requestId.trim().isEmpty()) {

            checkSingleRequest(req, res, requestId.trim());

        } else {

            showAllRequests(req, res);
        }
    }


    // ==========================================
    // DO POST
    // ==========================================

    @Override
    protected void doPost(HttpServletRequest req,
                           HttpServletResponse res)
            throws ServletException, IOException {

        String requestId = req.getParameter("request_id");

        if (requestId == null ||
                requestId.trim().isEmpty()) {

            showAllRequests(req, res);

            return;
        }

        checkSingleRequest(req, res, requestId.trim());
    }


    // ==========================================
    // SHOW ALL REQUESTS OF LOGGED-IN USER
    // ==========================================

    private void showAllRequests(HttpServletRequest req,
                                 HttpServletResponse res)
            throws IOException {

        res.setContentType("text/html;charset=UTF-8");

        PrintWriter out = res.getWriter();


        // ==========================================
        // CHECK SESSION
        // ==========================================

        HttpSession session = req.getSession(false);

        if (session == null ||
                session.getAttribute("email") == null) {

            res.sendRedirect("User_signin.html");

            return;
        }


        String userEmail =
                (String) session.getAttribute("email");


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {

            // ==========================================
            // CONNECT TO ORACLE
            // ==========================================

            Class.forName(
                    "oracle.jdbc.driver.OracleDriver"
            );

            con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );


            // ==========================================
            // GET ALL REQUESTS OF THIS USER
            // ==========================================

            String sql =
                    "SELECT request_id, type, weight, image, " +
                    "location, cost, status " +
                    "FROM wastedetails " +
                    "WHERE email = ? " +
                    "ORDER BY request_id DESC";


            ps = con.prepareStatement(sql);

            ps.setString(1, userEmail);

            rs = ps.executeQuery();


            // ==========================================
            // HTML
            // ==========================================

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println(
                    "<meta name='viewport' " +
                    "content='width=device-width, initial-scale=1.0'>"
            );

            out.println(
                    "<title>EcoCycle - My Activities</title>"
            );


            // ==========================================
            // GOOGLE FONT
            // ==========================================

            out.println(
                    "<link rel='preconnect' " +
                    "href='https://fonts.googleapis.com'>"
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


            // ==========================================
            // CSS
            // ==========================================

            out.println("<style>");

            out.println(
                    "* {" +
                    "margin:0;" +
                    "padding:0;" +
                    "box-sizing:border-box;" +
                    "}"
            );

            out.println(
                    "body {" +
                    "font-family:'Inter',sans-serif;" +
                    "background:#f5f9f4;" +
                    "color:#183c2b;" +
                    "}"
            );


            // NAVBAR

            out.println(
                    ".navbar {" +
                    "width:100%;" +
                    "height:78px;" +
                    "background:white;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:space-between;" +
                    "padding:0 7%;" +
                    "box-shadow:0 2px 12px rgba(0,0,0,0.06);" +
                    "}"
            );


            // LOGO

            out.println(
                    ".logo {" +
                    "font-size:26px;" +
                    "font-weight:800;" +
                    "color:#111;" +
                    "}"
            );

            out.println(
                    ".logo span {" +
                    "color:#31975b;" +
                    "}"
            );


            // NAV LINKS

            out.println(
                    ".nav-links {" +
                    "display:flex;" +
                    "gap:30px;" +
                    "}"
            );

            out.println(
                    ".nav-links a {" +
                    "text-decoration:none;" +
                    "color:#555;" +
                    "font-size:14px;" +
                    "font-weight:500;" +
                    "}"
            );

            out.println(
                    ".nav-links a:hover," +
                    ".nav-links a.active {" +
                    "color:#31975b;" +
                    "}"
            );


            // MAIN

            out.println(
                    ".main {" +
                    "width:90%;" +
                    "max-width:1100px;" +
                    "margin:auto;" +
                    "padding:55px 0;" +
                    "}"
            );


            // HEADING

            out.println(
                    ".heading {" +
                    "text-align:center;" +
                    "margin-bottom:40px;" +
                    "}"
            );

            out.println(
                    ".icon {" +
                    "width:65px;" +
                    "height:65px;" +
                    "margin:0 auto 18px;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "border-radius:50%;" +
                    "background:#e7f6ec;" +
                    "color:#31975b;" +
                    "font-size:32px;" +
                    "}"
            );

            out.println(
                    ".subtitle {" +
                    "color:#31975b;" +
                    "font-size:12px;" +
                    "font-weight:700;" +
                    "letter-spacing:1.5px;" +
                    "margin-bottom:8px;" +
                    "}"
            );

            out.println(
                    ".heading h1 {" +
                    "font-size:30px;" +
                    "margin-bottom:12px;" +
                    "}"
            );

            out.println(
                    ".heading h1 span {" +
                    "color:#31975b;" +
                    "}"
            );

            out.println(
                    ".description {" +
                    "color:#777;" +
                    "font-size:14px;" +
                    "line-height:1.6;" +
                    "}"
            );


            // USER EMAIL

            out.println(
                    ".user-email {" +
                    "text-align:center;" +
                    "margin-bottom:30px;" +
                    "color:#666;" +
                    "font-size:14px;" +
                    "}"
            );


            // REQUEST TITLE

            out.println(
                    ".requests-title {" +
                    "font-size:22px;" +
                    "margin-bottom:20px;" +
                    "}"
            );


            // REQUEST CARD

            out.println(
                    ".request-card {" +
                    "background:white;" +
                    "border-radius:16px;" +
                    "margin-bottom:22px;" +
                    "padding:22px;" +
                    "display:grid;" +
                    "grid-template-columns:180px 1fr;" +
                    "gap:25px;" +
                    "box-shadow:0 8px 25px rgba(24,60,43,0.08);" +
                    "}"
            );


            // IMAGE

            out.println(
                    ".request-image {" +
                    "width:180px;" +
                    "height:160px;" +
                    "border-radius:12px;" +
                    "overflow:hidden;" +
                    "background:#e7f6ec;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "}"
            );

            out.println(
                    ".request-image img {" +
                    "width:100%;" +
                    "height:100%;" +
                    "object-fit:cover;" +
                    "}"
            );

            out.println(
                    ".no-image {" +
                    "color:#31975b;" +
                    "font-size:14px;" +
                    "text-align:center;" +
                    "}"
            );


            // REQUEST CONTENT

            out.println(
                    ".request-content h3 {" +
                    "font-size:20px;" +
                    "margin-bottom:18px;" +
                    "}"
            );


            // DETAILS

            out.println(
                    ".request-details {" +
                    "display:grid;" +
                    "grid-template-columns:1fr 1fr;" +
                    "gap:15px 25px;" +
                    "}"
            );

            out.println(
                    ".detail label {" +
                    "display:block;" +
                    "font-size:11px;" +
                    "color:#888;" +
                    "text-transform:uppercase;" +
                    "letter-spacing:.7px;" +
                    "margin-bottom:5px;" +
                    "}"
            );

            out.println(
                    ".detail p {" +
                    "font-size:14px;" +
                    "font-weight:600;" +
                    "color:#183c2b;" +
                    "word-break:break-word;" +
                    "}"
            );


            // REQUEST ID

            out.println(
                    ".request-id {" +
                    "background:#f1f8f3;" +
                    "padding:10px 12px;" +
                    "border-radius:7px;" +
                    "}"
            );


            // STATUS

            out.println(
                    ".status {" +
                    "display:inline-block;" +
                    "padding:7px 14px;" +
                    "border-radius:20px;" +
                    "font-size:12px;" +
                    "font-weight:700;" +
                    "}"
            );

            out.println(
                    ".pending {" +
                    "background:#fff4d6;" +
                    "color:#a36a00;" +
                    "}"
            );

            out.println(
                    ".accepted {" +
                    "background:#e4f7ea;" +
                    "color:#237542;" +
                    "}"
            );


            // BUTTON

            out.println(
                    ".check-btn {" +
                    "display:inline-block;" +
                    "margin-top:20px;" +
                    "padding:10px 18px;" +
                    "background:#31975b;" +
                    "color:white;" +
                    "text-decoration:none;" +
                    "border-radius:7px;" +
                    "font-size:13px;" +
                    "font-weight:600;" +
                    "}"
            );

            out.println(
                    ".check-btn:hover {" +
                    "background:#267b48;" +
                    "}"
            );


            // EMPTY

            out.println(
                    ".empty-card {" +
                    "background:white;" +
                    "padding:45px;" +
                    "border-radius:16px;" +
                    "text-align:center;" +
                    "box-shadow:0 8px 25px rgba(24,60,43,0.08);" +
                    "}"
            );

            out.println(
                    ".empty-card h3 {" +
                    "margin-bottom:10px;" +
                    "}"
            );

            out.println(
                    ".empty-card p {" +
                    "color:#777;" +
                    "font-size:14px;" +
                    "}"
            );


            // FOOTER

            out.println(
                    ".footer {" +
                    "background:#183c2b;" +
                    "color:white;" +
                    "padding:18px 7%;" +
                    "text-align:center;" +
                    "font-size:13px;" +
                    "}"
            );


            // RESPONSIVE

            out.println(
                    "@media(max-width:750px) {" +
                    ".navbar {" +
                    "flex-direction:column;" +
                    "height:auto;" +
                    "gap:18px;" +
                    "padding:20px;" +
                    "}" +

                    ".nav-links {" +
                    "flex-wrap:wrap;" +
                    "justify-content:center;" +
                    "gap:15px;" +
                    "}" +

                    ".request-card {" +
                    "grid-template-columns:1fr;" +
                    "}" +

                    ".request-image {" +
                    "width:100%;" +
                    "height:220px;" +
                    "}" +

                    ".request-details {" +
                    "grid-template-columns:1fr;" +
                    "}" +
                    "}"
            );

            out.println("</style>");

            out.println("</head>");


            // ==========================================
            // BODY
            // ==========================================

            out.println("<body>");


            // ==========================================
            // NAVBAR
            // ==========================================

            out.println("<header class='navbar'>");

            out.println(
                    "<div class='logo'>" +
                    "Eco<span>Cycle</span>" +
                    "</div>"
            );

            out.println("<nav class='nav-links'>");

            out.println(
                    "<a href='HomeUser.html'>Home</a>"
            );

            out.println(
                    "<a href='usell.html'>Sell Waste</a>"
            );

            out.println(
                    "<a href='userProducts'>Buy Products</a>"
            );

            out.println(
                    "<a href='requeststatus' class='active'>" +
                    "My Activities</a>"
            );

            out.println(
                    "<a href='#'>About Us</a>"
            );

            out.println("</nav>");

            out.println("</header>");


            // ==========================================
            // MAIN
            // ==========================================

            out.println("<main class='main'>");


            // ==========================================
            // HEADING
            // ==========================================

            out.println("<div class='heading'>");

            out.println(
                    "<div class='icon'>♻</div>"
            );

            out.println(
                    "<p class='subtitle'>ECOCYCLE</p>"
            );

            out.println(
                    "<h1>My <span>Waste Requests</span></h1>"
            );

            out.println(
                    "<p class='description'>" +
                    "View all your submitted waste requests " +
                    "and track their status." +
                    "</p>"
            );

            out.println("</div>");


            // ==========================================
            // USER EMAIL
            // ==========================================

            out.println(
                    "<div class='user-email'>" +
                    "Logged in as: <strong>" +
                    escapeHtml(userEmail) +
                    "</strong>" +
                    "</div>"
            );


            // ==========================================
            // REQUEST TITLE
            // ==========================================

            out.println(
                    "<h2 class='requests-title'>" +
                    "Your Submitted Requests</h2>"
            );


            boolean hasRequests = false;


            // ==========================================
            // DISPLAY ALL REQUESTS
            // ==========================================

            while (rs.next()) {

                hasRequests = true;


                String requestId =
                        rs.getString("request_id");

                String waste =
                        rs.getString("type");

                String weight =
                        rs.getString("weight");

                String image =
                        rs.getString("image");

                String location =
                        rs.getString("location");

                String cost =
                        rs.getString("cost");

                String status =
                        rs.getString("status");


                boolean accepted =
                        status != null &&
                        status.equalsIgnoreCase("TRUE");


                // ==========================================
                // REQUEST CARD
                // ==========================================

                out.println(
                        "<div class='request-card'>"
                );


                // ==========================================
                // IMAGE
                // ==========================================

                out.println(
                        "<div class='request-image'>"
                );

                if (image != null &&
                        !image.trim().isEmpty()) {

                    out.println(
                            "<img src='uploads/" +
                            escapeHtml(image) +
                            "' alt='Waste Image'>"
                    );

                } else {

                    out.println(
                            "<div class='no-image'>" +
                            "No photograph uploaded" +
                            "</div>"
                    );
                }

                out.println("</div>");


                // ==========================================
                // CONTENT
                // ==========================================

                out.println(
                        "<div class='request-content'>"
                );


                out.println(
                        "<h3>" +
                        escapeHtml(waste) +
                        "</h3>"
                );


                out.println(
                        "<div class='request-details'>"
                );


                // REQUEST ID

                out.println(
                        "<div class='detail'>" +
                        "<label>Request ID</label>" +
                        "<p class='request-id'>" +
                        escapeHtml(requestId) +
                        "</p>" +
                        "</div>"
                );


                // WASTE TYPE

                out.println(
                        "<div class='detail'>" +
                        "<label>Waste Type</label>" +
                        "<p>" +
                        escapeHtml(waste) +
                        "</p>" +
                        "</div>"
                );


                // WEIGHT

                out.println(
                        "<div class='detail'>" +
                        "<label>Weight</label>" +
                        "<p>" +
                        escapeHtml(weight) +
                        " grams</p>" +
                        "</div>"
                );


                // LOCATION

                out.println(
                        "<div class='detail'>" +
                        "<label>Pickup Location</label>" +
                        "<p>" +
                        escapeHtml(location) +
                        "</p>" +
                        "</div>"
                );


                // COST

                out.println(
                        "<div class='detail'>" +
                        "<label>Estimated Value</label>" +
                        "<p>₹" +
                        escapeHtml(cost) +
                        "</p>" +
                        "</div>"
                );


                // STATUS

                out.println(
                        "<div class='detail'>" +
                        "<label>Status</label>"
                );


                if (accepted) {

                    out.println(
                            "<p class='status accepted'>" +
                            "✓ ACCEPTED" +
                            "</p>"
                    );

                } else {

                    out.println(
                            "<p class='status pending'>" +
                            "⏳ PENDING" +
                            "</p>"
                    );
                }

                out.println("</div>");

                out.println("</div>");


                // ==========================================
                // CHECK STATUS BUTTON
                // ==========================================

                out.println(
                        "<a class='check-btn' " +
                        "href='requeststatus?request_id=" +
                        URLEncoder.encode(
                                requestId,
                                StandardCharsets.UTF_8
                        ) +
                        "'>" +
                        "View Full Status →" +
                        "</a>"
                );


                out.println("</div>");

                out.println("</div>");
            }


            // ==========================================
            // NO REQUESTS
            // ==========================================

            if (!hasRequests) {

                out.println(
                        "<div class='empty-card'>"
                );

                out.println(
                        "<h3>No Waste Requests Yet</h3>"
                );

                out.println(
                        "<p>" +
                        "You have not submitted any waste " +
                        "requests yet." +
                        "</p>"
                );

                out.println("</div>");
            }


            out.println("</main>");


            // ==========================================
            // FOOTER
            // ==========================================

            out.println(
                    "<footer class='footer'>" +
                    "© 2026 EcoCycle. All rights reserved. " +
                    "| Building a Cleaner Future ♻" +
                    "</footer>"
            );


            out.println("</body>");
            out.println("</html>");


        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<h2>Error while fetching your requests.</h2>"
            );

            out.println(
                    "<p>" +
                    escapeHtml(e.getMessage()) +
                    "</p>"
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


    // ==========================================
    // CHECK SINGLE REQUEST
    // ==========================================

    private void checkSingleRequest(
            HttpServletRequest req,
            HttpServletResponse res,
            String requestId)
            throws IOException {

        res.setContentType("text/html;charset=UTF-8");

        PrintWriter out = res.getWriter();


        // ==========================================
        // SESSION
        // ==========================================

        HttpSession session =
                req.getSession(false);

        if (session == null ||
                session.getAttribute("email") == null) {

            res.sendRedirect("User_signin.html");

            return;
        }


        String userEmail =
                (String) session.getAttribute("email");


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {

            Class.forName(
                    "oracle.jdbc.driver.OracleDriver"
            );

            con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );


            // ==========================================
            // CHECK REQUEST ID + USER EMAIL
            // ==========================================

            String sql =
                    "SELECT request_id, email, type, weight, " +
                    "image, location, extra_info, cost, status " +
                    "FROM wastedetails " +
                    "WHERE request_id = ? " +
                    "AND email = ?";


            ps = con.prepareStatement(sql);

            ps.setString(1, requestId);

            ps.setString(2, userEmail);

            rs = ps.executeQuery();


            if (rs.next()) {

                String email =
                        rs.getString("email");

                String waste =
                        rs.getString("type");

                String weight =
                        rs.getString("weight");

                String location =
                        rs.getString("location");

                String cost =
                        rs.getString("cost");

                String status =
                        rs.getString("status");


                boolean accepted =
                        status != null &&
                        status.equalsIgnoreCase("TRUE");


                // ==========================================
                // STATUS PAGE
                // ==========================================

                out.println("<!DOCTYPE html>");

                out.println("<html>");

                out.println("<head>");

                out.println(
                        "<meta charset='UTF-8'>"
                );

                out.println(
                        "<meta name='viewport' " +
                        "content='width=device-width, initial-scale=1.0'>"
                );

                out.println(
                        "<title>EcoCycle - Request Status</title>"
                );

                out.println(
                        "<link rel='stylesheet' " +
                        "href='success.css'>"
                );

                out.println("</head>");

                out.println("<body>");


                // HEADER

                out.println(
                        "<header class='header'>"
                );

                out.println(
                        "<div class='logo'>" +
                        "<span>Eco</span>Cycle" +
                        "</div>"
                );

                out.println(
                        "<nav class='navbar'>"
                );

                out.println(
                        "<a href='HomeUser.html'>Home</a>"
                );

                out.println(
                        "<a href='usell.html'>Sell Waste</a>"
                );

                out.println(
                        "<a href='buy-products.html'>Buy Products</a>"
                );

                out.println(
                        "<a href='requeststatus'>" +
                        "My Activities</a>"
                );

                out.println(
                        "<a href='#'>About Us</a>"
                );

                out.println("</nav>");

                out.println("</header>");


                // STATUS SECTION

                out.println(
                        "<section class='success-section'>"
                );

                out.println(
                        "<div class='success-card'>"
                );


                if (accepted) {

                    out.println(
                            "<div class='success-icon'>" +
                            "&#10003;" +
                            "</div>"
                    );

                } else {

                    out.println(
                            "<div class='error-icon'>" +
                            "&#8987;" +
                            "</div>"
                    );
                }


                out.println(
                        "<h1>Request <span>Status</span></h1>"
                );


                out.println(
                        "<div class='info-box'>"
                );


                // REQUEST ID

                out.println(
                        "<div class='info-row'>" +
                        "<span class='info-label'>" +
                        "Request ID</span>" +
                        "<span class='info-value'>" +
                        escapeHtml(requestId) +
                        "</span>" +
                        "</div>"
                );


                // EMAIL

                out.println(
                        "<div class='info-row'>" +
                        "<span class='info-label'>" +
                        "Email ID</span>" +
                        "<span class='info-value'>" +
                        escapeHtml(email) +
                        "</span>" +
                        "</div>"
                );


                // WASTE

                out.println(
                        "<div class='info-row'>" +
                        "<span class='info-label'>" +
                        "Waste Type</span>" +
                        "<span class='info-value'>" +
                        escapeHtml(waste) +
                        "</span>" +
                        "</div>"
                );


                // WEIGHT

                out.println(
                        "<div class='info-row'>" +
                        "<span class='info-label'>" +
                        "Weight</span>" +
                        "<span class='info-value'>" +
                        escapeHtml(weight) +
                        " grams</span>" +
                        "</div>"
                );


                // LOCATION

                out.println(
                        "<div class='info-row'>" +
                        "<span class='info-label'>" +
                        "Pickup Location</span>" +
                        "<span class='info-value'>" +
                        escapeHtml(location) +
                        "</span>" +
                        "</div>"
                );


                // COST

                out.println(
                        "<div class='info-row'>" +
                        "<span class='info-label'>" +
                        "Estimated Value</span>" +
                        "<span class='info-value'>₹" +
                        escapeHtml(cost) +
                        "</span>" +
                        "</div>"
                );


                // STATUS

                out.println(
                        "<div class='info-row'>" +
                        "<span class='info-label'>" +
                        "Status</span>"
                );


                if (accepted) {

                    out.println(
                            "<span class='info-value'>" +
                            "ACCEPTED</span>"
                    );

                } else {

                    out.println(
                            "<span class='info-value'>" +
                            "PENDING</span>"
                    );
                }

                out.println("</div>");

                out.println("</div>");


                // MESSAGE

                if (accepted) {

                    out.println(
                            "<p class='error-message' " +
                            "style='color:green;'>" +
                            "Your waste request has been " +
                            "accepted by the company." +
                            "</p>"
                    );

                } else {

                    out.println(
                            "<p class='error-message'>" +
                            "Your waste request is currently " +
                            "pending. Please wait for the " +
                            "company to accept it." +
                            "</p>"
                    );
                }


                // BACK BUTTON

                out.println(
                        "<a href='requeststatus' " +
                        "class='home-btn'>" +
                        "← Back to My Requests</a>"
                );


                // TRANSACTION

                out.println(
                        "<a href='usertransactionfetch2' " +
                        "class='home-btn'>" +
                        "View Transaction Details</a>"
                );


                out.println("</div>");

                out.println("</section>");

                out.println("</body>");

                out.println("</html>");


            } else {

                // ==========================================
                // REQUEST NOT FOUND
                // ==========================================

                out.println("<!DOCTYPE html>");

                out.println("<html>");

                out.println("<head>");

                out.println(
                        "<meta charset='UTF-8'>"
                );

                out.println(
                        "<title>EcoCycle - Request Not Found</title>"
                );

                out.println(
                        "<link rel='stylesheet' " +
                        "href='success.css'>"
                );

                out.println("</head>");

                out.println("<body>");

                out.println(
                        "<section class='success-section'>"
                );

                out.println(
                        "<div class='success-card'>"
                );

                out.println(
                        "<div class='error-icon'>" +
                        "&#10007;" +
                        "</div>"
                );

                out.println(
                        "<h1>" +
                        "Request <span>Not Found</span>" +
                        "</h1>"
                );

                out.println(
                        "<p class='error-message'>" +
                        "No request was found with this " +
                        "Request ID for your account." +
                        "<br><strong>" +
                        escapeHtml(requestId) +
                        "</strong>" +
                        "</p>"
                );

                out.println(
                        "<a href='requeststatus' " +
                        "class='retry-btn'>" +
                        "Back to My Requests</a>"
                );

                out.println("</div>");

                out.println("</section>");

                out.println("</body>");

                out.println("</html>");
            }


        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<h2>Error while checking request status.</h2>"
            );

            out.println(
                    "<p>" +
                    escapeHtml(e.getMessage()) +
                    "</p>"
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


    // ==========================================
    // HTML ESCAPE
    // ==========================================

    private String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}