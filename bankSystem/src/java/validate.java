
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.http.HttpSession;

/**
Name : Hatem Sayed Ali Mohamed
ID   : 20170084
Group: CS_IS_1
 **/
@WebServlet(urlPatterns = {"/validate"})
public class validate extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                String customerID = request.getParameter("id");
                String password = request.getParameter("password");

                if (customerID.isEmpty() || password.isEmpty()) {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Please enter your ID and Password!');");
                    out.println("location='index.html';");
                    out.println("</script>");
                }
                else{
                Class.forName("com.mysql.jdbc.Driver");
                String urlDB = "jdbc:mysql://localhost:3306/bank";
                String userDB = "root";
                String passwordDB = "root";
                Connection Con = null;
                Statement Stmt = null;
                ResultSet rs = null;
                Con = DriverManager.getConnection(urlDB, userDB, passwordDB);
                Stmt = Con.createStatement();

                String query;
                query = "SELECT * FROM Customer WHERE CustomerID = " + "'" + customerID + "'" + " AND CustomerPassword = " + "'" + password + "';";
                rs = Stmt.executeQuery(query);
                if (!rs.next()) {
                    Con.close();
                    Stmt.close();
                    rs.close();
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('ID or Password incorrect!');");
                    out.println("location='index.html';");
                    out.println("</script>");
                } else {
                    String name = rs.getString("CustomerName");
                    String address = rs.getString("CustomerAddress");
                    int mobile = Integer.parseInt(rs.getString("CustomerMobile"));

                    HttpSession session = request.getSession(true);
                    session.setAttribute("customerID", customerID);
                    response.sendRedirect("customerhome.jsp");
                }
                Con.close();
                Stmt.close();
                rs.close();
                }
            } catch (Exception e) {
                out.print(e);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
