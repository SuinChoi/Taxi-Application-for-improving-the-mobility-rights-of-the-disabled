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

         <h1 text-align="center">고객관리</h1>
	 <div class = "search">
	 	<input type=text placeholder="ID검색" height="50px" />
		<button style="background-color:white">검색</button>
	 </div>
<div class="container">
        <%
        Class.forName("com.mysql.jdbc.Driver");
        String myUrl = "jdbc:mysql://localhost/MANAGER"; //jooho는 mysql db명이다.
        Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
        %>

        <table >
                <thead>
                <tr>
                        <th>ID</th>
                        <th>이름</th>
                        <th>전화번호</th>
			<th>상세정보</th>
                </tr>
                </thead>
                <%
                        ResultSet rs = null;
                Statement stmt = null;
                try{
                        String sql ="select * from manager";
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(sql);

                        while(rs.next()){
                                String id = rs.getString("id");
                                String name= rs.getString("name");
                                String mobile = rs.getString("mobile");

                %>
                <tr>
                        <td><%=id %></td>
                        <td><%=name %></td>
                        <td><%=mobile %></td>
			<td><button class="favorite styled" type="button">상세정보</button></td>
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

