package hirondelle.predict.main.deleteaccount;

import static hirondelle.predict.main.deleteaccount.DeleteAccountAction.DELETE_LISTS;
import static hirondelle.predict.main.deleteaccount.DeleteAccountAction.DELETE_PREDICTIONS;
import static hirondelle.predict.main.deleteaccount.DeleteAccountAction.DELETE_ROLES;
import static hirondelle.predict.main.deleteaccount.DeleteAccountAction.DELETE_USER;
import hirondelle.predict.main.lists.PredictionList;
import hirondelle.web4j.database.DAOException;
import hirondelle.web4j.database.DbTx;
import hirondelle.web4j.database.Tx;
import hirondelle.web4j.database.TxTemplate;
import hirondelle.web4j.model.Id;
import hirondelle.web4j.security.SafeText;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/** Data Access Object (DAO) for deleting user accounts. */
final class DeleteAccountDAO {
  
  /**
   Delete the current user's account. 
   
   <P>All data is deleted, with no recovery possible. 
   A transaction is used here to wrap multiple operations.
   <P>Return the total number of records deleted.
   
   <P>This method deletes data from the following tables (in the given order): 
   <ul>   <li>Prediction (this is a looping operation)   <li>PredictionList   <li>UserRole
   <li>Users
   </ul>
   
   @param aLoginName current user's login name
   @param aUserId underlying database id corresponding to the login name
   @param aLists all prediction lists owned by the user 
   */
  int delete(SafeText aLoginName, Id aUserId, List<PredictionList> aLists) throws DAOException{
    Tx deleteAll = new DeleteAllTx(aLoginName, aUserId, aLists);
    return deleteAll.executeTx();
  }

  // PRIVATE //

  /** Transaction for deleting all items as a single unit of work. */
  private static final class DeleteAllTx extends TxTemplate {
    DeleteAllTx(SafeText aLoginName, Id aUserId, List<PredictionList> aLists){
      fLoginName = aLoginName;
      fUserId = aUserId;
      fLists = aLists;
    }
    @Override public int executeMultipleSqls(Connection aConnection) throws SQLException, DAOException {
      int result = 0;
      for(PredictionList list: fLists){
        result = result + deletePredictionsFor(list.getId(), aConnection);
      }
      result = result + DbTx.edit(aConnection, DELETE_LISTS, fUserId);
      result = result + DbTx.edit(aConnection, DELETE_ROLES, fLoginName);
      result = result + DbTx.edit(aConnection, DELETE_USER, fLoginName);
      return result;
    }
    private SafeText fLoginName;
    private Id fUserId;
    private List<PredictionList> fLists;
    private int deletePredictionsFor(Id aListId, Connection aConnection) throws DAOException {
      return DbTx.edit(aConnection, DELETE_PREDICTIONS, aListId);
    }
  }
}
