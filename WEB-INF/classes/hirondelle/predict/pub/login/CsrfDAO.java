package hirondelle.predict.pub.login;

import hirondelle.web4j.database.SqlId;

/**
  This class exists only to declare {@link SqlId}s needed for {@link hirondelle.web4j.security.CsrfFilter}
  operations.  
 */
public final class CsrfDAO {

  /** Used by WEB4J to protect against CSRF attacks.  */
  public static final SqlId FETCH_FORM_SOURCE_ID =  new SqlId("FETCH_FORM_SOURCE_ID");
  
  /** Used by WEB4J to protect against CSRF attacks.  */
  public static final SqlId SAVE_FORM_SOURCE_ID =  new SqlId("SAVE_FORM_SOURCE_ID");
  
}
