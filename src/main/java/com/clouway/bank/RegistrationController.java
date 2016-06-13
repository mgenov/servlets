package com.clouway.bank;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class RegistrationController {

  private UserRepository userRepository = new PersistentUserRepository();
  RegistrationValidator validator = new RegistrationValidator();

  public String register(String userName, String password, String confirmPassword) {
    String validationMessage = validator.validateField("Username", userName, "^[a-zA-Z]$");
    validationMessage += validator.validateField("Password", password, "^[a-zA-z0-9]$");
    validationMessage += validator.matchPassword(password, confirmPassword);
    User user = userRepository.findByName(userName);
    if ("".equals(validationMessage)) {
      try {
        return "username taken " + user.name;
      } catch (NullPointerException e) {
        userRepository.register(new User(userName, password));
        return null;
      }
    } else {
      return validationMessage;
    }
  }
}
