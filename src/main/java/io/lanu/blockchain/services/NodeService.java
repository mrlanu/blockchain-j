package io.lanu.blockchain.services;

import io.lanu.blockchain.entities.Block;
import io.lanu.blockchain.entities.Transaction;
import io.lanu.blockchain.util.Wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.lanu.blockchain.util.Util.*;

public final class NodeService {
    private List<Block> chain = new ArrayList<>();
    private List<Transaction> openTransactionsList = new ArrayList<>();
    private double reward = 10;
    private Wallet wallet;
    private static int DIFFICULTY = 4;

    private NodeService() {
        init();
    }

    public void createWallet() {
        wallet = new Wallet();
        try {
            wallet.createKeys(1024, "RSA");
            wallet.writeKeyToFile("KeyPair/publicKey", wallet.getPublicKey().getEncoded());
            wallet.writeKeyToFile("KeyPair/privateKey", wallet.getPrivateKey().getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadWallet(){
        wallet = new Wallet();
        try {
            wallet.loadKeyPair("RSA");
            //wallet.printKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(){
        chain = readChainFromFile();
        if (chain.size() == 0){
            createGenesisBlock();
        }
        openTransactionsList = readOpenTxFromFile();
    }

    public static NodeService ignite() {
        return new NodeService();
    }

    private void createGenesisBlock() {
        Block genesisBlock = new Block(1L, "none", new ArrayList<>());
        proofOfWork(genesisBlock);
        chain.add(genesisBlock);
        writeChainToFile(chain);
    }

    public boolean addTransaction(){
        TransactionValue value = getTransactionValue();
        String signature = wallet.signTransaction(wallet.getPublicKeyAsString(), value.recipient, value.amount);
        Transaction transaction = new Transaction(wallet.getPublicKeyAsString(), value.recipient, value.amount, signature);
        if (verifyTransaction(transaction)){
            openTransactionsList.add(transaction);
            writeOpenTxToFile(openTransactionsList);
            return true;
        }
        return false;
    }

    private boolean verifyTransaction(Transaction transaction){
        return getBalance() >= transaction.getAmount() && wallet.verifyTransaction(transaction);
    }

    public double getBalance(){
        String participant = wallet.getPublicKeyAsString();
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

    public boolean mineBlock() {
        Block prevBlock = chain.get(chain.size() - 1);
        String previousHash = hashBlock(prevBlock);
        for (Transaction tr : openTransactionsList) {
            if (!wallet.verifyTransaction(tr)){
                return false;
            }
        }
        List<Transaction> copyTransactions = new ArrayList<>(openTransactionsList);
        copyTransactions.add(new Transaction("MINING", wallet.getPublicKeyAsString(), reward, ""));
        Block block = new Block(chain.size() + 1, previousHash, copyTransactions);
        proofOfWork(block);
        chain.add(block);
        writeChainToFile(chain);
        openTransactionsList.clear();
        writeOpenTxToFile(openTransactionsList);
        return true;
    }

    private void proofOfWork(Block block){
        String target = new String(new char[DIFFICULTY]).replace('\0', '0');
        Random random = new Random();
        while(!hashBlock(block).substring(0, DIFFICULTY).equals(target)) {
            block.setNonce(random.nextLong());
        }
    }

    public Boolean verifyChain() {
        //loop through the chain to check hashes:
        for(int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.getPreviousHash().equals(hashBlock(previousBlock))){
                return false;
            }
        }
        return true;
    }

    public void printChain(){
        System.out.println();
        chain.forEach(b -> System.out.println(b + "\n"));
    }

    public void printOpenTransactions(){
        openTransactionsList.forEach(System.out::println);
    }

    public Wallet getWallet() {
        return wallet;
    }

    public static class TransactionValue{
        private String recipient;
        private double amount;

        public TransactionValue(String recipient, double amount) {
            this.recipient = recipient;
            this.amount = amount;
        }
    }
}
