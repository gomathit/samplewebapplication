package hirondelle.predict.main.lists;

import hirondelle.web4j.action.ActionTemplateListAndEdit;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.ForeignKeyException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.List;
import java.util.logging.Logger;

/**
 List and edit {@link PredictionList} objects.
 
 @sql statements.sql
 @view view.jsp
*/
public final class PredictionListAction extends ActionTemplateListAndEdit {

  /** Constructor.  */
  public PredictionListAction(RequestParser aRequestParser){
    super(FORWARD, REDIRECT_TO_LISTING, aRequestParser);
    fUserId = getUserId(); 
  }
  
  public static final SqlId PREDICTION_LIST_LIST =  new SqlId("PREDICTION_LIST_LIST");
  public static final SqlId PREDICTION_LIST_FETCH =  new SqlId("PREDICTION_LIST_FETCH");
  public static final SqlId PREDICTION_LIST_FETCH_PUBLIC =  new SqlId("PREDICTION_LIST_FETCH_PUBLIC");
  public static final SqlId PREDICTION_LIST_ADD =  new SqlId("PREDICTION_LIST_ADD");
  public static final SqlId PREDICTION_LIST_CHANGE =  new SqlId("PREDICTION_LIST_CHANGE");
  public static final SqlId PREDICTION_LIST_DELETE =  new SqlId("PREDICTION_LIST_DELETE");
  
  public static final RequestParameter ID = RequestParameter.withLengthCheck("Id");
  public static final RequestParameter TITLE = RequestParameter.withLengthCheck("Title");

  /** Show all lists owned by the current user. */
  protected void doList() throws DAOException {
    List<PredictionList> predictionLists = fDAO.list(fUserId);
    addToRequest(ITEMS_FOR_LISTING, predictionLists);
    if( predictionLists.isEmpty() ){
      addMessage("Please add your first list of predictions.");
    }
  }
  
  /** Try to build a {@link PredictionList} from user input. */
  @Override protected void validateUserInput() {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fPredictionList = builder.build(PredictionList.class, ID, TITLE, fUserId);
      fLogger.fine(fPredictionList.toString());
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }
  
  /** Try to add a new {@link PredictionList}. */
  protected void attemptAdd() throws DAOException {
    try {
      fDAO.add( fPredictionList, DateTime.today(getTimeZone()) );
      addMessage("Your new list was added successfully.");
    }
    catch(DuplicateException ex){
      addError("Cannot add duplicate PredictionList.");  
    }
  }
  
  /** Fetch an existing {@link PredictionList}, in preparation for editing it. */
  protected void attemptFetchForChange() throws DAOException {
    PredictionList predictionList = fDAO.fetch(getIdParam(ID), fUserId);
    if( predictionList == null ){
      addError("Item no longer exists. Likely deleted by another user.");
    }
    else {
      addToRequest(ITEM_FOR_EDIT, predictionList);
    }
  }

  /** Try to change an existing {@link PredictionList}. */
  protected void attemptChange() throws DAOException {
    try {
      boolean success = fDAO.change(fPredictionList);
      if (success){
        addMessage("Your list has been changed successfully.");
      }
      else {
        addError("No update occurred. Item likely deleted by another user.");
      }
    }
    catch(DuplicateException ex){
      addError("Duplicate Prediction List. Please use another identifier.");  
    }
  }
  
  /** Try to delete an existing {@link PredictionList}. */
  protected void attemptDelete() throws DAOException {
    try {
      fDAO.delete(getIdParam(ID), fUserId);
      addMessage("Your list has been deleted.");
    }
    catch (ForeignKeyException ex){
      //Some might prefer to delete all related items at once, either using a cascading delete, 
      //or in a transaction
      addError("Cannot delete this list. You must first remove its predictions, one by one.");      
    }
  }
  
  // PRIVATE
  private PredictionList fPredictionList;
  private PredictionListDAO fDAO = new PredictionListDAO();
  private Id fUserId;
  private static final ResponsePage FORWARD = new ResponsePage("Your Lists", "view.jsp", PredictionListAction.class);
  private static final ResponsePage REDIRECT_TO_LISTING = new ResponsePage("PredictionListAction.do?Operation=List");
  private static final Logger fLogger = Util.getLogger(PredictionListAction.class);
}