package hirondelle.predict.pub.resetpassword;

import static hirondelle.predict.pub.resetpassword.ResetPasswordAction.RESET_LOST_PASSWORD;
import hirondelle.predict.pub.register.PasswordHasher;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.model.Id;

/**
 Data Access Object (DAO) for resetting the user's password. 
*/
final class ResetPasswordDAO {

  /**
   Reset the password to a new value.
   
   <P>The user account is identified by the email address. 
   The update occurs only if the nonce value present in <tt>aReset</tt>
   matches the nonce attached to the user account, and only if the nonce is not 
   more than 15 minutes old. 
  */
  int resetPassword(ResetPassword aReset) throws DAOException {
    String hashedPassword =  PasswordHasher.hash(aReset.getPassword().getRawString()); 
    return Db.edit(RESET_LOST_PASSWORD, Id.from(hashedPassword), aReset.getEmail(), aReset.getNonce());    
  }
}
