<%-- Login Page. Configured in web.xml. --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
 <tags:headsimple title='Home'/>
 
<body onload='showFocus()'>

<tags:headerAndMenu/> 

<h2>Log In To Your Account</h2>
<tags:displayMessages/>

<div class="body">
<p align="center" >
 <form method="POST" action='<%= response.encodeURL("j_security_check") %>' class="user-input" name='giveMeFocus'>
 <table align="center">
 <c:if test='${not empty param["Retry"]}'>
  <tr>
   <td colspan='2' align='center'><b>Please try again.</b></td>
  </tr>
  <tr>
   <td>&nbsp;</td>
  </tr>
 </c:if>
 <tr>
  <td><label>Name</label></td>
  <td><input type="text" name="j_username" size='30'></td>
 </tr>
 <tr>
  <td><label>Password (or phrase)</label></td>
  <%-- 'autocomplete' is a non-HTML attribute, supported by some browsers. Prevents prepopulation of passwords.--%>
  <td><input type="password" name="j_password" autocomplete="false" size='30'></td>
 </tr>
 <tr align="center">
  <td colspan="2"><input type="submit" value="Login"></td>
 </tr>
 </table>
 </form>
 
 <P>
<table align="center">
  <tr>
  <td colspan='2'>
    <c:url var='registerURL' value='/pub/register/RegisterAction.show' />
    <a href='${registerURL}' title='Let me create an account'>Not Registered Yet?</a></li>
  </td>
 </tr>
 <tr>
  <td colspan='2'>
    <c:url var='lostPassURL' value='/pub/lostpassword/LostPasswordAction.show' />
    <a href='${lostPassURL}' title='Send me an email to let me reset my password'>Lost Your Password?</a></li>
  </td>
 </tr>
 
 </table>
 
</div>

<P>
</body>
</html>

