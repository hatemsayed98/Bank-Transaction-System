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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author hatem
 */
@WebServlet(name = "cancelTransaction", urlPatterns = {"/cancelTransaction"})
public class cancelTransaction extends HttpServlet {

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
                String transactionID = request.getParameter("transactionID");
                String fromAccountID = request.getParameter("fromAccountID");
                if (transactionID.isEmpty() || fromAccountID.isEmpty()) {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Please fill in the fields!');");
                    out.println("location='transactions.jsp';");
                    out.println("</script>");
                } else {
                    String urlDB = "jdbc:mysql://localhost:3306/bank";
                    String userDB = "root";
                    String passwordDB = "root";
                    Connection Con;
                    Statement Stmt;
                    ResultSet rs;
                    Con = DriverManager.getConnection(urlDB, userDB, passwordDB);
                    Stmt = Con.createStatement();

                    String query;
                    query = "SELECT * FROM banktransaction WHERE BankTransactionID = '" + transactionID + "';";
                    rs = Stmt.executeQuery(query);
                    if (!rs.next()) {
                        Con.close();
                        Stmt.close();
                        rs.close();
                        out.println("<script type=\"text/javascript\">");
                        out.println("alert('Transaction ID is wrong!');");
                        out.println("location='transactions.jsp';");
                        out.println("</script>");
                    } else {
                        String BTTOAccount = rs.getString("BTTOAccount");
                        String BTFromAccount = rs.getString("BTFromAccount");
                        if (BTTOAccount.equals(fromAccountID) || !fromAccountID.equals(BTFromAccount)) {
                            Con.close();
                            Stmt.close();
                            rs.close();
                            out.println("<script type=\"text/javascript\">");
                            out.println("alert('You can only cancel transactions you made!');");
                            out.println("location='transactions.jsp';");
                            out.println("</script>");
                        } else {
                            LocalDateTime dateOfTransaction = rs.getTimestamp("BTCreationDate").toLocalDateTime();
                            LocalDateTime now = LocalDateTime.now();
                            double daysBetween = ChronoUnit.SECONDS.between(now, dateOfTransaction);
                            daysBetween = daysBetween / (3600 * 24);
                            if (Math.abs(daysBetween) >= 1.0) {
                                Con.close();
                                Stmt.close();
                                rs.close();
                                out.println("<script type=\"text/javascript\">");
                                out.println("alert('You can only cancel transactions less than one day!');");
                                out.println("location='transactions.jsp';");
                                out.println("</script>");
                            } else {

                                double amountToBeCanceled = Double.parseDouble(rs.getString("BTAmount"));

                                query = "UPDATE bankaccount SET BACurrentBalance = BACurrentBalance - '" + amountToBeCanceled
                                        + "' WHERE BankAccountID = '" + BTTOAccount + "';";
                                Stmt.executeUpdate(query);
                                query = "UPDATE bankaccount SET BACurrentBalance = BACurrentBalance + '" + amountToBeCanceled
                                        + "' WHERE BankAccountID = '" + fromAccountID + "';";
                                Stmt.executeUpdate(query);
                                query = " DELETE FROM banktransaction WHERE BankTransactionID = '" + transactionID + "';";
                                Stmt.executeUpdate(query);

                                Con.close();
                                Stmt.close();
                                rs.close();
                                out.println("<script type=\"text/javascript\">");
                                out.println("alert('Transaction Successfully Canceled!');");
                                out.println("location='transactions.jsp';");
                                out.println("</script>");
                            }
                        }
                    }
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
