package hirondelle.predict.pub.json;

import hirondelle.predict.main.lists.PredictionList;
import hirondelle.predict.main.lists.PredictionListDAO;
import hirondelle.predict.main.prediction.Prediction;
import hirondelle.predict.main.prediction.PredictionDAO;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.model.AppException;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.util.Consts;
import hirondelle.web4j.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

/**
 Serve a given {@link PredictionList} in <a href='http://www.json.org/'>JSON</a> format.
 Such data would typically be consumed by javascript code, which would render the data 
 in some desired way.
 
 <P>See the <tt>/pub/JsonTest.jsp</tt> for an example of using javascript to call this 
 action, and render the data in a web page.
   
 @view /pub/view.json - this view can be reused by any other feature in the app 
 that needs to serve JSON.
*/
public final class ViewPublicListJsonAction  extends ActionImpl {

  /** Constructor. */
  public ViewPublicListJsonAction(RequestParser aRequestParser){
    super(JSON_RESPONSE, aRequestParser);
  }
  
  public static final RequestParameter LIST_ID = RequestParameter.withLengthCheck("ListId");
  public static final SqlId LIST_PREDICTIONS =  new SqlId("LIST_PREDICTIONS");

  /**
   Fetch a {@link PredictionList}, its {@link Prediction}s, and its average score, and place 
   them all in request scope. 
  */
  @Override public ResponsePage execute() throws AppException {
    //the response's content-type is set to application/json in the JSP, not here

    //fetch the underlying data in the usual way
    List<Prediction> predictions = fDAO.list(getIdParam(LIST_ID));
    Integer averageScore = Prediction.calculateAverageScore(predictions);
    PredictionList list = fetchPredictionList();
    
    //build a string containing your json data, out of the items fetched above 
    String json = formatAsJson(predictions, averageScore, list); //hard-coded result!; see below
    
    //add your json string to request scope, just like any other data
    //the key has to match the reference in the JSP (this example uses 'data' as the key)
    addToRequest(DATA, json);
    
    //forward to a dead-simple jsp (see constructor); it adds any headers you may need, usually 
    //content-type and encoding; the exact same page can be referenced any time you need 
    //to serve json data 
    return getResponsePage();
  }

  // PRIVATE 
  private PredictionDAO fDAO = new PredictionDAO();
  private static final Logger fLogger = Util.getLogger(ViewPublicListJsonAction.class);
  
  private PredictionList fetchPredictionList() throws DAOException {
    PredictionListDAO dao = new PredictionListDAO();
    PredictionList result = dao.fetchPublic(getIdParam(LIST_ID));
    fLogger.fine("Parent list: " + result);
    return result;
  }
  
  /*
    A reference to a single, simple JSP you have made for serving all of your json data.
    
    This style references a JSP relative to the root directory of your web app. That JSP 
    has a simple job: to accept a single object named 'data', and call its toString method. 
    
    <P>Using a JSP as a means of serving the json text lets you control headers and 
    such, using all the tools available in JSP's. Note that JSP's are designed to 
    serve ANY kind of text content you can think of: HTML, plain text, XML, 
    JSON - anything at all. So, there's nothing strange about using a JSP
    to help serve json data.
    
    <P>The style seen below references a file in the application's root directory. 
    That's the preferred style when more than one feature needs to return JSON data. If your 
    app serves a lot of JSON, you should define such a JSON_RESPONSE object in 
    a SINGLE place, and just reference it where you need to serve JSON data.
    
    <P>Note the suffix, '.json'. You need to add a simple servlet-mapping entry in web.xml, to tell
    Tomcat (or whatever your container is) that any .json files are to be served 
    with its usual, built-in JSP servlet.
  */
  private static final ResponsePage JSON_RESPONSE = new ResponsePage("/pub/view.json").setIsRedirect(false);
  
  /*
   Use this style if the JSP is in the same directory as this class (which is not the case here). 
   This style is preferred only when this class is the only feature serving JSON in the app.
  */
  //private static final ResponsePage FORWARD = new ResponsePage("view.json", ViewPublicListJsonAction.class);
  
  /**
   This implementation cheats: it returns hard-coded text. You would never 
   do this in a real application! 
   
   The general idea is that you would use your tool of choice for creating a 
   JSON string out of the given objects. How you do that is up to you; web4j itself 
   has no special help for you in this regard.
  */
  private String formatAsJson(List<Prediction> aPredictions, Integer aAverageScore, PredictionList aList){
    //the args are ignored; return hard-coded json text
    StringBuilder result = new StringBuilder();
    try {
      InputStream input = this.getClass().getResourceAsStream("hard-coded-json.txt"); 
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));
      try {
        String line = null;
        while ((line = reader.readLine()) != null) {
          result.append(line + Consts.NEW_LINE);
        }      
      }
      finally {
        if (reader != null) reader.close();
      }
    }
    catch(IOException ex){
      ex.printStackTrace();
    }
    return result.toString();
  }
  
}