package io.lanu.blockchain.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class Wallet {
    private final String PATH_PRIVATE = "KeyPair/privateKey";
    private final String PATH_PUBLIC = "KeyPair/publicKey";
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public void createKeys(int keyLength, String algorithm) throws NoSuchAlgorithmException {
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
        loadPrivate(PATH_PRIVATE, algorithm);
        loadPublic(PATH_PUBLIC, algorithm);
    }

    public void printKeys(){
        System.out.println("Public key - " + getHexString(getPublicKey().getEncoded()));
        System.out.println("Private key - " + getHexString(getPrivateKey().getEncoded()));
    }

    //String representation
    public String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
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
