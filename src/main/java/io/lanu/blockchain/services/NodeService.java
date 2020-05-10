package io.lanu.blockchain.services;

import com.google.gson.Gson;
import io.lanu.blockchain.entities.Block;
import io.lanu.blockchain.entities.Transaction;
import io.lanu.blockchain.util.Wallet;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class NodeService {
    private final List<Block> chain = new ArrayList<>();
    private final List<Transaction> openTransactionsList = new ArrayList<>();
    private double reward = 10;
    private final String OWNER = "Serhiy";
    private Wallet wallet;
    private static int DIFFICULTY = 4;

    private NodeService(Wallet wallet) {
        this.wallet = wallet;
        chain.add(new Block(1L, "", new ArrayList<>()));
    }

    public static NodeService ignite() {
        return new NodeService(null);
    }

    public boolean addTransaction(){
        TransactionValue value = getTransactionValue();
        Transaction transaction = new Transaction(OWNER, value.recipient, value.amount, "");
        if (verifyTransaction(transaction)){
            openTransactionsList.add(transaction);
            return true;
        }
        return false;
    }

    public boolean mineBlock() {
        Block prevBlock = chain.get(chain.size() - 1);
        String previousHash = hashBlock(prevBlock);
        List<Transaction> copyTransactions = new ArrayList<>(openTransactionsList);
        copyTransactions.add(new Transaction("MINING", OWNER, reward, ""));
        Block block = new Block(chain.size() + 1, previousHash, copyTransactions);
        proofOfWork(block);
        chain.add(block);
        openTransactionsList.clear();
        //writeChainToFile();
        //System.out.println("Block has been mined.");
        return true;
    }

    private void proofOfWork(Block block){
        String target = new String(new char[DIFFICULTY]).replace('\0', '0');
        Random random = new Random();
        while(!hashBlock(block).substring(0, DIFFICULTY).equals(target)) {
            block.setNonce(random.nextLong());
        }
    }

    public void printChain(){
        System.out.println();
        chain.forEach(b -> System.out.println(b + "\n"));
    }

    public void printOpenTransactions(){
        openTransactionsList.forEach(System.out::println);
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
        chain.get(1).setNonce(5);
    }

    private String hashBlock(Block block){
        Gson gson = new Gson();
        String json = gson.toJson(block);
        return DigestUtils.sha256Hex(json);
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
