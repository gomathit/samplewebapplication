package hirondelle.predict.main.lists;

import hirondelle.web4j.model.Check;
import hirondelle.web4j.model.ModelCtorException;
import hirondelle.web4j.model.ModelUtil;
import static hirondelle.web4j.util.Consts.FAILS;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.security.SafeText;

/**  Model Object for a list of related predictions.*/
public final class PredictionList {

  /**
   Constructor.
    
   @param aId Internal database primary key, optional
   @param aTitle Title or name of this list of predictions, required, max 50 chars, not unique for given user
   @param aCreationDate Date and time this prediction list was created, required. Never changes.
   @param aUser User id of the list owner, required
  */
  public PredictionList(
   Id aId, SafeText aTitle, DateTime aCreationDate, Id aUser
  ) throws ModelCtorException {
    fId = aId;
    fTitle = aTitle;
    fCreationDate = aCreationDate;
    fUser = aUser;
    validateState(CheckDate.YES);
  }
  
  /**
  Constructor for public view of a prediction list. 
  This constructor has the creator's screen name as an additional parameter.
   
  @param aId Internal database primary key, optional
  @param aTitle Title or name of this list of predictions, required, max 50 chars, not unique for given user
  @param aCreationDate Date and time this prediction list was created, required. Never changes.
  @param aUser User id of the list owner, required
  @param aUserScreenName screen name of the owner, required
 */
  public PredictionList(
    Id aId, SafeText aTitle, DateTime aCreationDate, Id aUser, SafeText aUserScreenName
  )  throws ModelCtorException {
     fId = aId;
     fTitle = aTitle;
     fCreationDate = aCreationDate;
     fUser = aUser;
     fUserScreenName = aUserScreenName;
     validateState(CheckDate.YES);
   }
  
  /**
   Constructor used when building from user input.
   
   @param aId Internal database primary key, optional
   @param aTitle Title or name of this list of predictions, required, max 50 chars, (not unique for given user)
   @param aUser User id of the list owner, required, must come from session-scope object, not from user input
  */
  public PredictionList(Id aId, SafeText aTitle, Id aUser) throws ModelCtorException {
    fId = aId;
    fTitle = aTitle;
    fUser = aUser;
    validateState(CheckDate.NO);
  }
  
  public Id getId() { return fId; }  
  public SafeText getTitle() { return fTitle; }
  /** Returns null if no creation date passed to constructor. */
  public DateTime getCreationDate() {  return fCreationDate; }  
  public Id getUser() { return fUser; }
  public SafeText getUserScreenName() { return fUserScreenName; }
  
  @Override public String toString(){
    return ModelUtil.toStringFor(this);
  }
  
  @Override public boolean equals(Object aThat){
    Boolean result = ModelUtil.quickEquals(this, aThat);
    if ( result == null ) {
      PredictionList that = (PredictionList)aThat;
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
  private final SafeText fTitle;
  private DateTime fCreationDate;
  private final Id fUser;
  private SafeText fUserScreenName;
  private int fHashCode;
  
  private enum CheckDate{YES, NO};

  private void validateState(CheckDate aCheckDate) throws ModelCtorException {
    ModelCtorException ex = new ModelCtorException();
    
    if ( FAILS == Check.required(fTitle, Check.max(50)) ) {
      ex.add("Please provide a title (up to 50 characters)");
    }
    if ( FAILS == Check.required(fUser) ){
      ex.add("User id is missing (programmer error)");
    }
    if( CheckDate.YES == aCheckDate ){
      if ( FAILS == Check.required(fCreationDate) ) {
        ex.add("Creation date missing is missing (programmer error).");
      }
    }

    if ( ! ex.isEmpty() ) throw ex;
  }
  
  private Object[] getSignificantFields(){
    return new Object[] {fTitle, fCreationDate, fUser};
  }
}