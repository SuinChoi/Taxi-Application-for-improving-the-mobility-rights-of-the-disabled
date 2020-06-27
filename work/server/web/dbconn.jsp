    <%@ page import="java.sql.*" %>
	<%
	
	Connection conn = null;
	
		String url = "jdbc:mysql://localhos/Users";
		String user = "root";
		String password ="1234";
		
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url, user, password);
		
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM Join";
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()){
			out.println(rs.getString(2) + ", " + rs.getString(3) +"<br/>");
		}
		rs.close();
		stmt.close();
	%>
