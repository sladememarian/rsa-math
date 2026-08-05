package com.rsa.crypto;

import com.rsa.math.ModularExponentiation;
import com.rsa.math.RsaKeyGenerator;
import com.rsa.math.TextEncoder;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class DigitalSignature {

    private final BigInteger n;
    private final BigInteger e;
    private final BigInteger d;

    public DigitalSignature(RsaKeyGenerator.RsaKeyPair keyPair) {
        this.n = keyPair.n;
        this.e = keyPair.e;
        this.d = keyPair.d;
    }

    public DigitalSignature(BigInteger n, BigInteger e, BigInteger d) {
        this.n = n;
        this.e = e;
        this.d = d;
    }

    private static BigInteger hashToBigInteger(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return new BigInteger(1, hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("SHA-256 not available", ex);
        }
    }

    public BigInteger sign(BigInteger message) {
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
        if (message.compareTo(n) >= 0) {
            throw new IllegalArgumentException("message must be less than modulus n");
        }
        return ModularExponentiation.modPow(message, d, n);
    }

    public boolean verify(BigInteger message, BigInteger signature) {
        if (message == null || signature == null) {
            throw new IllegalArgumentException("message and signature cannot be null");
        }
        if (message.compareTo(n) >= 0 || signature.compareTo(n) >= 0) {
            throw new IllegalArgumentException("message and signature must be less than modulus n");
        }
        BigInteger verified = ModularExponentiation.modPow(signature, e, n);
        return verified.equals(message);
    }

    public BigInteger signText(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("text cannot be null or empty");
        }
        byte[] data = text.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        BigInteger hash = hashToBigInteger(data);
        return sign(hash);
    }

    public boolean verifyText(String text, BigInteger signature) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("text cannot be null or empty");
        }
        byte[] data = text.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        BigInteger hash = hashToBigInteger(data);
        return verify(hash, signature);
    }

    public List<BigInteger> signTextBlocks(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("text cannot be null or empty");
        }
        List<BigInteger> blocks = TextEncoder.encodeBlocks(text, n.bitLength());
        return blocks.stream()
            .map(this::sign)
            .toList();
    }

    public boolean verifyTextBlocks(String text, List<BigInteger> signatures) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("text cannot be null or empty");
        }
        if (signatures == null || signatures.isEmpty()) {
            throw new IllegalArgumentException("signatures cannot be null or empty");
        }
        List<BigInteger> blocks = TextEncoder.encodeBlocks(text, n.bitLength());
        if (blocks.size() != signatures.size()) {
            return false;
        }
        for (int i = 0; i < blocks.size(); i++) {
            if (!verify(blocks.get(i), signatures.get(i))) {
                return false;
            }
        }
        return true;
    }

    public BigInteger getModulus() {
        return n;
    }

    public BigInteger getPublicExponent() {
        return e;
    }

    public BigInteger getPrivateExponent() {
        return d;
    }
}