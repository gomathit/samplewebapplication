package hirondelle.predict.pub.search;

import hirondelle.web4j.action.ActionTemplateSearch;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelFromRequest;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Util;

import java.util.List;
import java.util.logging.Logger;

/**
 Search for predictions using a text match.
 
  @view view.jsp
  @sql statements.sql
*/
public final class SearchAction extends ActionTemplateSearch {
  
  public static final RequestParameter SEARCH_TEXT = RequestParameter.withLengthCheck("Search Text");
  public static final RequestParameter SEARCH_STYLE = RequestParameter.withLengthCheck("SearchStyle");
  public static final RequestParameter START_DATE = RequestParameter.withLengthCheck("Start Date");
  public static final RequestParameter END_DATE = RequestParameter.withLengthCheck("End Date");

  public static final SqlId SEARCH_FOR_EXACT_PHRASE = new SqlId("SEARCH_FOR_EXACT_PHRASE");
  public static final SqlId SEARCH_FOR_ALL_OF_THESE_WORDS = new SqlId("SEARCH_FOR_ALL_OF_THESE_WORDS");
  
  /** Constructor. */
  public SearchAction(RequestParser aRequestParser){
    super(FORWARD, aRequestParser);
  }
  
  /** Ensure user input can construct a {@link SearchCriteria} object. */
  @Override protected void validateUserInput() throws AppException {
    try {
      ModelFromRequest builder = new ModelFromRequest(getRequestParser());
      fSearchCriteria = builder.build(SearchCriteria.class, SEARCH_TEXT, SEARCH_STYLE, START_DATE, END_DATE);
      fLogger.fine("Search Criteria: " + fSearchCriteria);
    }
    catch (ModelCtorException ex){
      addError(ex);
    }    
  }

  /** List the {@link SearchResult}s that correspond to the given criteria. */
  @Override protected void listSearchResults() throws AppException {
    SearchDAO dao = new SearchDAO();
    List<SearchResult> matchingPredictions = dao.listSearchResults(fSearchCriteria);
    addToRequest(ITEMS_FOR_LISTING, matchingPredictions);
    if(matchingPredictions.isEmpty()){
      addMessage("No Results. Please try again");
    }
  }
  
  // PRIVATE 
  private SearchCriteria fSearchCriteria;
  private static final ResponsePage FORWARD = new ResponsePage("Search All Predictions", "view.jsp", SearchAction.class);
  private static final Logger fLogger = Util.getLogger(SearchAction.class);
}
