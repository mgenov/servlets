package com.clouway.bank.validator;

import com.clouway.bank.core.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AmountValidator implements Validator {
  private final Pattern amountPattern = Pattern.compile("^[0-9]{1,3}+(\\.[0-9]{2})?$");

  @Override
  public String validate(Object object) {
    return null;
  }

  @Override
  public String validate(String email, String password) {
    return null;
  }

  @Override
  public String validate(String cash) {
    Matcher cashMatcher = amountPattern.matcher(cash);
    StringBuffer buffer = new StringBuffer();
    buffer.append(validateMatching(cashMatcher, "The format of the amount is not valid. Valid format is 12.00. Please enter valid data!"));

    return buffer.toString();
  }

  private String validateMatching(Matcher matcher, String errorMessage) {
    if (!matcher.matches()) {
      return errorMessage;
    } else {
      return "";
    }
  }
}
