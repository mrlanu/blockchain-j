package io.lanu.blockchain;

import io.lanu.blockchain.util.Wallet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Node {
    public static void main(String[] args) throws Exception {
        Wallet gk;
        try {
            gk = new Wallet();
            gk.createKeys(1024, "RSA");
            gk.writeKeyToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
            gk.writeKeyToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
            gk.loadKeyPair("RSA");
            gk.printKeys();
        } catch (NoSuchAlgorithmException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
