package io.lanu.blockchain.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lanu.blockchain.entities.Block;
import io.lanu.blockchain.entities.Transaction;
import io.lanu.blockchain.services.NodeService;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Util {

    private static final String CHAIN_FILE_PATH = "chain.json";
    private static final String TX_FILE_PATH = "transactions.json";

    public static NodeService.TransactionValue getTransactionValue(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Recipient :");
        String recipient = scanner.nextLine();
        System.out.println("Amount :");
        double amount = scanner.nextDouble();
        return new NodeService.TransactionValue(recipient, amount);
    }

    public static String hashBlock(Block block){
        Gson gson = new Gson();
        String json = gson.toJson(block);
        return DigestUtils.sha256Hex(json);
    }

    public static void writeChainToFile(List<Block> chain){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(CHAIN_FILE_PATH)){
            gson.toJson(chain, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeOpenTxToFile(List<Transaction> transactions){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(TX_FILE_PATH)){
            gson.toJson(transactions, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
