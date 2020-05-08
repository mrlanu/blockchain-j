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
                addTransaction();
                printOpenTransactions();
            }
            if (choice.equals("2")){
                mineNewBlock();
            }
            if (choice.equals("h")){
                hackChain();
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
        System.out.println("Please choose.");
        System.out.println("1 - Add new Transaction");
        System.out.println("2 - Mine new Block");
        System.out.println("h - hack chain");
        System.out.println("q - quit");
    }
}
