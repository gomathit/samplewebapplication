<%-- View the predictions in a public Prediction List. --%>
<P>&nbsp;

<P>
<c:if test="${not empty itemsForListing}">
<table class="report" title="Prediction Listing" align="center"> 
 <tr>
  <th title="Line Number">#</th>
  <th>Prediction</th>
  <th>Outcome</th>
 </tr>
<w:alternatingRow> 
<c:forEach var="item" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight" >
  <td title="Line Number">${index.count}</td>
  <td>${item.text} <i>${item.remark}</i></td>
  <td nowrap>${item.outcome}</td>
 </tr>
</c:forEach>
<c:if test='${not empty averageScore}'>
 <tr class="row_highlight">
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>${averageScore}% Right</td>
 </tr>
</c:if>
</w:alternatingRow>
</table>
</c:if>

