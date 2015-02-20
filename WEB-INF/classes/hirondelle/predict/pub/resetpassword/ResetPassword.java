package hirondelle.predict.pub.resetpassword;

import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.predict.util.Captcha;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.security.SafeText;

/** Model object for resetting a user's password to a new value. */
public final class ResetPassword {

  /**
   * Full constructor. All params are required. 
   *  
   *  <P>The CAPTCHA fields must be present, and must be a valid response.
   * @param aEmail Valid email address (required)
   * @param aNonce one-time nonce used to as a 'handshake' between app and the owner of an email address
   * @param aPassword Login password, 6..50 chars (required)
   * @param aPasswordConfirm must be same as password (required)
   * @param aCaptchaChallenge (required)
   * @param aCaptchaResponse (required)
   * @param aIpAddress the client IP address (required)
   */
   public ResetPassword(
    SafeText aEmail,
    SafeText aNonce,
    SafeText aPassword, 
    SafeText aPasswordConfirm, 
    SafeText aCaptchaChallenge,
    SafeText aCaptchaResponse,
    Id aIpAddress
   ) throws ModelCtorException {
     fEmail = aEmail;
     fNonce = aNonce;
     fPassword = aPassword;
     fPasswordConfirm = aPasswordConfirm;
     fCaptchaChallenge = aCaptchaChallenge;
     fCaptchaResponse = aCaptchaResponse;
     fIpAddress = aIpAddress;
     validateState();
   }
   
   public SafeText getEmail() { return fEmail; }  
   public SafeText getNonce() { return fNonce; }  
   public SafeText getPassword() { return fPassword; }  
   public SafeText getPasswordConfirm() { return fPasswordConfirm; }  

   /** Intended for debugging only. Passwords are not emitted. */
   @Override public String toString(){
     StringBuilder builder = new StringBuilder();
     builder.append(ResetPassword.class);
     builder.append(" Email : " + fEmail);
     return builder.toString();
   }
  
   @Override public boolean equals(Object aThat){
     Boolean result = ModelUtil.quickEquals(this, aThat);
     if ( result == null ) {
       ResetPassword that = (ResetPassword)aThat;
       result = ModelUtil.equalsFor(this.getSignificantFields(), that.getSignificantFields());
     }
     return result;
   }
   
   @Override public int hashCode(){
     if ( fHashCode == 0 ) {
       fHashCode = ModelUtil.hashCodeFor(getSignificantFields());
     }
     return fHashCode;
   }

   // PRIVATE //
   private final SafeText fEmail;
   private final SafeText fNonce;
   private final SafeText fPassword;
   private final SafeText fPasswordConfirm;
   private final SafeText fCaptchaChallenge;
   private final SafeText fCaptchaResponse;
   private final Id fIpAddress;
   private int fHashCode;

   private void validateState() throws ModelCtorException {
     ModelCtorException ex = new ModelCtorException();
     
     if ( FAILS == Check.required(fNonce)  ) {
       ex.add("Nonce is not present.");
     }
     if ( FAILS == Check.required(fEmail, Check.email()) ) {
       ex.add("Valid email address is required.");
     }
     if ( FAILS == Check.required(fPassword, Check.range(6, 50)) ) {
       ex.add("Password is required, minimum 6 characters.");
     }
     if ( FAILS == Check.required(fPasswordConfirm, Check.range(6, 50)) ) {
       ex.add("Password confirmation is required, must match the password supplied above.");
     }
     if( bothPasswordsPresent() && passwordsDontMatch() ){
       ex.add("Password confirmation doesn't match the original password.");      
     }
     if( FAILS == isCaptchaPresent() ){
       ex.add("Please type in the fuzzy characters (CAPTCHA)");
     }
     else  if(  FAILS == isCaptchaValid() ){
       ex.add("CAPTCHA invalid. Please type in the fuzzy characters (CAPTCHA) again.");
     }

     if ( ! ex.isEmpty() ) throw ex;
   }
   
   private Object[] getSignificantFields(){
     return new Object[] {fEmail, fNonce, fPassword, fPasswordConfirm};
   }
   
   private boolean isCaptchaPresent(){
     return Check.required(fCaptchaChallenge) && Check.required(fCaptchaResponse);
   }
   
   private boolean isCaptchaValid(){
     return Captcha.isCaptchaValid(
       fIpAddress.getRawString(), 
       fCaptchaChallenge.getRawString(), 
       fCaptchaResponse.getRawString()
     );
   }
   
   private boolean bothPasswordsPresent(){
     return Check.required(fPassword) && Check.required(fPasswordConfirm);    
   }
   
   private boolean passwordsDontMatch(){
     return ! fPassword.equals(fPasswordConfirm);
   }
}