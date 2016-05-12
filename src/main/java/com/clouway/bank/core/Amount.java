package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Amount {

  public final String username;
  public final String value;

  public Amount(String username, String value) {
    this.username = username;
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Amount amount1 = (Amount) o;

    if (username != null ? !username.equals(amount1.username) : amount1.username != null) return false;
    return !(value != null ? !value.equals(amount1.value) : amount1.value != null);

  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }
}
