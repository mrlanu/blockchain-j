package io.lanu.blockchain;

import io.lanu.blockchain.services.NodeService;
import static io.lanu.blockchain.services.NodeService.ignite;


public class Node {

    private static class SingletonHolder {
        private static final NodeService INSTANCE = ignite();
    }

    private static NodeService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void addTransaction(){
        getInstance().addTransaction();
    }

    public static void printOpenTransactions(){
        getInstance().printOpenTransactions();
    }

    public static void mineNewBlock(){
        getInstance().mineBlock();
    }

    public static boolean verifyChain(){
        return getInstance().verifyChain();
    }

    public static double getBalance(String participant){
        return getInstance().getBalance(participant);
    }

    public static void hackChain(){
        getInstance().hackChain();
    }

}
