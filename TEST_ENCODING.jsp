<%@ page contentType="text/html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
 <tags:headsimple title='Home'/>
<body>

<tags:headerAndMenu/> 

<h2>So What's This All About?</h2>

This site is for storing, sharing, and searching <b>predictions</b>. Why? Just for fun...

<P>The following features are available :
<ul>
 <li>you can create as many predictions as you want. 
 <li>all predictions are publicly viewable and searchable.
 <li>you can assign your predictions a result or score.
 <li>you can arrange your predictions in separate lists.
</ul>


 <tags:footer/>
<%-- <tags:tracking/> --%>

<PRE>
Encoding (web4j_key_for_character_encoding) : ${web4j_key_for_character_encoding}
Locale (web4j_key_for_locale) : ${web4j_key_for_locale}
Request encoding (as set by Controller): <%= request.getCharacterEncoding() %>
Response encoding (as set by Controller): <%= response.getCharacterEncoding() %>
Czech 
 should be :   Jako efektivn&#x115;j&#x161;&#xED; se n&#xE1;m jev&#xED; po&#x159;&#xE1;d&#xE1;n&#xED; tzv.
 rendered as : Jako efektivnější se nám jeví pořádání tzv.

Arabic
 should be :    &#x06A4;
 rendered as :  ڤ


</PRE>
</body>
</html>