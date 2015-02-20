package hirondelle.predict.pub.lostpassword;

import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.predict.util.Captcha;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.security.SafeText;

/**
  Model object for user entry in the lost-password form.  
*/
public final class LostPassword {

  /**
   * Full constructor. All params are required. 
   *  
   *  <P>The captcha fields must be present, and must have a valid response.
   * @param aEmail Valid email address 
   * @param aCaptchaChallenge 
   * @param aCaptchaResponse 
   * @param aIpAddress the client IP address 
   */
   public LostPassword(
    SafeText aEmail, 
    SafeText aCaptchaChallenge,
    SafeText aCaptchaResponse,
    Id aIpAddress
   ) throws ModelCtorException {
     fEmail = aEmail;
     fCaptchaChallenge = aCaptchaChallenge;
     fCaptchaResponse = aCaptchaResponse;
     fIpAddress = aIpAddress;
     validateState();
   }
   
   public SafeText getEmailAddress() { return fEmail; }  

   /** Intended for debugging only. Passwords are not emitted. */
   @Override public String toString(){
     StringBuilder builder = new StringBuilder();
     builder.append(LostPassword.class);
     builder.append(" Email: " + fEmail);
     return builder.toString();
   }

   // PRIVATE //
   private final SafeText fEmail;
   private final SafeText fCaptchaChallenge;
   private final SafeText fCaptchaResponse;
   private final Id fIpAddress;

   private void validateState() throws ModelCtorException {
     ModelCtorException ex = new ModelCtorException();
     
     if ( FAILS == Check.required(fEmail, Check.email()) ) {
       ex.add("Valid email address is required.");
     }
     if( FAILS == isCaptchaPresent() ){
       ex.add("Please type in the fuzzy characters (CAPTCHA)");
     }
     else  if(  FAILS == isCaptchaValid() ){
       ex.add("CAPTCHA invalid. Please type in the fuzzy characters (CAPTCHA) again.");
     }

     if ( ! ex.isEmpty() ) throw ex;
   }
   
   private boolean isCaptchaPresent(){
     return Check.required(fCaptchaChallenge) && Check.required(fCaptchaResponse);
   }
   
   private boolean isCaptchaValid(){
     return Captcha.isCaptchaValid(
       fIpAddress.getRawString(), fCaptchaChallenge.getRawString(), fCaptchaResponse.getRawString()
     );
   }
}
