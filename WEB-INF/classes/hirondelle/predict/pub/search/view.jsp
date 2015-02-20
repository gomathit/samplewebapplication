<%-- Search all predictions for given text. --%>
<c:url value="SearchAction.search" var="searchURL"/> 
<form action='${searchURL}' method="GET" class="user-input"  name='giveMeFocus'>
 <w:populate> 
 <table align="center">
  <tr>
   <td> <label>Search For</label> </td>
   <td><input name="Search Text" type="text" size='68' title='2..255 characters'></td>
  </tr>
  <tr>
   <td> <label>Using</label> </td>
   <td>
     <input name="SearchStyle" type="radio" value='AllOfTheWords' checked> all of the words
     <input name="SearchStyle" type="radio" value='ExactPhrase'> exact phrase
   </td>
  </tr>
   <tr>
    <td><label>Created</label></td>
    <td>
      between <input name="Start Date" type="text" size='10' title='Created on or after this date YYYYMMDD'> and 
      <input name="End Date" type='text' size='10' title='Created on or before this date YYYYMMDD'> YYYYMMDD
    </td>
   </tr>
  <tr>
   <td align="center" colspan=2>
      <input type='submit' value="Search">
   </td>
  </tr>
 </table>
 </w:populate> 
</form>

<P>
<c:if test="${not empty itemsForListing}">
<table class="report" title="Matching Predictions" align="center"> 
 <tr>
  <th title="Line Number">#</th>
  <th>Text</th>
  <th>Outcome</th>
  <th>Created</th>
  <th>Author</th>
  <th>View List</th>
 </tr>
<w:alternatingRow> 
<c:forEach var="item" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <td title="Line Number">${index.count}</td>
  <td>${item.text}</td>
  <td nowrap>${item.outcome}</td>
  <td nowrap>
   <c:set value="${item.creationDate}" var="creationDate"/>
   <w:showDateTime name="creationDate" pattern='MMMM DD, YYYY'/>
  </td>
  <td nowrap>${item.creatorScreenName}</td>
  <td>
    <c:url var='link' value='../viewlist/ViewPublicListAction.do' >
       <c:param name="ListId" value="${item.predictionListId}" />
    </c:url>
    <a href='${link}' title='View Prediction List'>link</a>
  </td>
 </tr>
</c:forEach>
</w:alternatingRow>
</table>
</c:if>

