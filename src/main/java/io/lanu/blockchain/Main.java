package io.lanu.blockchain;

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

        var node = new Node();

        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.println();
            System.out.println("Please choose.");
            System.out.println("1 - Add new Transaction");
            System.out.println("q - quit");
            String choice = scanner.nextLine();
            //clear console
            /*System.out.print("\033[H\033[2J");
            System.out.flush();*/
            if (choice.equals("1")){
                node.addTransaction();
                node.printOpenTransactions();
            }
            if (choice.equals("q")){
                scanner.close();
                break;
            }
        }
    }
}
