package hirondelle.predict.main.prediction;

import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.predict.main.codes.CodeTable;
import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.Code;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import hirondelle.web4j.security.SafeText;

import java.util.List;

/** 
 Model Object for a Prediction. 
 
 <P>This class is interesting since it does not include the foreign key to the PredictionList table; 
  that is taken only from the session, not from the database, nor from user input.  
  <P>This object is immutable, and makes defensive copies where needed.
*/
public final class Prediction {

  /**
   Full constructor. Used when fetching from the database.
    
   @param aId internal database identifier (optional)
   @param aParentId internal database identifier (required)
   @param aText main text of the prediction max 255 characters (required)
   @param aCreationDate date-time the prediction was created (required)
   @param aRemark any remark the user may wish to make (justification, special remark on the outcome, etc.)  max 2000 chars (optional)
   @param aOutcome final outcome of the prediction (optional)
   @param aOutcomeDate Date the final outcome was established, if ever (optional); if outcome is specified, then the
   outcome date must also be specified 
  */
  public Prediction(
   Id aId, Id aParentId,  SafeText aText,  DateTime aCreationDate, 
   SafeText aRemark,  Id aOutcome,  DateTime aOutcomeDate
  ) throws ModelCtorException {
    fId = aId;
    fParentId = aParentId;
    fText = aText;
    fCreationDate = aCreationDate;
    fRemark = aRemark;
    fOutcome = CodeTable.codeFor(aOutcome, CodeTable.OUTCOMES);
    fOutcomeDate = aOutcomeDate;
    validateState(CheckDates.YES);
  }
  
  /**
   Partial constructor, used for user input. 
   
   <P>Similar to the full constructor, but the user never inputs the creation date, or the outcome date.
   Here, those dates are simply set to <tt>null</tt>.
   */
  public Prediction(
    Id aId,  Id aParentId, SafeText aText, SafeText aRemark,  Id aOutcome
   ) throws ModelCtorException {
     fId = aId;
     fParentId = aParentId;
     fText = aText;
     fRemark = aRemark;
     fOutcome = CodeTable.codeFor(aOutcome, CodeTable.OUTCOMES);
     fCreationDate = null;
     fOutcomeDate = null;
     validateState(CheckDates.NO);
 }
  
  public Id getId() { return fId; }  
  public Id getParentId() { return fParentId; }  
  public SafeText getText() { return fText; }  
  public DateTime getCreationDate() { return fCreationDate; } 
  public SafeText getRemark() { return fRemark; }  
  public Code getOutcome() { return fOutcome; }
  
  /**
    Returns <tt>null</tt> if there is no outcome.
    Otherwise, returns the score attached to the outcome (see {@link CodeTable#OUTCOMES}).  
   */
  public Integer getOutcomeScore() {
    Integer result = null;
    if (fOutcome != null) {
      //Undecidable items will have the short text as null
      SafeText value = fOutcome.getShortText();
      if( value != null ) {
        result = Integer.valueOf(value.getRawString());
      }
    }
    return result;
  }
  
  public DateTime getOutcomeDate() {
    return fOutcomeDate; 
  }
  
  /**
   Calculate the average of {@link #getOutcomeScore()} for all of the predictions in a list.
   
    <P>Predictions which have a <tt>null</tt> outcome do not contribute to the result.
    If <i>none</i> of the predictions in the list have an outcome, then <tt>null</tt> is returned.
    Integer division is used to calculate the result, so there will be some rounding.  
  */
  public static Integer calculateAverageScore(List<Prediction> aPredictions){
    Integer result = null;
    int sum = 0;
    int numScores = 0;
    for(Prediction prediction : aPredictions){
      if(prediction.getOutcomeScore() != null){
        ++numScores;
        sum = sum + prediction.getOutcomeScore();
      }
    }
    if ( numScores > 0 ) {
      result = sum / numScores ; //integer division is close enough
    }
    return result;
  }
  
  @Override public String toString(){
    return ModelUtil.toStringFor(this);
  }
  
  @Override public boolean equals(Object aThat){
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ) {
      Prediction that = (Prediction)aThat;
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
  private final Id fId;
  private final Id fParentId;
  private final SafeText fText;
  private final DateTime fCreationDate;
  private final SafeText fRemark;
  private final Code fOutcome;
  private final DateTime fOutcomeDate;
  private int fHashCode;
  
  private enum CheckDates{YES, NO};
  
  private void validateState(CheckDates aCheckDates) throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    
    if ( FAILS == Check.required(fParentId) ) {
      ex.add("Parent Id is required. Programmer error.");
    }
    if ( FAILS == Check.required(fText, Check.range(1, 255)) ) {
      ex.add("You must input text for your prediction (maximum 255 characters)");
    }
    if ( FAILS == Check.optional(fRemark, Check.range(1, 2000)) ) {
      ex.add("Remark must have visible text, maximum 2,000 chars.");
    }
    
    if(CheckDates.YES == aCheckDates){
      if ( FAILS == Check.required(fCreationDate) ) {
        ex.add("Creation Date is missing (programmer error)");
      }
      if( fOutcome != null && fOutcomeDate == null) {
        ex.add("Outcome and Outcome Date must appear together.");
      }
      if( fOutcome == null && fOutcomeDate != null) {
        ex.add("Outcome and Outcome Date must appear together.");
      }
    }

    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fText, fCreationDate, fRemark, fOutcome, fOutcomeDate};
  }
}