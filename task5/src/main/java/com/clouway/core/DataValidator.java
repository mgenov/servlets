package com.clouway.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kristiyan Petkov  <kristiqn.l.petkov@gmail.com> on 27.05.16.
 */
public class DataValidator implements Validator {
  public boolean isValid(String email, String password) {
    boolean result = false;
    String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    String passwordPattern = "^[a-zA-Z0-9]{6,16}+$";
    Pattern ePattern = Pattern.compile(emailPattern);
    Pattern pPattern = Pattern.compile(passwordPattern);
    Matcher eMatcher = ePattern.matcher(email);
    Matcher pMatcher = pPattern.matcher(password);
    if (eMatcher.matches() && pMatcher.matches()) {
      result = true;
    }
    return result;
  }

  public boolean isValid(String username, String password, String email) {
    boolean result = false;
    String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    String passwordPattern = "^[a-zA-Z0-9]{6,16}+$";
    String usernamePattern = "^[a-zA-Z0-9]{6,16}+$";
    Pattern ePattern = Pattern.compile(emailPattern);
    Pattern pPattern = Pattern.compile(passwordPattern);
    Pattern uPattern = Pattern.compile(usernamePattern);
    Matcher eMatcher = ePattern.matcher(email);
    Matcher pMatcher = pPattern.matcher(password);
    Matcher uMatcher = uPattern.matcher(username);
    if (eMatcher.matches() && pMatcher.matches() && uMatcher.matches()) {
      result = true;
    }
    return result;
  }

  public boolean isValid(String amount) {
    boolean result = false;
    String amounPattern = "^(0|0?[1-9]\\d*)\\.\\d\\d$";
    Pattern aPattern = Pattern.compile(amounPattern);
    Matcher aMatcher = aPattern.matcher(amount);
    if (aMatcher.matches()) {
      result = true;
    }
    return result;
  }
}
