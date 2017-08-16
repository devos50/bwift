package models;

public class BankAccount {
    /*
    Representation of a bank account.
     */
    private String address;
    private double balance;

    BankAccount(String address) {
        this.address = address;
        this.balance = 1000.0; // initial balance
    }

    public void add(int toAdd) {
        assert toAdd > 0;
        this.balance += toAdd;
    }

    public void subtract(int toSub) {
        assert this.balance - toSub >= 0;
        this.balance -= toSub;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getAddress() {
        return this.address;
    }

}
