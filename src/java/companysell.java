import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/companysell")
public class companysell extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html;charset=UTF-8");

        PrintWriter pw = res.getWriter();

        // Get data from HTML form
        String email = req.getParameter("email");
        String type = req.getParameter("type");
        String name = req.getParameter("name");
        String quantity = req.getParameter("quantity");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");

        Connection conn = null;
        PreparedStatement ps = null;

        try {

            // Convert quantity and cost
            int qty = Integer.parseInt(quantity);
            double price = Double.parseDouble(cost);

            // Calculate total cost
            double total = qty * price;

            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to Oracle database
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "system",
                    "manager"
            );

            // SQL query
            String sql =
                    "INSERT INTO companysell "
                    + "(email, material_category, product_name, quantity, "
                    + "price_per_unit, total_cost, description) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, type);
            ps.setString(3, name);
            ps.setInt(4, qty);
            ps.setDouble(5, price);
            ps.setDouble(6, total);
            ps.setString(7, description);

            // Execute query
            int result = ps.executeUpdate();

            if (result > 0) {

                pw.println("<html>");
                pw.println("<head>");
                pw.println("<title>Success</title>");

                pw.println("<style>");

                pw.println("body {");
                pw.println("font-family: Arial, sans-serif;");
                pw.println("background: #f5f9f4;");
                pw.println("color: #183c2b;");
                pw.println("text-align: center;");
                pw.println("padding-top: 100px;");
                pw.println("}");

                pw.println(".box {");
                pw.println("background: white;");
                pw.println("width: 500px;");
                pw.println("margin: auto;");
                pw.println("padding: 40px;");
                pw.println("border-radius: 20px;");
                pw.println("box-shadow: 0 10px 30px rgba(24,60,43,0.12);");
                pw.println("}");

                pw.println("h1 {");
                pw.println("color: #28784b;");
                pw.println("}");

                pw.println(".amount {");
                pw.println("font-size: 24px;");
                pw.println("font-weight: bold;");
                pw.println("color: #237544;");
                pw.println("}");

                pw.println("a {");
                pw.println("display: inline-block;");
                pw.println("margin-top: 20px;");
                pw.println("padding: 12px 25px;");
                pw.println("background: #28784b;");
                pw.println("color: white;");
                pw.println("text-decoration: none;");
                pw.println("border-radius: 8px;");
                pw.println("}");

                pw.println("</style>");

                pw.println("</head>");
                pw.println("<body>");

                pw.println("<div class='box'>");

                pw.println(
                        "<h1>Product Added Successfully!</h1>"
                );

                pw.println(
                        "<p>Your product has been successfully stored.</p>"
                );

                pw.println(
                        "<p>Product: <b>" +
                        name +
                        "</b></p>"
                );

                pw.println(
                        "<p>Quantity: <b>" +
                        qty +
                        "</b></p>"
                );

                pw.println(
                        "<p>Price Per Unit: <b>&#8377;" +
                        String.format("%.2f", price) +
                        "</b></p>"
                );

                pw.println("<p>Total Cost:</p>");

                pw.println(
                        "<div class='amount'>&#8377;" +
                        String.format("%.2f", total) +
                        "</div>"
                );

                pw.println(
                        "<a href='companysell.html'>" +
                        "Sell Another Product" +
                        "</a>"
                );

                pw.println("</div>");

                pw.println("</body>");
                pw.println("</html>");
            }

        } catch (NumberFormatException e) {

            pw.println("<html>");
            pw.println("<body>");

            pw.println("<h2>Invalid Input</h2>");
            pw.println(
                    "<p>Please enter valid quantity and price.</p>"
            );

            pw.println("</body>");
            pw.println("</html>");

        } catch (ClassNotFoundException e) {

            pw.println("<html>");
            pw.println("<body>");

            pw.println("<h2>Oracle JDBC Driver Not Found</h2>");
            pw.println(
                    "<p>Please add the Oracle JDBC driver (ojdbc) " +
                    "to your NetBeans project.</p>"
            );

            pw.println("</body>");
            pw.println("</html>");

            e.printStackTrace();

        } catch (SQLException e) {

            pw.println("<html>");
            pw.println("<body>");

            pw.println("<h2>Database Error</h2>");
            pw.println(
                    "<p>" +
                    e.getMessage() +
                    "</p>"
            );

            pw.println("</body>");
            pw.println("</html>");

            e.printStackTrace();

        } finally {

            try {

                if (ps != null) {
                    ps.close();
                }

                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            pw.close();
        }
    }
}