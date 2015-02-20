<%@ page contentType="text/html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
 <tags:head/>
 <body onload='showFocus()'>
 <tags:headerAndMenu/> 
 <h2><w:txt>${param.TTitle}</w:txt></h2>
<tags:displayMessages/>


<div class="body">
 <c:if test="${not empty param.TBody}">
   <jsp:include page='${param.TBody}' flush="true"/>
 </c:if>

 <c:if test="${empty param.TBody}"> 
  <jsp:include page="Error.html" flush="true"/>
 </c:if>
</div>

 <tags:footer/>

</body>
</html>