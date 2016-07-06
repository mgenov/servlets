package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Amount {

  public final String username;
  public final Double value;

  public Amount(String username, Double value) {
    this.username = username;
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Amount amount = (Amount) o;

    if (username != null ? !username.equals(amount.username) : amount.username != null) return false;
    return !(value != null ? !value.equals(amount.value) : amount.value != null);

  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }
}
