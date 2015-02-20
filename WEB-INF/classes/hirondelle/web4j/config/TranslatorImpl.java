package hirondelle.web4j.config;

import java.util.*;
import hirondelle.web4j.ui.translate.Translator;

/**
 Implementation of {@link Translator}, required by WEB4J.
 
 <P>Currently, this application does not translate its user interface elements. 
 Thus, this implementation simply returns the text without any translation.
*/
public final class TranslatorImpl implements Translator {  

  /**  Returns <tt>aBaseText</tt> without alteration. See class comment.  */
  public String get(String aBaseText, Locale aLocale) {
    return aBaseText;
  }
  
}  