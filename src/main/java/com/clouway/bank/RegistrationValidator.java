package com.clouway.bank;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class RegistrationValidator {
  public String validateField(String fieldName, String fieldValue, String regex) {
    String validationMessage = "";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(fieldValue);
    boolean valid = matcher.find();
    if (!valid){
      validationMessage=fieldName+" is invalid";
    }
    return validationMessage;
  }

  public String matchPassword(String password, String confirmPassword) {
    String validationMessage = "";
    if (!password.equals(confirmPassword)){
      validationMessage = "passwords don't match";
    }
    return validationMessage;
  }
}
