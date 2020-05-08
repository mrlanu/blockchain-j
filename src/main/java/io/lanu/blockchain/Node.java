package io.lanu.blockchain;

import io.lanu.blockchain.services.NodeService;
import static io.lanu.blockchain.services.NodeService.ignite;


public class Node {

    private static class SingletonHolder {
        private static final NodeService INSTANCE = ignite();
    }

    private static NodeService getInstance() {
        return Node.SingletonHolder.INSTANCE;
    }

    public static void addTransaction(){
        getInstance().addTransaction();
    }

    public static void printOpenTransactions(){
        getInstance().printOpenTransactions();
    }

}
