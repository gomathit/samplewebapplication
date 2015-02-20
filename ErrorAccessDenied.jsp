<%-- Access Denied error page for the application. Configured in web.xml. --%>
<%@page session='false' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=${web4j_key_for_character_encoding}">
 <title>
  ${web4j_key_for_app_info.name}</title>
  <c:url value="/stylesheet10.css" var="cssURL"/>
  <link rel="stylesheet" type="text/css" href='${cssURL}' media="all">
</head>

<body>
<div class="body">

<p align="center">
  <b>Access Denied.</b>
  <br>
  You are not authorized to view that page.
</p> 
</div>
<P>

</body>
</html>
