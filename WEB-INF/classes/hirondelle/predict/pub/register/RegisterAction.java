package hirondelle.predict.pub.register;

import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.logging.Logger;

/**
 Allow the user to create an account on the site.
 A user can only log in after successfully creating an account.
 
  @view view.jsp
  @sql statements.sql
*/
public final class RegisterAction extends ActionTemplateShowAndApply {

  /** Constructor. */
  public RegisterAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
    createSessionAndCsrfToken();
  }
  
  public static final SqlId ADD_NEW_USER = new SqlId("ADD_NEW_USER");
  public static final SqlId ADD_NEW_USER_ROLE = new SqlId("ADD_NEW_USER_ROLE");
  
  public static final RequestParameter LOGINNAME = RequestParameter.withLengthCheck("LoginName");
  public static final RequestParameter SCREENNAME = RequestParameter.withLengthCheck("ScreenName");
  public static final RequestParameter PASSWORD = RequestParameter.withLengthCheck("Password");
  public static final RequestParameter PASSWORDCONFIRM = RequestParameter.withLengthCheck("PasswordConfirm");
  public static final RequestParameter EMAIL = RequestParameter.withLengthCheck("Email");
  public static final RequestParameter CAPTCHA_CHALLENGE = RequestParameter.withLengthCheck("recaptcha_challenge_field");
  public static final RequestParameter CAPTCHA_RESPONSE = RequestParameter.withLengthCheck("recaptcha_response_field");
  
  /** Show the empty form, with no prepopulation. */
  protected void show() throws DAOException {
    fLogger.fine("Forwarding to JSP");
  }
  
  /** Check user input can build a {@link Register} object. */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      String ipAddress = getRequestParser().getRequest().getRemoteAddr();
      fRegister = builder.build(
        Register.class, 
        LOGINNAME, SCREENNAME, PASSWORD, PASSWORDCONFIRM,
        EMAIL, CAPTCHA_CHALLENGE, CAPTCHA_RESPONSE, Id.from(ipAddress) 
      );
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** 
  Add a new user to the database.
  If the user name or email address is already taken, then the operation fails, 
  and the user is asked to try a different user name. 
 */
 protected void apply() throws AppException {
   RegisterDAO dao = new RegisterDAO();
   try {
     dao.add(fRegister);
     addMessage("Thank you! Please login with your user name and password.");
   }
   catch (DuplicateException ex){
     addError("Please try again. That user name (or email address) is already taken.");
   }
 }
  
  // PRIVATE //
  private Register fRegister;
  private static final ResponsePage FORWARD = new ResponsePage("Register", "view.jsp", RegisterAction.class);
  private static final ResponsePage REDIRECT = new ResponsePage("../../main/lists/PredictionListAction.do?Operation=List");
  private static final Logger fLogger = Util.getLogger(RegisterAction.class);
}