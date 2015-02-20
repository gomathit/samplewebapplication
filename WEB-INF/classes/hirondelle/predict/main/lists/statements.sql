-- Important: 
-- The user id is used in statements to enforce ownership constraints.
-- The user id has been placed in session scope by LoginTasks

PREDICTION_LIST_LIST {
 SELECT 
  Id, Title, CreationDate, UserFK
 FROM 
   PredictionList
WHERE 
    UserFK=?
 ORDER BY 
   CreationDate ASC
}

PREDICTION_LIST_FETCH {
 SELECT 
  Id, Title, CreationDate, UserFK
 FROM 
   PredictionList
 WHERE Id=? AND UserFK=?
}

PREDICTION_LIST_FETCH_PUBLIC {
 SELECT 
   PredictionList.Id, PredictionList.Title, PredictionList.CreationDate, 
   PredictionList.UserFK, Users.ScreenName 
 FROM    
   PredictionList LEFT JOIN Users ON PredictionList.UserFK = Users.Id  
 WHERE 
   PredictionList.Id=?
}

PREDICTION_LIST_ADD {
 INSERT INTO PredictionList (Title, CreationDate, UserFK) 
 VALUES (?, ?, ?)
}

PREDICTION_LIST_CHANGE {
  --CreationDate never changes
  UPDATE PredictionList 
  SET Title=?
  WHERE Id=? AND UserFK=?
}

PREDICTION_LIST_DELETE {
  DELETE FROM PredictionList WHERE Id=? AND UserFK=?
}