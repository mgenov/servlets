package com.clouway.bank.http.validation;

import com.clouway.bank.core.TransactionValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates transaction data
 *
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class BankTransactionValidator implements TransactionValidator {

  /**
   * Validates the value of funds in the transaction
   *
   * @param amount to be validated
   * @return empty string if the value is valid, and error message if it is not
   */
  @Override
  public String validateAmount(String amount) {
    String amountRegex = "^([0-9]{1,10})||(([0-9]{1,10})\\.([0-9]{1,2}))$";
    Pattern pattern = Pattern.compile(amountRegex);
    Matcher matcher = pattern.matcher(amount);
    if (matcher.matches()) {
      return "";
    } else {
      return "incorrect value, has to have a positive number '.' and one or two digits afterwards";
    }
  }
}
