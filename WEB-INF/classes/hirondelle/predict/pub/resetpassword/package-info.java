/** 
 Allow the user to reset their own password, without requiring 
 them to enter the old password.
 Used when the user has forgotten their password.
 
 <P>When the user loses their password, they fill in two forms :
 <ol>
  <li>the first form allows the user to request an email. The email will include a 
   special link carrying a one-time "nonce" value.
  <li>the link in the email points to a second form, which carrys along the nonce as a hidden 
  value. This form is used to set the password to some new value, WITHOUT 
  requiring entry of the old value. 
 </ol>
 
 <P>This package handles step 2, while <tt>hirondelle.predict.pub.lostpassword</tt> 
 handles step 1.
*/ 
package hirondelle.predict.pub.resetpassword;
