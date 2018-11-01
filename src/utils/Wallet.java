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

    public enum WalletType {
        CURRENCY, ENERGY
    }

    private GenericAgent owner;
    private float balance;
    private List<Transaction> transactions;
    private WalletType walletType;

    public Wallet(GenericAgent owner, WalletType walletType, float balance) {
        this.owner = owner;
        this.walletType = walletType;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public Wallet(GenericAgent owner, WalletType walletType) {
        this(owner, walletType, 0);
    }

    public float getBalance() {
        return this.balance;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void withdraw(float amount, Wallet target) {
        if (amount < 0 || target.walletType != this.walletType) {
            throw new IllegalArgumentException();
        }
        this.balance -= amount;
        target.balance += amount;

        Transaction t = new Transaction(amount, this, target);
        this.transactions.add(t);
        target.transactions.add(t);
    }

    public void deposit(float amount, Wallet target) {
        if (amount < 0 || target.walletType != this.walletType) {
            throw new IllegalArgumentException();
        }
        this.balance += amount;
        target.balance -= amount;

        Transaction t = new Transaction(amount, target, this);
        this.transactions.add(t);
        target.transactions.add(t);
    }

    /**
     * Method for injections to the Wallet's balance.
     * Useful for producers, which, by producing energy, inject their energy wallet with energy units.
     * For generation
     *
     * @param amount
     */
    public void inject(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        this.balance += amount;
        Transaction t = new Transaction(amount, null, this);
        this.transactions.add(t);
    }

    public void consume(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        this.balance -= amount;
        Transaction t = new Transaction(-1 * amount, null, this);
        this.transactions.add(t);
    }

}
