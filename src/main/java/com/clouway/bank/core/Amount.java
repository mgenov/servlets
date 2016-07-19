package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Amount {

  public final String userId;
  public final Double value;

  public Amount(String userId, Double value) {
    this.userId = userId;
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Amount amount = (Amount) o;

    if (userId != null ? !userId.equals(amount.userId) : amount.userId != null) return false;
    return !(value != null ? !value.equals(amount.value) : amount.value != null);

  }

  @Override
  public int hashCode() {
    int result = userId != null ? userId.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }
}
