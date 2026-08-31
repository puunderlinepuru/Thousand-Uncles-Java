package com.thousand_uncles.discord_bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.data.models.uncletopia.AnyPercentMapRecordEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Test {
    public void main(String[] args) {

        AnyPercentMapRecordEntry testEntry = new AnyPercentMapRecordEntry(
                0,
                "name",
                BigDecimal.ONE,
                BigDecimal.ONE,
                "link",
                "link",
                "link",
                "link",
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        /*String signedMessage = "failed";
        try {
            signedMessage = RsaSignatureUtils.signMessage(testEntry.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("SignedMessage: " + signedMessage);

        try {
            boolean verdict = RsaSignatureUtils.verifySignature(testEntry.toString(), signedMessage, (RSAPublicKey) RsaSignatureUtils.publicKey);
            System.out.println(verdict);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

    }

    private static void precision(String timeString){
        double aDouble = Double.parseDouble(timeString);
        System.out.printf("%.6f%n", aDouble);
    }

    public static String NumberToString (BigDecimal timeNumber) {
        String timeString;
        BigDecimal minutes = timeNumber.divide(BigDecimal.valueOf(60),0, RoundingMode.HALF_UP);
        BigDecimal seconds = timeNumber.remainder(BigDecimal.valueOf(60)).setScale(2, RoundingMode.HALF_UP);
        if (seconds.compareTo(BigDecimal.valueOf(10)) <0){
            timeString = minutes + ":0" + seconds;
        } else {
            timeString = minutes + ":" + seconds;
        }
        return timeString;
    }

    private static void encoding(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("test", "thing");

        System.out.println("as text:" + objectNode);

        byte[] utf8Bytes = objectNode.toString().getBytes();
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : utf8Bytes) {
            hexBuilder.append(String.format("%02x", b).toUpperCase());
        }
        System.out.println("hex: " + hexBuilder);
    }

    private static void toHex(byte[] message){
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : message) {
            hexBuilder.append(String.format("%02x", b).toUpperCase());
        }
        System.out.println("hex: " + hexBuilder);
    }

    static class RsaSignatureUtils {

        // SHA-256 hash with padding, standard for RSA signatures
        private static final String SIGNATURE_ALGO = "SHA256withRSA";
        private static final String PUBLIC_KEY_FORMAT = "X.509";
        private static final String PRIVATE_KEY_FORMAT = "PKCS8";

        public static PublicKey publicKey;

        public static String signMessage(String message) throws Exception {
            String signatureStr = Base64.getEncoder().encodeToString(message.getBytes());
            byte[] signedBytes = signatureStr.getBytes();
            // Generate RSA key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

            // Create signer and sign message
            Signature sig = Signature.getInstance(SIGNATURE_ALGO);
            sig.initSign(privateKey);
            sig.update(message.getBytes("UTF-8"));
            byte[] signature = sig.sign();

            toHex(signature);

            // Convert to string for logging/storage
            return Base64.getEncoder().encodeToString(signature);
        }

        public static boolean verifySignature(String message, String signatureBase64, RSAPublicKey publicKey) throws Exception {
            byte[] signature = Base64.getDecoder().decode(signatureBase64);
            Signature sig = Signature.getInstance(SIGNATURE_ALGO);
            sig.initVerify(publicKey);
            sig.update(message.getBytes("UTF-8"));
            return sig.verify(signature);
        }

        public static PublicKey parsePublicKey(String keyString) throws Exception {
            String base64Key = keyString.trim();
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (PublicKey) keyFactory.generatePublic(spec);
        }
    }
}