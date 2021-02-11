<%-- 
    Document   : transactions
    Created on : Dec 25, 2020, 9:53:14 PM

Name : Hatem Sayed Ali Mohamed
ID   : 20170084
Group: CS_IS_1

--%>

<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import = "java.sql.*" %>
<% Class.forName("com.mysql.jdbc.Driver").newInstance(); %>
<!DOCTYPE html>
<html>
    <head>
        <style>           
            th, td { 
                padding: 5px; 
                text-align: left; 

            } 
            .ho:hover {
                background-color:#4f190f;
                color: white;
            }
            .bottom-right-border {
                border-bottom: 1px solid black;
                border-right: 1px solid black; }
            th{
                background-color: red;
                color: white
            }
            table#t01 {
                border: 1px solid black; 
                border-collapse: collapse; 
                width: 100%; 
                background-color: #f2f2d1; 
            } 
        </style> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Transactions</title>
    </head>
    <body>
        <h3>Bank Account Info</h3>

        <%
            class Transaction {

                String id;
                String from;
                String to;
                double amount;
                LocalDateTime date;

                Transaction(String id, String from, String to, double amount, LocalDateTime date) {
                    this.id = id;
                    this.from = from;
                    this.to = to;
                    this.amount = amount;
                    this.date = date;
               }
            }
            String urlDB = "jdbc:mysql://localhost:3306/bank";
            String userDB = "root";
            String passwordDB = "root";
            Connection Con = null;
            Statement Stmt = null;
            ResultSet rs = null;
            try {

                String bankID = request.getSession().getAttribute("bankID").toString();
                Con = DriverManager.getConnection(urlDB, userDB, passwordDB);
                Stmt = Con.createStatement();
                String query ="SELECT * FROM bankaccount WHERE bankaccountID = " + "'" + bankID + "';";
                rs = Stmt.executeQuery(query);
                rs.next();
                double bankAmount = Double.parseDouble(rs.getString("BACurrentBalance"));
                 query = "SELECT * FROM banktransaction WHERE (BTToAccount = '"
                        + bankID + "' OR BTFromAccount = '" + bankID + "');";
                rs = Stmt.executeQuery(query);
                ArrayList<Transaction> transactions = new ArrayList();
                while (rs.next()) {
                    Transaction transaction = new Transaction(rs.getString("BankTransactionID"),
                            rs.getString("BTFromAccount"),
                            rs.getString("BTToAccount"),
                            Double.parseDouble(rs.getString("BTAmount")),
                            rs.getTimestamp("BTCreationDate").toLocalDateTime()
                    );
                    transactions.add(transaction);
                }
        %>
        
        
                    <table id="t01" style="width:50%">
                <tr  >
                    <th bordered>Your bank ID</th>
                    <th >Bank Current Balance</th>
                </tr>
                <tr>
                    <td ><%=bankID%></td>
                    <td ><%=bankAmount%></td>
                </tr>
            </table><br>
            
            <h3>Your list of transactions</h3><br>
            <table id="t01" style="width:50%">
                <tr class = "bottom-right-border" >
                    <th >Transaction ID</th>
                    <th >Amount</th>
                    <th >From</th>
                    <th >To</th>
                    <th >Creation Date</th>
                    <!--<th>Cancel Transaction</th>-->
                </tr>
                <%for (int i = 0; i < transactions.size(); i++) {%>
                <tr class = "bottom-right-border  ho">
                    <td style="width: 30%"><%=transactions.get(i).id%></td>
                    <td ><%=transactions.get(i).amount%></td>
                    <td ><%=transactions.get(i).from%></td>
                    <td ><%=transactions.get(i).to%></td>
                    <td ><%=transactions.get(i).date%></td>

                </tr><%}if(transactions.size() == 0){%>
                <tr><td></td><td></td><td></td><td></td><td></td></tr><%}%>
            </table>

            <br><h3>Start a New Transaction</h3>
        <form action="vaidateTransaction" method="get">
            <table cellspacing="5" border="0">
                <tr>
                    <td align="right">Transfered Account :</td>
                    <td><input type="text" name="transferedToID" id="transferedToID"></td>
                </tr>
                <tr>
                    <td align="right">Amount to transfer :</td>
                    <td><input type="number" name="amountTransfered" id="amountTransfered"></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Add Transaction"></td>
                </tr>
            </table>
        </form>
        <br>
        
        <h3>Cancel a Transaction</h3>
        <form action="cancelTransaction" method="get">
            <table cellspacing="5" border="0">
                <tr>
                    <td align="right">Transaction ID: </td>
                    <td><input type="text" name="transactionID" id="transactionID"></td>
                </tr>
                <tr>
                    <td><input type = "hidden" value="<%=bankID%>" name = "fromAccountID" id="fromAccountID"></td>
                    <td><input type="submit" value="Cancel Transaction"></td>
                    
                </tr>
            </table>
        </form>
        <br>
        <%

                //session.setAttribute("bankAmount", value);
                Con.close();
                Stmt.close();
                rs.close();
            } catch (Exception e) {
                out.println(e);
            }
        %>
    </body>
</html>
