<%@ include file="/WEB-INF/TagHeader.jspf" %>
 <%-- TTitle is a template parameter. --%>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <meta name="keywords" content="predictions">
 <meta name="description" content="Simple tool for tracking your predictions over time.">
 <title>${web4j_key_for_app_info.name} - <w:txt>${param.TTitle}</w:txt> </title>
 <c:url var='stylesheetURL' value='/css/stylesheet10.css' />
 <c:url var='shortcutIconURL' value='images/favicon.ico' />
 <link rel="stylesheet" type="text/css" href='${stylesheetURL}'  media="all"> 
 <link rel="shortcut icon" href='${shortcutIconURL}' type="image/vnd.microsoft.icon">
 <tags:showFocus/>
</head>