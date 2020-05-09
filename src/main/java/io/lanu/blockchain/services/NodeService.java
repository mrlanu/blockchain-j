package io.lanu.blockchain.services;

import io.lanu.blockchain.entities.Block;
import io.lanu.blockchain.entities.Transaction;
import io.lanu.blockchain.util.Wallet;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class NodeService {
    private final List<Block> chain = new ArrayList<>();
    private final List<Transaction> openTransactionsList = new ArrayList<>();
    private double reward = 10;
    private final String OWNER = "Serhiy";
    private Wallet wallet;

    private NodeService(Wallet wallet) {
        this.wallet = wallet;
        chain.add(new Block(1L, "", new ArrayList<>()));
    }

    public static NodeService ignite() {
        return new NodeService(null);
    }

    public void addTransaction(){
        TransactionValue value = getTransactionValue();
        Transaction transaction = new Transaction(OWNER, value.recipient, value.amount, "");
        if (verifyTransaction(transaction)){
            openTransactionsList.add(transaction);
        }else System.out.println("Not enough money.");
    }

    public void printOpenTransactions(){
        openTransactionsList.forEach(System.out::println);
    }

    public void mineBlock() {
        Block prevBlock = chain.get(chain.size() - 1);
        String previousHash = hashBlock(prevBlock);
        List<Transaction> copyTransactions = new ArrayList<>(openTransactionsList);
        copyTransactions.add(new Transaction("MINING", OWNER, reward, ""));
        Block block = new Block(chain.size() + 1, previousHash, copyTransactions);
        block.setHash(hashBlock(block));
        //proofOfWork(block);
        chain.add(block);
        chain.forEach(System.out::println);
        openTransactionsList.clear();
        //writeChainToFile();
        //System.out.println("Block has been mined.");
    }

    public double getBalance(String participant){
        double openTransactionsSpent = openTransactionsList.stream()
                .filter(t -> t.getSender().equals(participant))
                .mapToDouble(t -> -t.getAmount())
                .sum();
        double storedTransactionsBalance = chain.stream()
                .flatMap(t -> t.getTransactionList()
                        .stream())
                        .filter(t -> t.getSender().equals(participant) || t.getRecipient().equals(participant))
                        .mapToDouble(t -> t.getRecipient().equals(participant) ? t.getAmount() : -t.getAmount())
                        .sum();
        return storedTransactionsBalance + openTransactionsSpent;
    }

    private boolean verifyTransaction(Transaction transaction){
        return getBalance(transaction.getSender()) >= transaction.getAmount();
    }

    public Boolean verifyChain() {
        Block currentBlock;
        Block previousBlock;

        //loop through the chain to check hashes:
        for(int i = 1; i < chain.size(); i++) {
            currentBlock = chain.get(i);
            previousBlock = chain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.getPreviousHash().equals(hashBlock(previousBlock))){
                return false;
            }
        }
        return true;
    }

    public void hackChain(){
        chain.get(1).setMagicNumber(5);
    }

    private String hashBlock(Block block){
        return DigestUtils.sha256Hex(block.getId() + block.getPreviousHash() +
                block.getTimeStamp() + block.getTransactionList() + block.getMagicNumber());
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
