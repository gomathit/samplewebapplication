package hirondelle.predict.main.codes;

import hirondelle.web4j.model.Id;
import hirondelle.web4j.model.Code;

/**
 All code tables used in this application. (This implementation has only a single code table, 
 but most applications will have many code tables.)
 
 <P>See package summary for important information.
*/
public enum CodeTable {
  
  /** 
   End outcome of a prediction. For this item, {@link #getTableName()} returns: <tt>'outcome'</tt>.
   
   <P>A score out of 100 is attached to each outcome :
   <ul>    <li>Right - 100    <li>Mostly Right - 75
    <li>Half Right - 50
    <li>Mostly Wrong - 25
    <li>Wrong - 0    <li>Undecidable - Unknown
   </ul> 
  */
  OUTCOMES("outcome", true);
  
  /** 
   Return the name of the code table. 
   This name is used as the key for the corresponding {@code List<Code>} placed in application scope.
  */
  public Id getTableName() { return fTableName; }
  
  /** 
   Return <tt>true</tt> only if this code table has an explicit ordering.
   If there is an explicit order, it is usually related to a database column 
   defined for that purpose. 
  */
  public Boolean hasExplicitOrderColumn(){ return fHasOrderIndex; }
  
  /**
   Return a fully populated {@link Code}, given its {@link Id} and {@link CodeTable}.
   
   <P><span class='highlight'>Called from Model Object constructors that wish to translate a 
   simple {@link Id} for a known code table into a fully populated {@link Code}</span>, complete 
   with user-presentable text. See {@link CodeTableUtil} for more information.
  */
  public static Code codeFor(Id aCodeId, CodeTable aCodeTable){
    return CodeTableUtil.populate(aCodeId, aCodeTable);
  }
  
  // PRIVATE //
  private CodeTable(String aTableName, Boolean aHasOrderIndex){
    fTableName = new Id(aTableName);
    fHasOrderIndex = aHasOrderIndex;
  }
  private final Id fTableName;
  private final Boolean fHasOrderIndex; //never null  
}
