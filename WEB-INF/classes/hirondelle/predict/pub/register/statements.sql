-- Register a new user

-- If the Login Name or Email Address is already taken, then this operation will fail, 
-- because of uniqueness constraints on those fields.
ADD_NEW_USER {
  INSERT INTO Users (LoginName, ScreenName, Password, Email) VALUES (?, ?, ?, ?);
}

-- When a user is added, they need to be assigned a role as well
-- In this case, the user is assigned only a single role
-- If multiple roles were assigned, then this would need to be called N times, 
-- in a loop. That's not necessary in the present case.
-- This operation handles regular users only. Any other type of user (admin, translator) will be 
-- added manually, by INSERTing a new row into the UserRole table
ADD_NEW_USER_ROLE {
  INSERT INTO UserRole (LoginName) VALUES (?);
}


