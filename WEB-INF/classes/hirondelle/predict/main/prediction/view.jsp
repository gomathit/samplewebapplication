<%-- List-and-edit JSP for Predictions --%>
<c:set value='PredictionAction' var='baseURL'/> 
<tags:setFormTarget using='${baseURL}' />

<%-- Form. --%>
<form action='${formTarget}' method="post" class="user-input" name='giveMeFocus'> 
  <w:populate using="itemForEdit"> 
   <input name="Id" type="hidden">
   <input name="ParentId" type="hidden">
   <table align="center">
     <tr>
       <td><label alt='Required, max 255 characters'>Prediction</label></td>
       <td>
         <textarea name='Text' alt='Max 255 characters' rows='3'  cols='40'></textarea>
       </td>
      </tr>
      <tr>
       <td><label>Remark</label></td>
       <td><textarea name='Remark' rows='3'  cols='40'></textarea></td>
      </tr>
      <tr>
       <td><label>Outcome</label></td>
       <td>
         <select name='Outcome'> 
           <option> </option> 
           <c:forEach var='item' items='${outcome}'>
             <option value='${item.id}'>${item}</option>
           </c:forEach>
         </select>
        </td>
      </tr>
    <tr>
     <td align="center" colspan=2>
      <input type='submit' value="Add/Edit">
     </td>
    </tr>
   </table>
  </w:populate>
</form>

<P>
<c:if test="${not empty itemsForListing}">
<%-- Listing. --%>
<table class="report" title="Prediction Listing" align="center"> 
 <tr>
  <th title="Line Number">#</th>
  <th>Prediction/Remark</th>
  <th>Created</th>
  <th>Outcome</th>
  <th>Score(%)</th>
  <th>Outcome Date</th>
 </tr>
<w:alternatingRow> 
<c:forEach var="item" items="${itemsForListing}" varStatus="index">
 <tr class="row_highlight">
  <td title="Line Number">${index.count}</td>
  <td>${item.text} <i>${item.remark}</i></td>
  <td>
   <c:set value="${item.creationDate}" var="creationDate"/>
   <w:showDateTime name="creationDate" pattern='MMM DD YYYY'/>
  </td>
  <td nowrap>${item.outcome}</td>
  <td>${item.outcomeScore}</td>
  <td>
   <c:set value="${item.outcomeDate}" var="outcomeDate" />
   <w:showDateTime name="outcomeDate" pattern='MMM DD YYYY' />
  </td>
  <tags:editLinksFineGrained2 baseURL='${baseURL}' id='${item.id}' parentid='${item.parentId}' />
 </tr>
</c:forEach>
</w:alternatingRow>
</table>
</c:if>