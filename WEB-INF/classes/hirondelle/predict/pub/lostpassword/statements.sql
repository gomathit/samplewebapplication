-- Fetch a user record
FETCH_PARTIAL_USER {
  SELECT Email FROM Users WHERE Email=?
}

-- Update the user's record, to set a temporary nonce for resetting the password.
-- When the user attempts to reset it, the given nonce must be present, and 'not too old'
SET_TEMP_PASSWORD_NONCE {
  UPDATE Users SET PasswordNonce=?, PasswordNonceCreatedOn=NOW() WHERE Email=?
}



