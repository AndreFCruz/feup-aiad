package utils;

import agents.GenericAgent;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    class Transaction {
        float amount;
        Wallet payer;
        Wallet receiver;

        Transaction(float amount, Wallet payer, Wallet receiver) {
            this.amount = amount;
            this.payer = payer;
            this.receiver = receiver;
        }
    }

    private GenericAgent owner;
    private float balance;
    private List<Transaction> transactions;

    public Wallet(GenericAgent owner, float balance) {
        this.owner = owner;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public Wallet(GenericAgent owner) {
        this(owner, 0);
    }

    public float getBalance() {
        return this.balance;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void pay(float amount, Wallet target) {
        this.balance -= amount;
        target.balance += amount;

        Transaction t = new Transaction(amount, this, target);
        this.transactions.add(t);
        target.transactions.add(t);
    }

    public void credit(float amount, Wallet target) {
        this.balance += amount;
        target.balance -= amount;

        Transaction t = new Transaction(amount, target, this);
        this.transactions.add(t);
        target.transactions.add(t);
    }

}
