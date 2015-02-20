<%-- Reset the user password, without requiring the old password. --%>
<c:url value="ResetPasswordAction.apply" var="baseURL"/> 
<form action='${baseURL}' method="POST" class="user-input"  name='giveMeFocus'>
 <w:populate> 
 <table align="center">
  <input name="Nonce" type="hidden"></td>
  <tr>
   <td> <label>Email</label> </td>
   <td><input name="Email" type="text"></td>
  </tr>
  <tr>
   <td><label>Password</label></td>
   <td><input name="Password" type="password"></td>
  </tr>
  <tr>
   <td><label>Confirm Password</label></td>
   <td><input name="PasswordConfirm" type="password"></td>
  </tr>
  <tr>
   <td><label>Captcha</label></td>
   <td>
     <tags:captcha/>
   </td>
  </tr>
  <tr>
   <td align="center" colspan=2>
      <input type='submit' value="Reset Password">
   </td>
  </tr>
 </table>
 </w:populate> 
</form>
