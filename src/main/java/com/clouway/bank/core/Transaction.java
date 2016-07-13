package com.clouway.bank.core;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Transaction {
  public final long date;
  public final String email;
  public final String operation;
  public final double currentAmount;
  public final double processingAmount;

  public Transaction(long date, String email, String operation, Double processingAmount, double currentAmount) {
    this.date = date;
    this.email = email;
    this.operation = operation;
    this.processingAmount = processingAmount;
    this.currentAmount = currentAmount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Transaction that = (Transaction) o;

    if (date != that.date) return false;
    if (Double.compare(that.currentAmount, currentAmount) != 0) return false;
    if (email != null ? !email.equals(that.email) : that.email != null) return false;
    return operation != null ? operation.equals(that.operation) : that.operation == null;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = (int) (date ^ (date >>> 32));
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (operation != null ? operation.hashCode() : 0);
    temp = Double.doubleToLongBits(currentAmount);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "Transaction{" +
            "date=" + date +
            ", email='" + email + '\'' +
            ", operation='" + operation + '\'' +
            ", currentAmount=" + currentAmount +
            '}';
  }
}