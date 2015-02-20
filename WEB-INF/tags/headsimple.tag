<%@ include file="/WEB-INF/TagHeader.jspf" %>
<%@ attribute name="title" required="true" rtexprvalue="true" %>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <meta name="keywords" content="predictions">
 <meta name="description" content="Simple tool for tracking your predictions over time.">
 <title>All Your Predictions -> ${title}</title>
 <c:url var='stylesheetURL' value='/css/stylesheet10.css' />
 <c:url var='shortcutIconURL' value='/images/favicon.ico' />
 <link rel="stylesheet" type="text/css" href='${stylesheetURL}'  media="all"> 
 <link rel="shortcut icon" href='${shortcutIconURL}' type="image/vnd.microsoft.icon">
 <tags:showFocus/>
</head>
