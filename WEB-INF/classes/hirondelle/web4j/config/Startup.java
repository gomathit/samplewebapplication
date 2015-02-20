package hirondelle.web4j.config;

import hirondelle.predict.main.codes.CodeTableUtil;
import hirondelle.predict.util.Captcha;
import hirondelle.web4j.StartupTasks;
import hirondelle.web4j.database.DAOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/** Implementation of {@link StartupTasks}, required by WEB4J.  */
public final class Startup implements StartupTasks {

  /** 
   Perform startup tasks.
   
   This implementation has 2 tasks:
   <ul>
   <li>look up code tables, and place them into application scope.
   <li>pass along a value in web.xml to {@link Captcha#init(String)}.
   </ul>
  */
  public void startApplication(ServletConfig aConfig, String aDbName) throws DAOException {
    if (ConnectionSrc.DEFAULT.equals(aDbName)){
      fContext = aConfig.getServletContext();
      lookUpCodeTablesAndPlaceIntoAppScope();
      Captcha.init(aConfig.getInitParameter("CaptchaPrivateKey"));
    }
  }

  /**
   Called upon startup, and when the content of a pick list changes.
  
   <P>Refreshes the copies of pick lists placed in application scope.
  */
  public static void lookUpCodeTablesAndPlaceIntoAppScope() throws DAOException {
    CodeTableUtil.init(fContext);
  }
  
  // PRIVATE 
  private static ServletContext fContext;
}
