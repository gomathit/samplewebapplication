package hirondelle.predict.pub.register;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 Hash passwords.

 <P>A hash function is a one-way function that returns text that is difficult to reverse.
 A hash function is not an encryption, since the hashed value is never meant to be decrypted.
  
 <P>It is recommended that a random-but-fixed 'salt' value be added to a password before it 
 is hashed. That is not done here, but only because 
 Tomcat's implementation of form-based login allows for hashes, but not for salt values. 
 
 <P>Passwords should never be stored in cleartext. 
 All user passwords will pass through this class before they are stored in the database. 
 As well, when the user logs in, the submitted password is first hashed (again, using this class) 
 before the comparison with the stored value is made. 
 
*/
public final class PasswordHasher {
  
  /** 
   As a simple defensive measure, this class will verify the presence of a hash function in 
   the current JRE, when this class is loaded. 
  */
  static {
    MessageDigest sha = null;
    try {
      sha = MessageDigest.getInstance("SHA-1");
    }
    catch (NoSuchAlgorithmException ex){
      throw new RuntimeException("Unable to hash passwords. MessageDigest class cannot find the desired hash function.");
    }
  }
  
  /** 
   Return text of fixed length, representing a hash of a cleartext password. 
   */
  public static String hash(String aCleartextPassword){
    String result = null;
    MessageDigest sha = null;
    try {
      sha = MessageDigest.getInstance("SHA-1");
    }
    catch (NoSuchAlgorithmException ex){
      throw new RuntimeException("Cannot find SHA-1 hash function.");
    }
    //the salt is not included here - see class comment
    String clearTextPlusSalt = aCleartextPassword /* + SALT*/;
    byte[] digest =  sha.digest( clearTextPlusSalt.getBytes() );
    result = hexEncode(digest);
    return result;
  }
  
  // PRIVATE //
  
  /**
   Using a salt is desirable, but unfortunately Tomcat's default login mechanism does not 
   know about salt values - only the basic hash function can be specified, with no salt value.  
  */
  private static final String SALT = "ujdndk893rdlskjd378lkasdnm93lksdjfdl93287256dc";
  
  /**
   The byte[] returned by MessageDigest does not have a nice
   textual representation, so some form of encoding is usually performed.
 
   This implementation follows the example of David Flanagan's book
   "Java In A Nutshell", and converts a byte array into a String
   of hex characters.
  */
  private static String hexEncode( byte[] aInput){
    StringBuffer result = new StringBuffer();
    char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
    for ( int idx = 0; idx < aInput.length; ++idx) {
      byte b = aInput[idx];
      result.append( digits[ (b&0xf0) >> 4 ] );
      result.append( digits[ b&0x0f] );
    }
    return result.toString();
  }  
}