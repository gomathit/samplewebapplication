/** 
Allow anyone to search the text of predictions, looking for matches to 
text they provide. Doesn't require login.

<P>Search comes in two basic styles :
<ul>  <li>any-of-the-following-words style (the default); for example 'stanley cup bruins' might be 
  used to find all predictions regarding the Boston Bruins winning the Stanley Cup.
  <li>an exact phrase</ul>

<P>Search results include a link to the list which contains the prediction. 
It also shows the Screen Name of the user who made the prediction, and when they made it.

<P>Other points :
<ul>  <li>the user input for text must have at least two characters.
  <li>searches are always case-insensitive.
  <li>the database has an index for the field being searched.
  <li>there is a cap on the number of returned records.
  <li>the list is ordered by creation date (newest first).
  <li>if the user wishes to see more records, they must enter criteria on the date to display the 
  older records; this implements a simple kind of paging mechanism.
  <li>the from-date and to-date are dates, with year-month-day only, while the creation date of the 
  Prediction stored in the database is a full date-time.
</ul>
*/ 
package hirondelle.predict.pub.search;
