package com.clouway.bank.validator;

import com.clouway.bank.core.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class AmountValidator implements Validator<String> {
  private final Pattern amountPattern = Pattern.compile("^[0-9]{1,4}+(\\.[0-9]{2})?$");

  @Override
  public String validate(String amount) {
    Matcher amountMatcher = amountPattern.matcher(amount);
    StringBuffer buffer = new StringBuffer();
    buffer.append(validateMatching(amountMatcher, "Amount must be positive number."));

    return buffer.toString();
  }

  @Override
  public String validate(String email, String password) {
    return null;
  }

  private String validateMatching(Matcher matcher, String errorMessage) {
    if (!matcher.matches()) {
      return errorMessage;
    } else {
      return "";
    }
  }
}
