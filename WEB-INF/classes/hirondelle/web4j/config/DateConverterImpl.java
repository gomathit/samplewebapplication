package hirondelle.web4j.config;

import java.util.*;
import java.text.*;
import java.util.regex.*;

import hirondelle.web4j.model.DateTime;
import hirondelle.web4j.request.DateConverter;

/**
 Implementation of {@link DateConverter}, required by WEB4J.
 This implementation includes date only, with no time. 
*/
public final class DateConverterImpl implements DateConverter { 
  
  /**
   Format a {@link DateTime} for the human eye.
   <P>Example return value, for January 31, 2006:<br> 
   <tt>'2009/01/31'</tt>.
  */
  public String formatEyeFriendlyDateTime(DateTime aDateTime, Locale aLocale){
    return aDateTime.format("YYYY/MM/DD");
  }
  
  /**
   Parse a {@link DateTime} entered in an eye-friendly style.
   <P>Example of the required input format, for January 31, 2006 : <br>
   <tt>'2006/01/31'</tt>
 */
  public DateTime parseEyeFriendlyDateTime(String aInputValue, Locale aLocale){
    return parseDateTime(aInputValue, EYE_FRIENDLY_REGEX);
  }
  
  /**
   Parse a {@link DateTime} entered in a hand-friendly style.
   <P>Example of the required input format, for January 31, 2006 : <br>
   <tt>'20060131'</tt>.   
  */
  public DateTime parseHandFriendlyDateTime(String aInputValue, Locale aLocale){
    return parseDateTime(aInputValue, HAND_FRIENDLY_REGEX);
  }
  
  
  /**
   Format a {@link Date} for the human eye.
    
   <P>Example return value, for January 31, 2006:<br> 
   <tt>'2006/01/31'</tt>.
  */
  public String formatEyeFriendly(Date aDate, Locale aLocale, TimeZone aTimeZone) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd"); 
    format.setTimeZone(aTimeZone);
    String result = format.format(aDate);
    return result;
  }
  
  /**
   Parse a {@link Date} entered in a hand-friendly style.
     
    <P>Example of the required input format, for January 31, 2006: <br>
    <tt>'20060131'</tt>.   
  */
  public Date parseHandFriendly(String aInputValue, Locale aLocale, TimeZone aTimeZone) {
    return parse(aInputValue, HAND_FRIENDLY_REGEX, aTimeZone);
  }
  
  /**
   Parse a {@link Date} entered in an eye-friendly style.
     
    <P>Example of the required input format, for January 31, 2006 : <br>
    <tt>'2006/01/31'</tt>
  */
  public Date parseEyeFriendly(String aInputValue, Locale aLocale, TimeZone aTimeZone) {
    return parse(aInputValue, EYE_FRIENDLY_REGEX, aTimeZone);
  }
  
  // PRIVATE
  
  /*
   Patterns are thread-safe. Matchers and SimpleDateFormats are NOT thread-safe. 
   Items that are not thread-safe can be used only as local variables, not as fields.
  */

  /** Month in the Gregorian calendar: <tt>01..12</tt>.   */
  private static final String MONTH =
    "(01|02|03|04|05|06|07|08|09|10|11|12)"
  ;

  /** Day of the month in the Gregorian calendar: <tt>01..31</tt>.   */
  private static final String DAY_OF_MONTH = 
    "(01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31)"
  ;
   
  /** Format : 20060131.   */
  private static final Pattern HAND_FRIENDLY_REGEX = 
    Pattern.compile("(\\d\\d\\d\\d)" + MONTH + DAY_OF_MONTH)
  ;
  
  /** Format : 2006/01/31.   */
  private static final Pattern EYE_FRIENDLY_REGEX = 
    Pattern.compile("(\\d\\d\\d\\d)" + "/" + MONTH + "/" + DAY_OF_MONTH)
  ;
  
  /**
   Requires the month, day, year to be the first, second, and third groups, respectively. 
   Optionally, hours and minutes can appear as 4th and 5th groups, respectively.
  */
  private Date parse(String aInputValue, Pattern aRegex, TimeZone aTimeZone){
    Date result = null;
    Matcher matcher = aRegex.matcher(aInputValue);
    if( matcher.matches() ) {
      Integer year = new Integer( matcher.group(1) );
      Integer month = new Integer(matcher.group(2));
      Integer day = new Integer(matcher.group(3));
      Calendar cal = new GregorianCalendar(year.intValue(), month.intValue() - 1, day.intValue(), 0,0,0);
      cal.setTimeZone(aTimeZone);
      result = cal.getTime();
    }
    return result;
  }

  /** Simple 'struct' to hold related items. */
  private static final class DateTimeParts {
    String year;
    String month;
    String day;
  }
  
  private DateTime parseDateTime(String aInputValue, Pattern aRegex){
    DateTime result = null;
    Matcher matcher = aRegex.matcher(aInputValue);
    if( matcher.matches() ) {
      DateTimeParts parts = getParts(matcher);
      Integer year = new Integer(parts.year);
      Integer month = new Integer(parts.month);
      Integer day = new Integer(parts.day);
      result = DateTime.forDateOnly(year, month, day);
    }
    return result;
  }
  
  /** Requires the year, month, day, to be the first, second, and third groups, respectively. */
  private DateTimeParts getParts(Matcher aMatcher){
    DateTimeParts result = new DateTimeParts();
    result.year = aMatcher.group(1);
    result.month = aMatcher.group(2);
    result.day = aMatcher.group(3);
    return result;
  }
}
