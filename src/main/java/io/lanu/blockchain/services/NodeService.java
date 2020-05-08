package io.lanu.blockchain.services;

import io.lanu.blockchain.entities.Block;
import io.lanu.blockchain.entities.Transaction;
import io.lanu.blockchain.util.Wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class NodeService {
    private final List<Block> chain = new ArrayList<>();
    private final List<Transaction> openTransactionsList = new ArrayList<>();
    private Wallet wallet;

    private NodeService(Wallet wallet) {
        this.wallet = wallet;
    }

    public static NodeService ignite() {
        return new NodeService(null);
    }

    public void addTransaction(){
        TransactionValue value = getTransactionValue();
        Transaction transaction = new Transaction("Serhiy", value.recipient, value.amount, "");
        openTransactionsList.add(transaction);
    }

    public void printOpenTransactions(){
        openTransactionsList.forEach(System.out::println);
    }

    private TransactionValue getTransactionValue(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Recipient - ");
        String recipient = scanner.nextLine();
        System.out.println("Amount - ");
        double amount = scanner.nextDouble();
        return new TransactionValue(recipient, amount);
    }


    private static class TransactionValue{
        private String recipient;
        private double amount;

        public TransactionValue(String recipient, double amount) {
            this.recipient = recipient;
            this.amount = amount;
        }
    }
}