package hirondelle.predict.pub.search;

/** 
 Simple enumeration of the various styles of search.
 None of the search styles is sensitive to case.  
*/
enum SearchStyle {
  
  /** 
   The search is on the whole, exact phrase entered by the user. 
  */
  ExactPhrase,
  
  /** 
   The search is for predictions that contain all of the given words entered by the user.
   The order of the words entered by the user has no relevance. 
  */
  AllOfTheWords; 
}
