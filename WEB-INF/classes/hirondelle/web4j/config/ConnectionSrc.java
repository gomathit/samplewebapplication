package hirondelle.web4j.config;

import hirondelle.web4j.database.ConnectionSource;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/** 
 Implementation of {@link ConnectionSource}, required by WEB4J. 
 
 <P>This application talks only to a single database. 
 
 <P>This implementation uses a <tt>Connection</tt> pool managed by the container.
 Only one method can be overridden - {@link #getConnectionByName(String)}.
*/
public class ConnectionSrc implements ConnectionSource  {
  
  /** Value {@value}. Name of <tt>init-param</tt> in web.xml used by this class. */
  public  static final String DEFAULT_CONN_STRING = "DefaultDbConnectionString";
  
  /** 
  Value - {@value}. Name used to identify the default database. 
  In this case, there is only one database. 
 */
  public static final String DEFAULT = "DEFAULT";
 

  /** Read in connection strings from <tt>web.xml</tt>. */
  public final void init(Map<String, String> aConfig){
    fDefaultDbConnString = aConfig.get(DEFAULT_CONN_STRING);
    ensureAllSettingsPresent();
    fMapNameToConnectionString = new LinkedHashMap<String, String>();
    fMapNameToConnectionString.put(DEFAULT, fDefaultDbConnString);
    fLogger.config(
      "Connection strings : " + Util.logOnePerLine(fMapNameToConnectionString)
    );
  }

  /**  Return value contains only {@link #DEFAULT}.  */
  public final Set<String> getDatabaseNames(){
    return Collections.unmodifiableSet(fMapNameToConnectionString.keySet()); 
  }
  
  /**  Return a {@link Connection} for the default database.  */
  public final Connection getConnection() throws DAOException {
    return getConnectionByName(DEFAULT);
  }

  /**
   Return a {@link Connection} for the identified database.
   @param aDatabaseName single value {@link #DEFAULT} 
  */
  public final Connection getConnection(String aDatabaseName) throws DAOException {
    return getConnectionByName(aDatabaseName);
  }
  
  /**
   Return a connection the named database. 
   <P>This method can be overridden by a subclass.
   Such overrides are intended for testing. 
  */ 
  protected Connection getConnectionByName(String aDbName) throws DAOException {
    Connection result = null;
    String dbConnString = getConnectionString(aDbName);  
    if( ! Util.textHasContent(dbConnString) ){
      throw new IllegalArgumentException(
        "Unknown database name : " + Util.quote(aDbName)
      );      
    }
    try {
      Context initialContext = new InitialContext();
      DataSource datasource = (DataSource)initialContext.lookup(dbConnString);
      if ( datasource == null ){
        fLogger.severe("Datasource is null for : " + dbConnString);
      }
      result = datasource.getConnection();
    }
    catch (NamingException ex){
      throw new DAOException(
        "Config error with JNDI and datasource, for db " + Util.quote(dbConnString), ex
      );
    }
    catch (SQLException ex ){
      throw new DAOException(
        "Cannot get JNDI connection from datasource, for db " + Util.quote(dbConnString), 
        ex
      );
    }
    return result;
  }
  
  /**
   This item is protected, in order to make it visible to any subclasses created 
   for testing outside of the normal runtime environment.
  */
  protected String getConnectionString(String aDbName){
    return fMapNameToConnectionString.get(aDbName);
  }
  
  // PRIVATE
  
  /**
   Maps the database name passed to {@link #getConnection(String)} to the 
   actual connection string.
  */
  private static Map<String, String> fMapNameToConnectionString;
  private static String fDefaultDbConnString;
  private static final Logger fLogger = Util.getLogger(ConnectionSrc.class);
  
  private static void ensureAllSettingsPresent(){
    if( ! Util.textHasContent(fDefaultDbConnString) ) {
      logError(DEFAULT_CONN_STRING);
    }
  }
  
  private static void logError(String aSettingName){
    fLogger.severe("Web.xml missing init-param setting for " + aSettingName);
  }
}