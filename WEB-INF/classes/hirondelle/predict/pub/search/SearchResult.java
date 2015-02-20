package hirondelle.predict.pub.search;

import hirondelle.predict.main.codes.CodeTable;
import hirondelle.web4j.model.Code;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.security.SafeText;

/** 
 Simple data carrier for search results. Does no validation.
*/
public final class SearchResult {

  /**
   Constructor.
   
    @param aText the text of the prediction (required)
    @param aOutcome the outcome of the prediction, if any (optional)
    @param aCreationDate the date the prediction was made (required)
    @param aCreatorScreenName the screen name of the user who made the prediction
    @param aPredictionListId id of the prediction list that contains the prediction (required)  
  */
  public SearchResult(
    SafeText aText, Id aOutcome, DateTime aCreationDate, 
    SafeText aCreatorScreenName, Id aPredictionListId
  ) throws ModelCtorException {
    fText = aText;
    fOutcome = CodeTable.codeFor(aOutcome, CodeTable.OUTCOMES);
    fCreationDate = aCreationDate;
    fCreatorScreenName = aCreatorScreenName;
    fPredictionListId = aPredictionListId;
    //validation seems redundant here - no user input - would only check db integrity - skip it
  }

  public SafeText getText() {   return fText;  }
  public Code getOutcome() {   return fOutcome;  }
  public DateTime getCreationDate() {    return fCreationDate;  }
  public SafeText getCreatorScreenName() {   return fCreatorScreenName;  }
  public Id getPredictionListId() {   return fPredictionListId;  }

  /** Intended for debugging only. */
  @Override public String toString(){
    return ModelUtil.toStringFor(this);
  }
 
  @Override public boolean equals(Object aThat){
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ) {
      SearchResult that = (SearchResult)aThat;
      result = ModelUtil.equalsFor(this.getSignificantFields(), that.getSignificantFields());
    }
    return result;
  }
  
  @Override public int hashCode(){
    if ( fHashCode == 0 ) {
      fHashCode = ModelUtil.hashCodeFor(getSignificantFields());
    }
    return fHashCode;
  }
  
  // PRIVATE 
  private SafeText fText;
  private Code fOutcome;
  private DateTime fCreationDate;
  private SafeText fCreatorScreenName;
  private Id fPredictionListId;
  private int fHashCode;
  
  private Object[] getSignificantFields(){
    return new Object[] {fText, fCreationDate, fCreatorScreenName, fOutcome};
  }
}
