<%@ page language="java" contentType="text/html; charset=ISO-8859-1"     pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Login & Registration</title>
		<link rel="stylesheet" href="/css/style.css"/>
		<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
	</head>
	<body>
		<div class="container">
			<div class="col">
				<div class="col">
					<h1 class="title">Welcome, <c:out value = "${usuario.getUserName()}"></c:out></h1>
					<h4>This is your dashboard. Nothing to see here yet </h4>
					
					<form action="/logout">
						<button type="submit">
							Logout
						</button>
					</form>
				</div>
			</div>
		</div>
	</body>
</html>