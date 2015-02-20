-- These operations are implemented most simply with a 'user id proxy'.
-- Here, PredictionListFK acts as a proxy for the user id, since each 
-- prediction list is owned by a specific user.
--
-- Such user id proxies are 'hot spots', that need extra care: since they usually 
-- come from request parameters, they cannot be trusted, and need explicit verification.
-- This is implemented (in part) using the FETCH_OWNER operation.

LIST_PREDICTIONS {
 SELECT 
  Id, PredictionListFK, Text, CreationDate, Remark, OutcomeFK, OutcomeDate 
 FROM 
   Prediction
 WHERE 
   PredictionListFK=? 
 ORDER BY 
   CreationDate ASC
}

FETCH_PREDICTION {
 SELECT 
  Id,  PredictionListFK, Text, CreationDate, Remark, OutcomeFK, OutcomeDate 
 FROM 
   Prediction
 WHERE 
   Id=? AND PredictionListFK=?
}

ADD_PREDICTION {
 INSERT INTO Prediction (PredictionListFK, Text, CreationDate, Remark, OutcomeFK, OutcomeDate) 
 VALUES (?, ?, ?, ?, ?, ?)
}

CHANGE_PREDICTION {
  UPDATE Prediction SET Text=?, Remark=?, OutcomeFK=?, OutcomeDate=? 
  WHERE Id=? AND PredictionListFK=?
}

DELETE_PREDICTION {
  DELETE FROM Prediction WHERE Id=? AND PredictionListFK=?
}

-- Returns the owner of a given PredictionList.
-- The returned value is compared to the logged in user name.
FETCH_OWNER {
 SELECT 
   Users.LoginName
 FROM 
   PredictionList, Users
 WHERE
   PredictionList.UserFK = Users.Id AND 
   PredictionList.Id = ?
}
