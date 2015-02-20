package hirondelle.predict.pub.search;

import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.database.DynamicSql;
import static hirondelle.web4j.database.DynamicSql.WHERE;
import static hirondelle.web4j.database.DynamicSql.AND;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.util.Util;
import static hirondelle.web4j.util.Consts.NEW_LINE;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/** 
 Data Access Object (DAO) for returning {@link SearchResult}s for the given 
 {@link SearchCriteria}.
 
 <P>The input parameters from the user are date-only. For the purposes of this SELECT, those 
 dates are coerced into date-times, to match with the underlying data type of the column.
 
 <P>For example, the from-date is coerced to the start-of-day, as in: 
 <PRE>2010-01-15 00:00:00.000000000</PRE>

 while the to-date is coerced to the end-of-day, as in:
 <PRE>2010-01-15 23:59:59.999999999</PRE>
*/
final class SearchDAO {
 
  /**
   Return search results.
   <P>This method uses {@link DynamicSql} to construct the SQL SELECT statement. 
   The number of items returned is limited to 200. The search is not sensitive to case.
   Items are sorted by creation date, with the newest appearing first. 
  */
  List<SearchResult> listSearchResults(SearchCriteria aSearchCriteria) throws DAOException {
    List<SearchResult> result = new ArrayList<SearchResult>();
    if( SearchStyle.ExactPhrase == aSearchCriteria.getSearchStyle() ){
      result = Db.search(
        SearchResult.class, 
        SearchAction.SEARCH_FOR_EXACT_PHRASE, 
        getCriteriaExactPhrase(aSearchCriteria), 
        getParamsExactPhrase(aSearchCriteria)
      );
    }
    else if ( SearchStyle.AllOfTheWords == aSearchCriteria.getSearchStyle() ){
      String[] searchTerms = getSearchTerms(aSearchCriteria);
      result = Db.search(
        SearchResult.class, 
        SearchAction.SEARCH_FOR_ALL_OF_THESE_WORDS, 
        getCriteriaAllWords(aSearchCriteria, searchTerms), 
        getParamsAllWords(aSearchCriteria, searchTerms)
      );
    }
    else {
      throw new AssertionError("Unexpected SearchStyle: " + aSearchCriteria);
    }
    return result;
  }
  
  // PRIVATE 
  
  private static final String TEXT_LIKE = "Prediction.Text LIKE ?" + NEW_LINE;
  private static final String AND_FROM_DATE = "AND Prediction.CreationDate >= ?" + NEW_LINE;
  private static final String AND_TO_DATE = "AND Prediction.CreationDate <= ?" + NEW_LINE;
  private static final String ORDER_BY_DATE_DESC = "ORDER BY Prediction.CreationDate DESC LIMIT 200" + NEW_LINE;
  
  private static final Logger fLogger = Util.getLogger(SearchDAO.class);
  
  /** Return the criteria for the case of searching for an exact phrase. */
  private DynamicSql getCriteriaExactPhrase(SearchCriteria aSearchCriteria) {
    StringBuilder result = new StringBuilder(WHERE + TEXT_LIKE);
    if(aSearchCriteria.getFromDate() != null){
      result.append(AND_FROM_DATE);
    }
    if(aSearchCriteria.getToDate() != null){
      result.append(AND_TO_DATE);
    }
    result.append(ORDER_BY_DATE_DESC);
    fLogger.fine("Dynamic SQL criteria: " + result);
    return new DynamicSql(result);
  }
  
  private Object[] getParamsExactPhrase(SearchCriteria aSearchCriteria){
    List<Object> result = new ArrayList<Object>();
    result.add(like(aSearchCriteria.getSearchText().getRawString()));
    if(aSearchCriteria.getFromDate() != null){
      result.add(aSearchCriteria.getFromDate());
    }
    if(aSearchCriteria.getToDate() != null){
      result.add(aSearchCriteria.getToDate());
    }
    return result.toArray();
  }
  
  /** Return the criteria for the case of searching for all words. */
  private DynamicSql getCriteriaAllWords(SearchCriteria aSearchCriteria, String[] aSearchTerms) {
    /*
     This is artificial; since the base SQL is static, it should be defined in an .sql file, in the usual way.
     This is done here merely to demonstrate that the ENTIRE sql statement can be constructed in code, if desired.
     An important point: when you do need to create such sql entirely in code, you need to be aware of the dangers
     of sql injection attacks. Always use '?' for all data input by the end user.
    */
    String BASE_SQL =   
      "SELECT Prediction.Text, Prediction.OutcomeFK, Prediction.CreationDate, Users.ScreenName, PredictionList.Id " +
      "FROM  Prediction " +  
      "LEFT JOIN PredictionList ON PredictionListFK = PredictionList.Id " + 
      "LEFT JOIN Users ON PredictionList.UserFK = Users.Id"
    ;
    //now add the WHERE clause and ORDER BY clause
    StringBuilder sql = new StringBuilder(BASE_SQL + NEW_LINE + WHERE);
    int idx = 0;
    for (String searchTerm: aSearchTerms) {
      if(idx > 0){
        sql.append(AND);
      }
      sql.append(TEXT_LIKE);
      ++idx;
    }
    if(aSearchCriteria.getFromDate() != null){
      sql.append(AND_FROM_DATE);
    }
    if(aSearchCriteria.getToDate() != null){
      sql.append(AND_TO_DATE);
    }
    sql.append(ORDER_BY_DATE_DESC);
    fLogger.fine("Dynamic SQL criteria: " + sql);
    DynamicSql criteria = new DynamicSql(sql);
    return criteria;
  }
  
  /** Search terms are separated by white space. */
  private String[] getSearchTerms(SearchCriteria aSearchCriteria){
    String WHITESPACE = "\\s";
    String searchTerms = aSearchCriteria.getSearchText().getRawString();
    return searchTerms.split(WHITESPACE);
  }
  
  private Object[] getParamsAllWords(SearchCriteria aSearchCriteria, String[] aSearchTerms){
    List<Object> result = new ArrayList<Object>();
    //common words are left in here; they are not removed
    for(String searchTerm: aSearchTerms){
      result.add(like(searchTerm));
    }
    if(aSearchCriteria.getFromDate() != null){
      result.add(aSearchCriteria.getFromDate());
    }
    if(aSearchCriteria.getToDate() != null){
      result.add(aSearchCriteria.getToDate());
    }
    return result.toArray();
  }
  
  /** Surround by '%', for LIKE operator. */
  private Id like(String aSearchTerm){
    return Id.from("%" + aSearchTerm + "%"); 
  }
}
