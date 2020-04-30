<%@ page contentType="text/html; charset=euc-kr" errorPage="DBError.jsp" %>
<%@ page import="java.sql.*"%>
<%
    String userId = request.getParameter("userId");
    
    Connection conn = null;
    Statement stmt = null;
    
    try{
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost/root", "root", "1234");
        if (conn == null)
            throw new Exception("데이터베이스에 연결할 수 없습니다.");
        stmt = conn.createStatement();
				      // the mysql insert statement
 String query = " insert into board (pass, name, email, title, content, readcount)"
 + " values (?, ?, ?, ?, ?, ?)";
// create the mysql insert preparedstatement
 PreparedStatement preparedStmt = conn.prepareStatement(query);
 preparedStmt.setString (1, "3648");
 preparedStmt.setString (2, userId);
 preparedStmt.setString(3, "obj@nate.com");
preparedStmt.setString(4, "Love");
preparedStmt.setString(5, "I LOVE YOU");
 preparedStmt.setInt (6, 0);

 preparedStmt.execute();
 conn.close();
				      /*int rowNum = stmt.executeUpdate(command);
        if (rowNum < 1)
            throw new Exception("데이터를 DB에 입력할 수 없습니다.");
    }
    finally{
        try{
            stmt.close();
        }
        catch(Exception ignored){
        }
        try{
            conn.close();
        }
        catch(Exception ignored){
        }
    }
    response.sendRedirect("DBInputResult.jsp");
    */
%>    

