<%-- Delete all items related to a specific account. --%>
<c:set value='DeleteAccountAction.apply' var='baseURL'/> 

  <form action='${baseURL}' method="post" class="user-input" name='giveMeFocus'> 
   <table align="center">
    <tr>
     <td colspan='2'>Careful! Once deleted, none of your information can be recovered.</td>
    </tr>
    <tr>
     <td align="center" colspan=2>
      <input type=submit value="Delete My Account Completely">
     </td>
    </tr>
   </table>
  </form>


