package io.lanu.blockchain;

import static io.lanu.blockchain.Node.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*Wallet gk;
        try {
            gk = new Wallet();
            gk.createKeys(1024, "RSA");
            gk.writeKeyToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
            gk.writeKeyToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
            gk.loadKeyPair("RSA");
            gk.printKeys();
        } catch (NoSuchAlgorithmException | IOException e) {
            System.err.println(e.getMessage());
        }*/

        while (true){
            Scanner scanner = new Scanner(System.in);
            printChoice();
            String choice = scanner.nextLine();
            //clear console
            /*System.out.print("\033[H\033[2J");
            System.out.flush();*/
            if (choice.equals("1")){
                if (getWallet() != null) {
                    if (addTransaction()) {
                        System.out.println("Transaction has been added.\n");
                        printOpenTransactions();
                    } else {
                        System.out.println("Not enough money.");
                    }
                }else {
                    System.out.println("\n<<< Load or create the Wallet >>>>");
                }
            }
            if (choice.equals("2")) {
                if (getWallet() != null) {
                    if (mineNewBlock()) {
                        printChain();
                    } else {
                        System.out.println("Error.");
                    }
                } else {
                    System.out.println("\n<<< Load or create the Wallet >>>");
                }
            }
            if (choice.equals("3")){
                    printChain();
            }
            if (choice.equals("4")){
                printOpenTransactions();
            }
            if (choice.equals("5")){
                createWallet();
            }
            if (choice.equals("6")){
                loadWallet();
            }
            if (choice.equals("b")){
                if (getWallet() != null) {
                    System.out.println("Balance: " + getBalance());
                } else {
                    System.out.println("\n<<< Load or create the Wallet >>>");
                }
            }
            if (choice.equals("q")){
                scanner.close();
                break;
            }
            if (!verifyChain()){
                System.out.println("**** Chain is invalid ****");
                break;
            }
        }
    }

    private static void printChoice(){
        System.out.println();
        System.out.println("Please choose: ");
        System.out.println("\t1 - Add new Transaction");
        System.out.println("\t2 - Mine new Block");
        System.out.println("\t3 - Print the chain");
        System.out.println("\t4 - Print the open Transactions");
        System.out.println("\t5 - Create new Wallet");
        System.out.println("\t6 - Load the Wallet");
        System.out.println("\tb - Get balance");
        System.out.println("\tq - quit");
    }
}
