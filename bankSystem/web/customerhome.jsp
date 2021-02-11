<%-- 
    Document   : customerhome
    Created on : Dec 23, 2020, 11:21:13 PM
    <!--
Name : Hatem Sayed Ali Mohamed
ID   : 20170084
Group: CS_IS_1
-->
--%>

<%@page import="java.io.PrintWriter" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import = "java.sql.*" %>
<% Class.forName("com.mysql.jdbc.Driver").newInstance(); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        <style>           
            th, td {
                padding: 5px; 
                text-align: left; 
                border: 1px solid black; 
            } 
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
    </head>
    <body>
        <%

            String urlDB = "jdbc:mysql://localhost:3306/bank";
            String userDB = "root";
            String passwordDB = "root";
            Connection Con = null;
            Statement Stmt = null;
            ResultSet rs = null;
            try {
                
                String customerID = request.getSession().getAttribute("customerID").toString();
                Con = DriverManager.getConnection(urlDB, userDB, passwordDB);
                Stmt = Con.createStatement();

                String query = "SELECT * FROM customer WHERE CustomerID = " + "'" + customerID + "';";
                rs = Stmt.executeQuery(query);
                rs.next();
                String name = rs.getString("CustomerName");
        %>
        <h2>Welcome <%=name%></h2>
        <%
            query = "SELECT * FROM BankAccount WHERE CustomerID = " + "'" + customerID + "';";
            rs = Stmt.executeQuery(query);

            if (!rs.next()) {%>
        <h4>You have no bank account!</h4>

        <form action = "addaccount" method = "get">
            <input type ="submit" value = "Add Account">

        </form>                
        <%
        } else {
            String bankID = rs.getString("BankAccountID");
            double bankAmount = Double.parseDouble(rs.getString("BACurrentBalance"));
            session.setAttribute("bankAmount", bankAmount);
            session.setAttribute("bankID", bankID);
        %>

        <h4>You have a bank account!</h4>

        <form action = "addaccount" method = "get">
            <input type ="submit" value = "Add Account" disabled>
            <br><br>

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
        </form>   
        <br><br><form action = "transactions.jsp" >
            <input type = "hidden" value="<%=bankID%>" name = "bankID" id="bankID">
            <input type ="submit" value = "View Transactions">
        </form>    
        <%}
                Con.close();
                Stmt.close();
                rs.close();
            } catch (Exception e) {
                out.println(e);
            }
        %>
    </body>
</html>
