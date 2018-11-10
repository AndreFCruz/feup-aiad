package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Wallet implements Serializable {
    class Transaction implements Serializable {
        float amount;
        Wallet payer;
        Wallet receiver;

        Transaction(float amount, Wallet payer, Wallet receiver) {
            this.amount = amount;
            this.payer = payer;
            this.receiver = receiver;

//            System.out.println(this.toString());
        }


        @Override
        public String toString() {
            if (payer == null) {
                return "INJECT " + amount + " to " + receiver.toString() + "\t(" + receiver.walletType;
            } else if (receiver == null) {
                return "CONSUME " + amount + " from " + payer.toString() + "\t(" + payer.walletType;
            }

            return "Transaction (" + payer.walletType + "): "
                    + payer.toString() + " to "
                    + receiver.toString()
                    + ". \t\tValue: " + amount
                    + ". \t\tPayer balance:\t" + payer.getBalance()
                    + ". \t\tReceiver balance:\t" + receiver.getBalance();
        }
    }

    public enum WalletType {
        CURRENCY, ENERGY
    }

    private float balance;
    private List<Transaction> transactions;
    private WalletType walletType;

    public Wallet(WalletType walletType, float balance) {
        this.walletType = walletType;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public Wallet(WalletType walletType) {
        this(walletType, 0);
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
     *
     * @param amount to be injected.
     */
    public void inject(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        this.balance += amount;
        Transaction t = new Transaction(amount, null, this);
        this.transactions.add(t);
    }

    /**
     * Method for consuming units from this wallet's balance.
     * Useful for consumers, that consume energy daily.
     *
     * @param amount to be consumed.
     */
    public void consume(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException();
        }
        this.balance -= amount;
        Transaction t = new Transaction(-1 * amount, this, null);
        this.transactions.add(t);
    }

}
