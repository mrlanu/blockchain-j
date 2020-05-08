package io.lanu.blockchain.util;

import io.lanu.blockchain.Transaction;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;



public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public void createKeys(int keyLength, String algorithm) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(keyLength);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void writeKeyToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public void loadKeyPair(String algorithm) throws Exception {
        loadPrivate("KeyPair/privateKey", algorithm);
        loadPublic("KeyPair/publicKey", algorithm);
    }

    public void printKeys() throws DecoderException {
        System.out.println("Public key - " + Hex.encodeHexString(getPublicKey().getEncoded()));
        System.out.println("Private key - " + Hex.encodeHexString(getPrivateKey().getEncoded()));
        // Hex.decodeHex(string.toCharArray());
    }

    public Transaction signTransaction(Transaction transaction) throws Exception{
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(getPrivateKey());
        rsa.update(transaction.getHash().getBytes());
        byte[] signature = rsa.sign();
        transaction.setSignature(Hex.encodeHexString(signature));
        return transaction;
    }

    private void loadPrivate(String filename, String algorithm) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        privateKey = kf.generatePrivate(spec);
    }

    private void loadPublic(String filename, String algorithm) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        publicKey = kf.generatePublic(spec);
    }

}
