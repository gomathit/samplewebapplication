package hirondelle.predict.main.prediction;

import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.FetchIdentifierOwner;

/**
 List and edit {@link Prediction} objects.
 
 <P>This feature is a prime example of implementing 
 <a href='http://www.web4j.com/UserGuide.jsp#DataOwnershipConstraints'>data ownership constraints</a>.
 Here, each Prediction is linked to a PredictionList. Each PredictionList is in turned linked to a 
 specific user - the <i>owner</i> of the PredictionList, and all of the Predictions it contains. 
 
 @view view.jsp
 @sql statements.sql
*/
public final class PredictionAction extends ActionTemplateListAndEdit implements FetchIdentifierOwner  {

  /** Constructor.  */
  public PredictionAction(RequestParser aRequestParser){
    super(FORWARD, dynamicRedirect(aRequestParser), aRequestParser);
  }
  
  public static final SqlId LIST_PREDICTIONS =  new SqlId("LIST_PREDICTIONS");
  public static final SqlId FETCH_PREDICTION =  new SqlId("FETCH_PREDICTION");
  public static final SqlId ADD_PREDICTION =  new SqlId("ADD_PREDICTION");
  public static final SqlId CHANGE_PREDICTION =  new SqlId("CHANGE_PREDICTION");
  public static final SqlId DELETE_PREDICTION =  new SqlId("DELETE_PREDICTION");
  public static final SqlId FETCH_OWNER =  new SqlId("FETCH_OWNER");

  public static final RequestParameter PREDICTION_LIST_ID = RequestParameter.withLengthCheck("ParentId");
  public static final RequestParameter ID = RequestParameter.withLengthCheck("Id");
  public static final RequestParameter TEXT = RequestParameter.withLengthCheck("Text");
  public static final RequestParameter CREATIONDATE = RequestParameter.withLengthCheck("CreationDate");
  public static final RequestParameter REMARK = RequestParameter.withLengthCheck("Remark");
  public static final RequestParameter OUTCOME = RequestParameter.withLengthCheck("Outcome");
  public static final RequestParameter OUTCOME_DATE = RequestParameter.withLengthCheck("OutcomeDate");

  /** 
   Helps enforce an ownership constraint. 
   Only the owner of a PredictionList can perform edits on its content. 
 */ 
  public Id fetchOwner() throws DAOException {
    return fDAO.fetchLoginNameFor(getIdParam(PREDICTION_LIST_ID));
  }
  
  /**  Show the list of {@link Prediction}s in a given prediction list.  */
  protected void doList() throws DAOException {
    addToRequest(ITEMS_FOR_LISTING, fDAO.list(getListId()));
  }

  /** Attempt to build a {@link Prediction} out of user input. */
  protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fPrediction = builder.build(Prediction.class, ID, PREDICTION_LIST_ID, TEXT, REMARK, OUTCOME);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** Attempt to add a new {@link Prediction}. */
  protected void attemptAdd() throws DAOException {   
    try {
      fDAO.add(fPrediction, getListId(), DateTime.now(getTimeZone()));
      addMessage("Prediction added successfully.");
    }
    catch(DuplicateException ex){
      addError("Cannot add duplicate Prediction.");  
    }
  }
  
  /** Fetch an existing {@link Prediction} in preparation for editing it. */
   protected void attemptFetchForChange() throws DAOException {
    Prediction prediction = fDAO.fetch(getIdParam(ID), getListId());
    if( prediction == null ){
      addError("Item no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, prediction);
    }
  }
  
   /** Attempt to change an existing {@link Prediction}. */
   protected void attemptChange() throws DAOException {
    try {
      boolean success = fDAO.change(fPrediction, getListId(), DateTime.today(getTimeZone()));
      if (success){
        addMessage("Prediction changed successfully.");
      }
      else {
        addError("No update occurred. Item likely deleted by another user.");
      }
    }
    catch(DuplicateException ex){
      addError("Duplicate Prediction. Please use another identifier.");  
    }
  }

  /** Attempt to delete an existing {@link Prediction}. */
  protected void attemptDelete() throws DAOException {
    try {
      fDAO.delete(getIdParam(ID), getListId());
      addMessage("Prediction deleted successfully.");
    }
    catch (DAOException ex){
      addError("Cannot delete Prediction. Item likely used elsewhere.");      
    }
  }
  
  // PRIVATE 
  private Prediction fPrediction;
  private PredictionDAO fDAO = new PredictionDAO();
  private static final ResponsePage FORWARD = new ResponsePage("Prediction", "view.jsp", PredictionAction.class);
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("PredictionAction.do?Operation=List");

  private Id getListId(){
    return getIdParam(PREDICTION_LIST_ID);
  }

  private static ResponsePage dynamicRedirect(RequestParser aRequestParser){
    String value = aRequestParser.getRawParamValue(PREDICTION_LIST_ID);
    return REDIRECT_TO_LISTING.appendQueryParam("ParentId", value);
  }
}