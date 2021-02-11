/*
Name : Hatem Sayed Ali Mohamed
ID   : 20170084
Group: CS_IS_1
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 *
 * @author hatem
 */
@WebServlet(urlPatterns = {"/addaccount"})
public class addaccount extends HttpServlet {

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

                Class.forName("com.mysql.jdbc.Driver");
                String urlDB = "jdbc:mysql://localhost:3306/bank";
                String userDB = "root";
                String passwordDB = "root";
                Connection Con = null;
                Statement Stmt = null;
                ResultSet rs = null;
                Con = DriverManager.getConnection(urlDB, userDB, passwordDB);
                Stmt = Con.createStatement();

                String query = "SELECT BankAccountID FROM bankaccount";
                rs = Stmt.executeQuery(query);
                ArrayList<Integer> arrayID = new ArrayList();
                while (rs.next()) {
                    arrayID.add(Integer.parseInt(rs.getString(1)));
                }
                Random rn = new Random();
                int randomID;
                do {
                    randomID = rn.nextInt(6 * (arrayID.size() + 1)) + 1;
                } while (arrayID.contains(randomID));
                out.println("below");
                String CustomerID = request.getSession().getAttribute("customerID").toString();
                out.println("hi");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String passedDate = dtf.format(now);
                out.print(passedDate + " - ");
                query = "INSERT INTO bankaccount (BankAccountID,BACreationDate, BACurrentBalance, CustomerID) "
                        + "VALUES(" + randomID + ",'" + passedDate + "', 1000," + CustomerID + ");";
                Stmt.executeUpdate(query);
                request.getSession().setAttribute("bankID", randomID);

                Con.close();
                Stmt.close();
                rs.close();
                response.sendRedirect("customerhome.jsp");

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
