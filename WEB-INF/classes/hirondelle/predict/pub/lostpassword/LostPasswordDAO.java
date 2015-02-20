package hirondelle.predict.pub.lostpassword;

import static hirondelle.predict.pub.lostpassword.LostPasswordAction.FETCH_PARTIAL_USER;
import static hirondelle.predict.pub.lostpassword.LostPasswordAction.SET_TEMP_PASSWORD_NONCE;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.Db;
import hirondelle.web4j.model.Id;

/**
  Data Access Object (DAO) for resetting lost passwords (first form). 
*/
final class LostPasswordDAO {

  /** 
   Return the user object, if any, associated with the given email address.
   The email address column has a uniqueness constraint on it.
   If no user having the given email address is found, then <tt>null</tt> is returned.
  */ 
  PartialUser fetchUser(String aEmailAddr) throws DAOException {
    return Db.fetch(PartialUser.class, FETCH_PARTIAL_USER, Id.from(aEmailAddr));
  }

  /**
    Set a new nonce value for user having the given email address.  
    The nonce value is a one-time value. In this case, it acts as a handshake between this 
    application and the 'owner' of a given email account. 
  */
  void setNewNonce(PartialUser aUser, String  aNonce) throws DAOException {
    Db.edit(SET_TEMP_PASSWORD_NONCE, Id.from(aNonce), Id.from(aUser.getEmailAddress()));
  }
  
}
