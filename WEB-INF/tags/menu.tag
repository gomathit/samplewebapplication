   <%@ include file="/WEB-INF/TagHeader.jspf" %>
    <c:url var='homePageURL' value='/Home.jsp' />
    <li><a href='${homePageURL}' title='Home page'>Home</a></li>
    <c:url var='searchURL' value='/pub/search/SearchAction.show' />
    <li><a href='${searchURL}' title='Search all predictions'>Search</a></li>
    <c:url var='loginURL' value='/main/lists/PredictionListAction.list' />
    <li><a href='${loginURL}' title='Login and add predictions'>Login</a></li>
    
  <w:show ifRole="user">
    <c:url var='listsURL' value='/main/lists/PredictionListAction.list' />
    <li><a href='${listsURL}' title='All of my lists of predictions'>Lists</a></li>
    <c:url value="/main/logoff/LogoffAction.apply" var="logoffURL"/>
    <li><form action='${logoffURL}' method='POST' class='log-off'> <input type='submit' value='Log Out' title='Log Out'></form>
    <c:url var='deleteAccountURL' value='/main/deleteaccount/DeleteAccountAction.show' />
    <li><a href='${deleteAccountURL}' title='Delete my account completely'>Delete</a></li>
  </w:show>
  <li><a href='mailto:support@web4j.com'>Contact</a>
    