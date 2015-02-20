<%@ include file="/WEB-INF/TagHeader.jspf" %>
<script type="text/javascript"  src="http://api.recaptcha.net/challenge?k=YOUR_PUBLIC_KEY_GOES_HERE"></script>
<noscript>
<iframe src="http://api.recaptcha.net/noscript?k=YOUR_PUBLIC_KEY_GOES_HERE"  height="300" width="500" frameborder="0"></iframe><br>
<textarea name="recaptcha_challenge_field" rows="3" cols="40"></textarea>
<input type="hidden" name="recaptcha_response_field"  value="manual_challenge">
</noscript>