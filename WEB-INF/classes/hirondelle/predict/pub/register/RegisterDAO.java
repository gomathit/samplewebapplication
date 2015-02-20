package hirondelle.predict.pub.register;

import static hirondelle.predict.pub.register.RegisterAction.ADD_NEW_USER;
import static hirondelle.predict.pub.register.RegisterAction.ADD_NEW_USER_ROLE;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DuplicateException;
import hirondelle.web4j.database.SqlId;
import hirondelle.web4j.database.TxSimple;
import hirondelle.web4j.model.Id;

/** 
 Data Access Object (DAO) for registration of new users. 
*/
final class RegisterDAO {  
  
  /**
   Add a newly registered user to the database.
    
   <P>The password is saved using a hash - see {@link PasswordHasher}.
   The user is saved, and assigned a 'user' role.
   The implementation uses a database transaction. If the user has attempted to use a Login Name 
   or Email Address that already exists, then the transaction will be rolled back.
  */
  void add(Register aRegister) throws DAOException, DuplicateException {
    SqlId[] sqls = new SqlId[]{ADD_NEW_USER, ADD_NEW_USER_ROLE};
    String hashedPassword =  PasswordHasher.hash(aRegister.getPassword().getRawString()); 
    Object[] params = new Object[]{
      /*params for user: */
      aRegister.getLoginName(), aRegister.getScreenName(), Id.from(hashedPassword), aRegister.getEmail(),
      /*params for role: */
      aRegister.getLoginName()
    };
    TxSimple tx = new TxSimple(sqls, params);
    int numRecords = tx.executeTx();
    if ( numRecords != 2 ){
      String msg = "Should have added 2 records (user and single role), but added this many: " + numRecords;
      throw new AssertionError(msg);
    }
  }
}