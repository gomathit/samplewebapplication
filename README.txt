This is an example WEB4J application.

This app is in the style typical of many public web apps, where the user 
controls the creation of their account, and where access to data is  
is based on the user, not on their role. (Intranet web apps usually 
grant access based on the user's role, not their id.)


This app uses a captcha mechanism.

Captcha's are those 'fuzzy character' things. They are used to prevent 
automated bots from creating accounts on websites. They trying to verify 
that an actual human being is attempting an operation, by asking the user
to perform a task that is difficult to implement in software.

This app requires you to create an account with recaptcha.net. 
After you create an account, you obtain a public key and a private key. 
You need to place the private key in web.xml, and the 
public key in the captcha.tag file.


This app should work with a large range of character sets, since
it uses UTF-8 as its encoding. 