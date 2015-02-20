package hirondelle.web4j.config;

import hirondelle.web4j.ApplicationInfo;
import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.util.Consts;

/**
 Implementation of {@link ApplicationInfo}, required by WEB4J.
 High-level information regarding the application.
*/
public final class AppInfo implements ApplicationInfo {
  
  public String getVersion(){    
    return "4.10.0.0";  
  }
  
  public DateTime getBuildDate(){
    return new DateTime("2014-10-30 13:51:12");
  }
  
  public String getName(){
    return "Predictions";
  }
  
  public String getAuthor(){
    return "Hirondelle Systems";
  }
  
  public String getLink(){
    return "http://www.web4j.com/";
  }
  
  public String getMessage(){
    return "App for sharing predictions. Example web4j application.";
  }

  /**
   Return {@link #getName()} + {@link #getVersion()}. 
  */
  @Override public String toString(){
    return getName() + Consts.SPACE + getVersion();
  }
}
