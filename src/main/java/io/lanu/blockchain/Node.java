package io.lanu.blockchain;

import io.lanu.blockchain.services.NodeService;
import io.lanu.blockchain.util.Wallet;

import static io.lanu.blockchain.services.NodeService.ignite;


public class Node {

    private static class SingletonHolder {
        private static final NodeService INSTANCE = ignite();
    }

    private static NodeService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static boolean addTransaction(){
        return getInstance().addTransaction();
    }

    public static void printOpenTransactions(){
        getInstance().printOpenTransactions();
    }

    public static void printChain(){
        getInstance().printChain();
    }

    public static boolean mineNewBlock(){
        return getInstance().mineBlock();
    }

    public static boolean verifyChain(){
        return getInstance().verifyChain();
    }

    public static double getBalance(){
        return getInstance().getBalance();
    }

    public static void createWallet(){getInstance().createWallet();}

    public static void loadWallet(){getInstance().loadWallet();}

    public static Wallet getWallet(){return getInstance().getWallet();}

}
