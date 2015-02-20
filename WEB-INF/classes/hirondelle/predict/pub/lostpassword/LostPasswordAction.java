package hirondelle.predict.pub.lostpassword;

import static hirondelle.web4j.util.Consts.NEW_LINE;
import hirondelle.predict.pub.register.PasswordHasher;
import hirondelle.web4j.BuildImpl;
import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;
import hirondelle.web4j.util.WebUtil;
import hirondelle.web4j.webmaster.Emailer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 First step in recovering a lost password. 

 <P>The second step is implemented by 
 {@link hirondelle.predict.pub.resetpassword.ResetPasswordAction}. 

 <P>The user inputs their email address into a form. Then, this action will send an email to the given email address. 
 The owner of that email address will receive an email containing a special link, which will take them to a 
 <i>second</i> form.
 
 <P>The special link will contain a one-off "nonce" value, whose value will populate a hidden variable in the 
 second form. This nonce value amounts to a handshake between the server and the owner of the given email 
 address. The nonce value is attached to the user account having the given email address, and is valid only for a
 limited time.  
 
 @view view.jsp
 @sql statements.sql
 */
public final class LostPasswordAction  extends ActionTemplateShowAndApply  {

  /** Constructor. */
  public LostPasswordAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
    createSessionAndCsrfToken();
  }
  
  public static final SqlId SET_TEMP_PASSWORD_NONCE = new SqlId("SET_TEMP_PASSWORD_NONCE");
  public static final SqlId FETCH_PARTIAL_USER = new SqlId("FETCH_PARTIAL_USER");
  
  public static final RequestParameter EMAIL = RequestParameter.withLengthCheck("Email");
  public static final RequestParameter CAPTCHA_CHALLENGE = RequestParameter.withLengthCheck("recaptcha_challenge_field");
  public static final RequestParameter CAPTCHA_RESPONSE = RequestParameter.withLengthCheck("recaptcha_response_field");
  
  /** Show an empty form, with no prepopulation. */
  protected void show() throws DAOException {
    //do nothing - just show the FORWARD PAGE
  }
  
  /** Check that user input can build a {@link LostPassword} object. */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      String ipAddress = getRequestParser().getRequest().getRemoteAddr();
      fLostPassword = builder.build(
        LostPassword.class, 
        EMAIL, CAPTCHA_CHALLENGE, CAPTCHA_RESPONSE, Id.from(ipAddress) 
      );
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /**
  Send an email to the given email address, containing a link allowing the user to reset their password. 
  <P>If the given email address is not in the database, then show an error message. 
  */
  protected void apply() throws AppException {
    LostPasswordDAO dao = new LostPasswordDAO();
    PartialUser user = dao.fetchUser(fLostPassword.getEmailAddress().getRawString());
    if(user == null){
      addError("That email address doesn't belong to any registered user. Please check the email address.");
    }
    else {
      fLogger.fine("Sending email for password reset. Sending to : "  + user.getEmailAddress());
      String nonce = buildNonceFor(user);
      //technically should be done in a tx, but the likelihood of error is nearly zero.
      dao.setNewNonce(user, nonce);
      
      Emailer emailer = BuildImpl.forEmailer();
      List<String> toEndUser = new ArrayList<String>();
      toEndUser.add(user.getEmailAddress());
      emailer.sendFromWebmaster(toEndUser, buildEmailSubject(user), buildEmailBody(user, nonce));
      addMessage("An email has been sent to that email address. The email contains a special link to let you reset your password.");
    }
  }

  // PRIVATE //
  private LostPassword fLostPassword;
  private static final ResponsePage FORWARD = new ResponsePage("Lost Password", "view.jsp", LostPasswordAction.class);
  private static final ResponsePage REDIRECT = new ResponsePage("LostPasswordAction.show");
  private static final Logger fLogger = Util.getLogger(LostPasswordAction.class);
  private static final String SUBJECT = "Reset Password Requested";
  
  /** 
   The nonce MUST be difficult to guess. 
   <P>It is calculated using the email address, and the current time, combined with a salt.
   It's a bit long. Some might choose to chop it down to fewer characters.
   */
  private String buildNonceFor(PartialUser aUser){
    Date now = new Date();
    String source = aUser.getEmailAddress() + now.toString(); 
    return PasswordHasher.hash(source);
  }
  
  /** Return the subject of the email. */
  private String buildEmailSubject(PartialUser aUser){
    return SUBJECT;
  }
  
  /** Return the full content of the email. */
  private String buildEmailBody(PartialUser aUser, String aNonce){
    StringBuilder result = new StringBuilder();
    result.append("Please click on the following temporary link to reset your password.");
    result.append(NEW_LINE);
    result.append(buildLinkForPasswordReset(aUser.getEmailAddress(), aNonce));
    return result.toString();
  }
  
  /** Return a special link containing a nonce value, required for password reset. */
  private String buildLinkForPasswordReset(String aEmailAddress, String aNonce){
    String origURL = WebUtil.getOriginalRequestURL(getRequestParser().getRequest(), getRequestParser().getResponse());
    //bit of hard-coding here; retain only the part before 'pub'
    String PUB = "pub";
    int startOfMain = origURL.indexOf(PUB);
    String rootURL = origURL.substring(0, startOfMain); // Example value:  'http://localhost:8081/predict/'
    
    String newURL = rootURL  + "pub/resetpassword/ResetPasswordAction.show";
    String result = WebUtil.setQueryParam(newURL, "Email", aEmailAddress);
    result = WebUtil.setQueryParam(result, "Nonce", aNonce);
    return result.toString();
  }
}