package com.clouway.bank.core;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class AccountRecord {
  public final Long date;
  public final String userId;
  public final String operation;
  public final Double amount;

  public AccountRecord(Long date, String userId, String operation, Double amount) {
    this.date = date;
    this.userId = userId;
    this.operation = operation;
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AccountRecord that = (AccountRecord) o;

    if (date != null ? !date.equals(that.date) : that.date != null) return false;
    if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
    if (operation != null ? !operation.equals(that.operation) : that.operation != null) return false;
    return !(amount != null ? !amount.equals(that.amount) : that.amount != null);

  }

  @Override
  public int hashCode() {
    int result = date != null ? date.hashCode() : 0;
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    result = 31 * result + (operation != null ? operation.hashCode() : 0);
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    return result;
  }
}
