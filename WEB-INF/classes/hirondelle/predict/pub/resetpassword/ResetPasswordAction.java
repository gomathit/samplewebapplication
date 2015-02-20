package hirondelle.predict.pub.resetpassword;

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

/**
  Reset the user's password to a new value.
  
  @view view.jsp
  @sql statements.sql  
*/
public final class ResetPasswordAction  extends ActionTemplateShowAndApply  {

  /** Constructor. */
  public ResetPasswordAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
    createSessionAndCsrfToken();
  }
  
  public static final SqlId RESET_LOST_PASSWORD = new SqlId("RESET_LOST_PASSWORD");
  
  public static final RequestParameter EMAIL = RequestParameter.withLengthCheck("Email");
  public static final RequestParameter NONCE = RequestParameter.withLengthCheck("Nonce");
  public static final RequestParameter PASSWORD = RequestParameter.withLengthCheck("Password");
  public static final RequestParameter PASSWORD_CONFIRM = RequestParameter.withLengthCheck("PasswordConfirm");
  public static final RequestParameter CAPTCHA_CHALLENGE = RequestParameter.withLengthCheck("recaptcha_challenge_field");
  public static final RequestParameter CAPTCHA_RESPONSE = RequestParameter.withLengthCheck("recaptcha_response_field");
  
  /** Show the empty form, with no prepopulation. */
  protected void show() throws DAOException {
    //do nothing - just show the FORWARD PAGE
  }

  /** Check user input can build a {@link ResetPassword} object. */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      String ipAddress = getRequestParser().getRequest().getRemoteAddr();
      fResetPassword = builder.build(
        ResetPassword.class, 
        EMAIL, NONCE, PASSWORD, PASSWORD_CONFIRM, CAPTCHA_CHALLENGE, CAPTCHA_RESPONSE, Id.from(ipAddress) 
      );
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }

  /**
  Reset the user's password.
   
  <P>This operation succeeds only if the nonce is present, it matches the nonce 
  value attached to the user account, and is 
  no more than 15 minutes old.
  */
  protected void apply() throws AppException {
    ResetPasswordDAO dao = new ResetPasswordDAO();
    int numRecords = dao.resetPassword(fResetPassword);
    if ( numRecords == 0 ) {
      addError("Please try again. Password reset is available only for 15 minutes after the email is sent.");
    }
    else {
      addMessage("Please log in with your new password.");
    }
  }

  // PRIVATE //
  private ResetPassword fResetPassword;
  private static final ResponsePage FORWARD = new ResponsePage("Reset Password", "view.jsp", ResetPasswordAction.class);
  private static final ResponsePage REDIRECT = new ResponsePage("../../main/lists/PredictionListAction.do?Operation=List");
}
