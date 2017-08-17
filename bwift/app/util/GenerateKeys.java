package util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class GenerateKeys {

    private static KeyPairGenerator keyGen;
    private static KeyPair pair;
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    public static void createKeys() {
        try {
            GenerateKeys.keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        GenerateKeys.keyGen.initialize(1024);

        GenerateKeys.pair = GenerateKeys.keyGen.generateKeyPair();
        GenerateKeys.privateKey = pair.getPrivate();
        GenerateKeys.publicKey = pair.getPublic();
    }

    public static PrivateKey getPrivateKey() {
        if(pair == null) { GenerateKeys.createKeys(); }
        return GenerateKeys.privateKey;
    }

    public static PublicKey getPublicKey() {
        if(pair == null) { GenerateKeys.createKeys(); }
        return GenerateKeys.publicKey;
    }
}