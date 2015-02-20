package hirondelle.predict.util;

import hirondelle.web4j.util.Util;
import java.util.logging.Logger;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

/**
  Utility class for <a href='http://www.captcha.net/'>CAPTCHA</a> tasks.
  
  <P>CAPTCHA tools help prevent spam, by distinguishing human beings from 
  automated bots. This class should be used anytime an Action needs to 
  validate a user's input for a CAPTCHA form. 
  
  <P>This application uses the <a href='http://recaptcha.net/'>RECAPTCHA</a> service 
  to prevent spam. It also uses the recaptcha4j Java library written by Soren Davidsen.  
 */
public final class Captcha {
  
  /**
   Initialize this class with a private key value, required by the recaptcha tool.
   Each user of the recaptcha tool is given a private key, used to interact with the recaptcha server.
    
   <P>This method is called once upon startup.
   */
  public static void init(String aPrivateKey) {
    fPrivateKey = aPrivateKey;
    if( fPrivateKey.equalsIgnoreCase("NONE") || ! Util.textHasContent(fPrivateKey) ) {
      String message = 
        "You haven't set the CaptchaPrivateKey setting in web.xml. " + 
        "You must acquire your own private key. See recaptcha.net for more details."
      ;
      fLogger.severe(message);
      throw new IllegalArgumentException(message);
    }
  }

  /** 
   Return <tt>true</tt> only if the user has supplied a valid response for 
   the given challenge.
   */
  public static boolean isCaptchaValid(String aIpAddress, String aChallenge, String aResponse){
    ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
    reCaptcha.setPrivateKey(fPrivateKey);
    ReCaptchaResponse reCaptchaResponse =  reCaptcha.checkAnswer(
      aIpAddress, aChallenge, aResponse
    );
    return reCaptchaResponse.isValid();
  }
  
  // PRIVATE 
  private static String fPrivateKey;
  private static final Logger fLogger = Util.getLogger(Captcha.class);
}