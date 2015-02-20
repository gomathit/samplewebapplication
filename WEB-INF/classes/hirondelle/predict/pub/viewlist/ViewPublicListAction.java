package hirondelle.predict.pub.viewlist;

import hirondelle.predict.main.lists.PredictionList;
import hirondelle.predict.main.lists.PredictionListDAO;
import hirondelle.predict.main.prediction.Prediction;
import hirondelle.predict.main.prediction.PredictionDAO;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;

import java.util.List;
import java.util.logging.Logger;

/**
  Public view of a {@link PredictionList}.
  
  <P>Search results will always include a link to this Action.
  This is the publicly available view of a prediction list.
  
   @view view.jsp
*/
public final class ViewPublicListAction  extends ActionImpl {

  /** Constructor. */
  public ViewPublicListAction(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  public static final RequestParameter LIST_ID = RequestParameter.withLengthCheck("ListId");
  public static final SqlId LIST_PREDICTIONS =  new SqlId("LIST_PREDICTIONS");

  /**
   Fetch a {@link PredictionList} and display it. Shows the title of the list, and its average score.
   See {@link Prediction#calculateAverageScore(List)}.
  */
  @Override public ResponsePage execute() throws AppException {
    ResponsePage result = getResponsePage();
    List<Prediction> predictions = fDAO.list(getIdParam(LIST_ID));
    if( ! predictions.isEmpty() ) {
      addToRequest(ITEMS_FOR_LISTING, predictions);
      SafeText title = getPredictionListTitle();
      addToRequest("title", title);
      addToRequest("averageScore", Prediction.calculateAverageScore(predictions));
      result = getResponse(title.getRawString());
    }
    else {
      //if user puts in an arbitrary list id, it will likely not exist
      addMessage("No list of predictions found.");
    }
    return result;
  }

  // PRIVATE 
  private PredictionDAO fDAO = new PredictionDAO();
  private static final Logger fLogger = Util.getLogger(ViewPublicListAction.class);
  
  private PredictionList fetchPredictionList() throws DAOException {
    PredictionListDAO dao = new PredictionListDAO();
    PredictionList result = dao.fetchPublic(getIdParam(LIST_ID));
    fLogger.fine("Parent list: " + result);
    return result;
  }
  
  private static final ResponsePage FORWARD = getResponse("List of Predictions");
  private static ResponsePage getResponse(String aTitle){
    return new ResponsePage(aTitle, "view.jsp", ViewPublicListAction.class);    
  }

  private SafeText getPredictionListTitle() throws DAOException {
    PredictionList list = fetchPredictionList();
    SafeText ownerScreenName = list.getUserScreenName();
    SafeText title = fetchPredictionList().getTitle();
    return SafeText.from(ownerScreenName.getRawString() + " - " + title.getRawString());
  }
}