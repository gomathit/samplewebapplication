package hirondelle.predict.pub.lostpassword;

import hirondelle.web4j.security.SafeText;

/** Dumb data carrier. No validation. */
public final class PartialUser {
  
  /** Constructor. */
  public PartialUser(SafeText aEmail){
    fEmail = aEmail;
  }
  
  String getEmailAddress() { return fEmail.getRawString(); }

  // PRIVATE //
  private SafeText fEmail;
}
