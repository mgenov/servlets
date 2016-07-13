package com.clouway.bank.validator;

import com.clouway.bank.core.User;
import com.clouway.bank.core.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class UserValidator implements Validator<User> {
  private final Pattern emailPattern = Pattern.compile(
          "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
  private final Pattern namePattern = Pattern.compile("[a-zA-Z]{3,15}");
  private final Pattern passPattern = Pattern.compile("[A-Za-z0-9]{6,18}");

  @Override
  public String validate(User user) {
    Matcher email = emailPattern.matcher(user.email);
    Matcher name = namePattern.matcher(user.name);
    Matcher password = passPattern.matcher(user.password);

    StringBuilder message = new StringBuilder();
    message.append(validateMatching(name, "The name should be between 1 and 15 letters" + "<br>"));
    message.append(validateMatching(email, "The email format should be like example@domain.com" + "<br>"));
    message.append(validateMatching(password, "The password should be at least 6 symbols" + "<br>"));

    return message.toString();
  }

  @Override
  public String validate(String email, String password) {
    Matcher userEmail = emailPattern.matcher(email);
    Matcher userPassword = passPattern.matcher(password);

    StringBuilder message = new StringBuilder();
    message.append(validateMatching(userEmail, "The email or password is wrong"));
    message.append(validateMatching(userPassword, "The email or password is wrong"));

    return message.toString();
  }

  private String validateMatching(Matcher matcher, String errorMessage) {
    if (!matcher.matches()) {
      return errorMessage;
    } else {
      return "";
    }
  }
}