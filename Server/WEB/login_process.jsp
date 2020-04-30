<%@ page pageEncoding="UTF-8"  import="java.sql.*" %>	
<%
	String id = request.getParameter("id");
	String user_psswd = request.getParameter("psswd");
	
	Connection conn = null;
	
	try{

	String url = "jdbc:mysql://localhost/MANAGER";
		
	Class.forName("com.mysql.jdbc.Driver");
	conn = DriverManager.getConnection(url, "root", "1234");
		
	Statement stmt = null;
	String sql = "SELECT * FROM manager WHERE id = " + id + "'AND psswd='" + user_psswd + "'";
	stmt.executeUpdate(sql);
//	ResultSet rs = stmt.executeQuery(sql);
	ResultSet rs = stmt.executeQuery(sql);	

	Boolean isLogin =false;
	while(rs.next()){
		isLogin =true;  // rs.next가 true면 정보가 있는것
	}

	//DB에 정보가 있으면
	if(isLogin){
		out.println("성공");
	}
	else
		out.println("실패");
	
	} catch(Exception e){
		out.println("DB연동 실패");
		}
		
		
		%>
