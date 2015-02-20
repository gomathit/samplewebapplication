-- Delete all items related to an account.
-- Does 3.23 have cascading deletes? No, but modern versions of MySQL do.

-- If 5.0 has cascading deletes, they should be used instead of this piecemeal approach.

-- These operations need to be executed in a transaction.

DELETE_PREDICTIONS {
  DELETE FROM Prediction WHERE PredictionListFK=?
}
 
DELETE_LISTS {
   DELETE FROM PredictionList WHERE UserFK=?
}

DELETE_ROLES {
  DELETE FROM UserRole WHERE LoginName=?
}

DELETE_USER {
  DELETE FROM Users WHERE LoginName=?
}