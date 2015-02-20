Contains the CREATE TABLE statements and related items for 
creating and populating a target MySQL database. 

The example script supplied here is for MySQL 5.

It's of course possible to use various databases.

Changing databases should *usually* not involve changes to 
source code. Instead, the following configuration changes are
needed :
- some settings in web.xml
- configuration of a DataSource in the web container
- reviewing the .sql file(s)
  As an example of the changes required in .sql files, the 
  following differences were found when a port of one app from MySQL 
  to Oracle was performed :
      FORMAT -> TO_CHAR
      DATEFORMAT -> TO_DATE
      CONCAT(x,y,z) -> ||
      'AS' aliases need quoting to preserve case

For an example configuration of a connection pool using Tomcat,
please see the Getting Started Guide :

http://www.web4j.com/GettingStartedGuide.jsp

For those starting out with WEB4J, using MySQL 5 is strongly recommended, 
since the tutorials all assume MySQL.