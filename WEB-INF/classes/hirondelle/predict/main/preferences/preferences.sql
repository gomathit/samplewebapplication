-- Here, the login name is used as criterion, not the user id, since it's already convenient to do so
FETCH_PREFERENCES {
  SELECT Id, LoginName, ScreenName FROM Users WHERE LoginName=?
}

CHANGE_PREFERENCES {
  UPDATE Users SET  ScreenName=? WHERE LoginName=?
}
