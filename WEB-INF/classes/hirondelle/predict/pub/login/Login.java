package hirondelle.predict.pub.login;

import hirondelle.predict.main.preferences.Preferences;
import hirondelle.predict.main.preferences.PreferencesDAO;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.security.LoginTasks;
import hirondelle.web4j.security.SafeText;
import hirondelle.web4j.util.Util;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 React to successful user login.
 
 <P>Web4j apps are required to implement the <tt>LoginTasks</tt> interface.
  
 <P>Places important items related to the user in session scope. 
 One particularly important item is the user id, which is used in many places, to properly 
 ensure operations are scoped to the correct user.
*/
public final class Login implements LoginTasks {
  
  public boolean hasAlreadyReacted(HttpSession aSession) {
    return aSession.getAttribute(Preferences.SCREEN_NAME) != null;
  }
  
  public void reactToUserLogin(HttpSession aSession, HttpServletRequest aRequest) throws AppException {
    //SafeText is used here to avoid using String 
    //AllowStringAsBuildingBlock is set to NO in web.xml
    SafeText userName = SafeText.from(aRequest.getUserPrincipal().getName());
    fLogger.fine("Adding user preferences to session, for principal name:" + userName);
    PreferencesDAO dao  = new PreferencesDAO();
    try {
      Preferences prefs = dao.fetch(userName);
      fLogger.fine("Adding user id and screen name to session");
      aSession.setAttribute(ActionImpl.USER_ID, prefs.getUserId());
      aSession.setAttribute(Preferences.SCREEN_NAME, prefs.getScreenName());
    }
    catch (DAOException ex){
      throw new AppException("Cannot fetch Preferences from database.", ex);
    }
  }
  
  // PRIVATE 
  private static final Logger fLogger = Util.getLogger(Login.class);

}
