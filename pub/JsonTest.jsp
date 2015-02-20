<%@ page contentType="text/html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <meta name="keywords" content="predictions">
 <meta name="description" content="Simple tool for tracking your predictions over time.">
 <title>Simple Test for JSON data</title>
 <c:url var='stylesheetURL' value='/css/stylesheet10.css' />
 <link rel="stylesheet" type="text/css" href='${stylesheetURL}'  media="all"> 
</head> 

<body>
<tags:headerAndMenu/> 
<h2>Simple Test of JSON</h2>
<tags:displayMessages/>

 
<div class="body">
 This page does a simple test, using JavaScript to fetch JSON data from the server, and display it here in a simple manner.

<P>

<script language='javascript' type='text/javascript'>
  var jsonObject;
  //fetch the JSON content from the server, using a fixed id for the Prediction List
  var request = new XMLHttpRequest();
  //set the hard-coded URL (simple synchronous request is used here) - change the ListId if necessary!
  request.open("GET", "http://localhost:8081/predict/pub/json/ViewPublicListJsonAction.do?ListId=1", false); 
  //set any headers you may need
  request.setRequestHeader("User-Agent", "XMLHttpRequest");
  //send the GET request to the server, and retrieve the JSON data
  request.send(null);
  //the state of the request object is now updated with the JSON data
  if( request.status == 200 ) {
    var rawJsonData = request.responseText;
    //alert('Success. Raw JSON data: ' + rawJsonData);
    //you need to add quotes to the raw JSON data, before passing it to the eval function
    jsonObject = eval("(" + rawJsonData + ")");
    document.writeln('<br>Predictions by: ' + jsonObject.predictionlist.owner + '<br>');
    document.writeln('Title: ' + jsonObject.predictionlist.title + '<br>');
    //for each prediction, show one per line
    for( i=0; i < jsonObject.predictionlist.prediction.length; i++ ) {
      document.writeln(jsonObject.predictionlist.prediction[i].text + '<br>');
    }
  }
  else {
    alert('Oops! Did not fetch JSON data successfully.');
  }
</script>
 
</div>

 <tags:footer/>
</body>
</html>
