package hirondelle.predict.main.deleteaccount;

import hirondelle.predict.main.lists.PredictionList;
import hirondelle.predict.main.lists.PredictionListDAO;
import hirondelle.predict.main.logoff.LogoffAction;
import hirondelle.web4j.action.Action;
import hirondelle.web4j.action.ActionTemplateShowAndApply;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.List;
import java.util.logging.Logger;


/**
  Delete the user's account, and all related items.
  Deletes all items related to the current user's account. The data cannot be recovered. 
  
 @sql statements.sql
 @view view.jsp
*/
public class DeleteAccountAction  extends ActionTemplateShowAndApply {

  /** Constructor. */
  public DeleteAccountAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT, aRequestParser);
  }
  
  public static final SqlId DELETE_PREDICTIONS = new SqlId("DELETE_PREDICTIONS");
  public static final SqlId DELETE_LISTS = new SqlId("DELETE_LISTS");
  public static final SqlId DELETE_ROLES = new SqlId("DELETE_ROLES");
  public static final SqlId DELETE_USER = new SqlId("DELETE_USER");
  
  /** Unusual : no request parameters are defined here. */
  
  /** Show the form used for initiating the delete operation, with no prepopulation. */
  protected void show() throws DAOException {
    //do nothing - just show the FORWARD PAGE
  }

  /** Do nothing - there is no user input in this case. */
  protected void validateUserInput() {
    //do nothing - no user input
  }
  
  /** Delete all items related to the account, log off, and redirect to the home page.  */
  protected void apply() throws AppException {
    fLogger.fine("Deleting account of current user. Data will not be recoverable. User will not be able to log back in.");
    PredictionListDAO listDAO = new PredictionListDAO();
    List<PredictionList> predictionLists = listDAO.list(getUserId());
    
    DeleteAccountDAO dao = new DeleteAccountDAO();
    int numRecords = dao.delete(getLoggedInUserName(), getUserId(), predictionLists);
    fLogger.fine("Account deleted for user login name: " + getLoggedInUserName().getRawString() + ". This many rows deleted:" + numRecords);
    
    //interesting - log off by calling another action directly
    //BUT cannot use the response page of the other action
    Action logoff = new LogoffAction(getRequestParser());
    logoff.execute();
  }

  // PRIVATE 
  private static final ResponsePage FORWARD = new ResponsePage("Delete Your Account", "view.jsp", DeleteAccountAction.class);
  private static final ResponsePage REDIRECT = new ResponsePage("../../Home.jsp");
  private static final Logger fLogger = Util.getLogger(DeleteAccountAction.class);
}
