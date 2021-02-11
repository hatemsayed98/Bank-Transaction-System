
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : Hatem Sayed Ali Mohamed
    ID   : 20170084
    Group: CS_IS_1
 */
@WebServlet(urlPatterns = {"/vaidateTransaction"})
public class vaidateTransaction extends HttpServlet {

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

                String transferedFromID = request.getSession().getAttribute("bankID").toString();
                String transferedToID = request.getParameter("transferedToID");
                double TransferedFromAmount = Double.parseDouble(request.getSession().getAttribute("bankAmount").toString());
                String amountTransferedString = request.getParameter("amountTransfered");
                if (transferedToID.isEmpty() || amountTransferedString.isEmpty() || Double.parseDouble(amountTransferedString) <= 0.0) {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Please fill in the fields!');");
                    out.println("location='transactions.jsp';");
                    out.println("</script>");
                } else {
                    if(transferedFromID.equals(transferedToID)){
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('You can not make transactions with your account!);");
                    out.println("location='transactions.jsp';");
                    out.println("</script>");
                    }
                    else{
                    double amountTransfered = Double.parseDouble(amountTransferedString);
                    if (TransferedFromAmount < amountTransfered) {
                        out.println("<script type=\"text/javascript\">");
                        out.println("alert('Your transaction amount exceeds your balance!');");
                        out.println("location='transactions.jsp';");
                        out.println("</script>");
                    } else {
                        Class.forName("com.mysql.jdbc.Driver");
                        String urlDB = "jdbc:mysql://localhost:3306/bank";
                        String userDB = "root";
                        String passwordDB = "root";
                        Connection Con;
                        Statement Stmt;
                        ResultSet rs;
                        Con = DriverManager.getConnection(urlDB, userDB, passwordDB);
                        Stmt = Con.createStatement();

                        String query;
                        query = "SELECT * FROM bankaccount WHERE BankAccountID = '" + transferedToID + "';";
                        rs = Stmt.executeQuery(query);
                        if (!rs.next()) {
                            Con.close();
                            Stmt.close();
                            rs.close();
                            out.println("<script type=\"text/javascript\">");
                            out.println("alert('Bank account ID does not exist!');");
                            out.println("location='transactions.jsp';");
                            out.println("</script>");
                        } else {
                            double TransferToAmount = Double.parseDouble(rs.getString("BACurrentBalance"));
                            TransferToAmount += amountTransfered;
                            query = "UPDATE bankaccount SET BACurrentBalance = '" + TransferToAmount + "' "
                                    + "WHERE BankAccountID = '" + transferedToID + "';";
                            Stmt.executeUpdate(query);

                            TransferedFromAmount -= amountTransfered;
                            query = "UPDATE bankaccount SET BACurrentBalance = '" + TransferedFromAmount + "' "
                                    + "WHERE BankAccountID = '" + transferedFromID + "';";
                            Stmt.executeUpdate(query);

                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            String passedDate = dtf.format(now);
                            query = "INSERT INTO banktransaction "
                                    + "(BTCreationDate, BTAmount, BTToAccount , BTFromAccount)"
                                    + " VALUES('" + passedDate + "','" + amountTransfered
                                    + "','" + transferedToID + "','" + transferedFromID + "');";
                            Stmt.executeUpdate(query);
                            Con.close();
                            Stmt.close();
                            rs.close();
                            out.println("<script type=\"text/javascript\">");
                            out.println("alert('Transaction successfully processed');");
                            out.println("location='transactions.jsp';");
                            out.println("</script>");
                        }
                    }}
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
