<%-- User starts the procedure for gaining control of their password. --%>
<c:url value="LostPasswordAction.apply" var="baseURL"/> 
<form action='${baseURL}' method="post" class="user-input"  name='giveMeFocus'>
 <w:populate> 
 <table align="center">
  <tr>
   <td><label>Email</label></td>
   <td><input name="Email" type="text"></td>
  </tr>
  <tr>
   <td><label>Captcha</label></td>
   <td>
     <tags:captcha/>
   </td>
  </tr>
  <tr>
   <td align="center" colspan=2>
      <input type='submit' value="Reset My Password">
   </td>
  </tr>
 </table>
 </w:populate> 
</form>
