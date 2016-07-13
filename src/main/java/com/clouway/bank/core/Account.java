package com.clouway.bank.core;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Account {
    public final String email;
    private Double balance;

    public Account(String email, Double balance) {
        this.email = email;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }


    public void deposit(Double sum) {
        this.balance += sum;
    }

    public void withdraw(Double sum) {
        this.balance -= sum;
    }

    public Double getBalance() {
        return this.balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (email != null ? !email.equals(account.email) : account.email != null) return false;
        return balance != null ? balance.equals(account.balance) : account.balance == null;

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}