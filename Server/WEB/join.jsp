<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<link rel="stylesheet" type="text/css" href="table.css">
<link rel="stylesheet" type="text/css" href="css.css">

<title>회원가입 조회</title>
</head>
<body>
	 <%@include file="top.jsp" %>

	 <h1 text-align="center">회원가입 인원 조회</h1>

<div class="container">	
	<%	
	Class.forName("com.mysql.jdbc.Driver");
 	String myUrl = "jdbc:mysql://localhost/jooho"; //jooho는 mysql db명이다.
	Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
	%>

	<table >
		<thead>
		<tr>
			<th>번호</th>
			<th>이름</th>
			<th>이메일</th>
			<th>번호</th>
			<th>추가</th>
		</tr>
		</thead>
		<%
			ResultSet rs = null;
		Statement stmt = null;
		try{
			String sql ="select * from board";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				String num = rs.getString("num");
				String name= rs.getString("name");
				String email = rs.getString("email");
				String title= rs.getString("title");
			
		%>
		<tr>
			<td><%=num %></td>
			<td><%=name %></td>
			<td><%=email %></td>
			<td><%=title %></td>
			<td><button class="favorite styled" type="button">회원추가</button></td>
		</tr>

		<%
			}
		}catch(SQLException ex){
			out.println("테이블 호출 실패");

		}finally{
			if(rs != null)
				rs.close();
			if(stmt != null)
				stmt.close();
			if(conn != null)
				conn.close();
		}
		%>
	</table>
</div>

</body>
