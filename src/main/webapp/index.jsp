<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>仪器管理-LIMS</title>
<!-- 引入css样式文件 -->
<!-- Bootstrap Core CSS -->
</head>
<body>
	<button type="button" 
		onclick="createApparatus()">创建仪器</button>
<script type="text/javascript" src="http://localhost:8080/lr/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="http://localhost:8080/lr/jquery.min.js"></script>
	<!-- 编写js代码 -->
	<script type="text/javascript">
	// 上新仪器
	function createApparatus() {
		$.ajax({  
	          type : "get",  
	          url : "http://localhost:8080/lr/EmployeeLogDayManagement/getEmployeeLogDayList",  
	          data : {"employeeId" : 11,
					"pageNum" : 1},  
	          async : false,  
	          success : function(msg){  
	        	  console.log(data);
					var msg = JSON.stringify(data);
					console.log("返回的参数：" + msg.success);
					//将json字符串转换为json对象
					var obj = eval('(' + msg + ')');
					console.log("djld:" + obj.success);
	          }  
	     }); 
		};
	</script>
</body>
</html>