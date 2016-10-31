package com.clouway.core;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class Customer {
    public final Integer id;
    public final String name;
    public final String password;
    public final Integer balance;

    public Customer(Integer id, String name, String password, Integer balance) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;

        Customer customer = (Customer) o;

        if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
        if (name != null ? !name.equals(customer.name) : customer.name != null) return false;
        if (password != null ? !password.equals(customer.password) : customer.password != null) return false;
        return balance != null ? balance.equals(customer.balance) : customer.balance == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}
