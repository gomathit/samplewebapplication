-- The search on the text field will not be case sensitive, since 
-- the Text field is not defined as 'BINARY'.

-- This is an example of dynamic criteria
-- All static criteria must appear here, and all dynamic criteria are appended in code
SEARCH_FOR_EXACT_PHRASE {
  SELECT 
    Prediction.Text, Prediction.OutcomeFK, Prediction.CreationDate, 
    Users.ScreenName, PredictionList.Id
  FROM  Prediction 
  LEFT JOIN PredictionList ON PredictionListFK = PredictionList.Id 
  LEFT JOIN Users ON PredictionList.UserFK = Users.Id 
-- Dynamic Criteria that may follow :
-- WHERE 
-- TEXT Like ?  (required)
-- AND Prediction.CreationDate >= ? (optional)
-- AND Prediction.CreationDate <= ? (optional)
-- ORDER BY Prediction.CreationDate DESC LIMIT 200;
}

-- This is an example having ALL of the sql built in code, 
-- not just the criteria. This style should be avoided unless its
-- absolutely necessary, because building SQL in code is open to 
-- injection attacks. 
SEARCH_FOR_ALL_OF_THESE_WORDS {
  -- dynamic; built in code
}



