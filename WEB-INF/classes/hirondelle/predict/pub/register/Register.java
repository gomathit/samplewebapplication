 package hirondelle.predict.pub.register;

import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.predict.util.Captcha;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.security.SafeText;

/** Model Object for a registering user, creating a new account. */
public final class Register {

  /**
  * Full constructor. All params are required. 
  *  
  *  <P>The CAPTCHA fields must be present, and must carry a valid response.
  * @param aLoginName Name used when logging in (required)
  * @param aScreenName User's name as typically displayed on the screen (required)
  * @param aPassword Login password, 6..50 chars (required)
  * @param aPasswordConfirm must be same as password (required)
  * @param aEmail Valid email address (required)
  * @param aCaptchaChallenge (required)
  * @param aCaptchaResponse (required)
  * @param aIpAddress the client IP address (required)
  */
  public Register(
   SafeText aLoginName, 
   SafeText aScreenName, 
   SafeText aPassword, 
   SafeText aPasswordConfirm, 
   SafeText aEmail, 
   SafeText aCaptchaChallenge,
   SafeText aCaptchaResponse,
   Id aIpAddress
  ) throws ModelCtorException {
    fLoginName = aLoginName;
    fScreenName = aScreenName;
    fPassword = aPassword;
    fPasswordConfirm = aPasswordConfirm;
    fEmail = aEmail;
    fCaptchaChallenge = aCaptchaChallenge;
    fCaptchaResponse = aCaptchaResponse;
    fIpAddress = aIpAddress;
    validateState();
  }
  
  public SafeText getLoginName() { return fLoginName; }  
  public SafeText getScreenName() { return fScreenName; }  
  public SafeText getPassword() { return fPassword; }  
  public SafeText getPasswordConfirm() { return fPasswordConfirm; }  
  public SafeText getEmail() { return fEmail; }  

  /** Intended for debugging only. Passwords are not emitted. */
  @Override public String toString(){
    StringBuilder builder = new StringBuilder();
    builder.append(Register.class);
    builder.append(" Login Name: " + fLoginName +  " Email: " + fEmail);
    return builder.toString();
  }
 
  @Override public boolean equals(Object aThat){
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ) {
      Register that = (Register)aThat;
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
  private final SafeText fLoginName;
  private final SafeText fScreenName;
  private final SafeText fPassword;
  private final SafeText fPasswordConfirm;
  private final SafeText fEmail;
  private final SafeText fCaptchaChallenge;
  private final SafeText fCaptchaResponse;
  private final Id fIpAddress;
  private int fHashCode;

  private void validateState() throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    
    if ( FAILS == Check.required(fLoginName, Check.range(6, 50)) ) {
      ex.add("Login Name is required, must be at least 6 characters.");
    }
    if ( FAILS == Check.required(fScreenName, Check.range(6, 50)) ) {
      ex.add("Screen Name is required, minimum 6 characters.");
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
    if ( FAILS == Check.required(fEmail, Check.email()) ) {
      ex.add("Valid email address is required (in case you forget your password).");
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
    return new Object[] {fLoginName, fScreenName, fPassword, fPasswordConfirm, fEmail};
  }
  
  private boolean isCaptchaPresent(){
    return Check.required(fCaptchaChallenge) && Check.required(fCaptchaResponse);
  }
  
  private boolean isCaptchaValid(){
    return Captcha.isCaptchaValid(
      fIpAddress.getRawString(), fCaptchaChallenge.getRawString(), fCaptchaResponse.getRawString()
    );
  }
  
  private boolean bothPasswordsPresent(){
    return Check.required(fPassword) && Check.required(fPasswordConfirm);    
  }
  
  private boolean passwordsDontMatch(){
    return ! fPassword.equals(fPasswordConfirm);
  }
}