package com.rsa.crypto;

import com.rsa.math.ModularExponentiation;
import com.rsa.math.RsaKeyGenerator;
import com.rsa.math.TextEncoder;
import java.math.BigInteger;
import java.util.List;

public class RsaEngine {

    private final RsaKeyGenerator.RsaKeyPair keyPair;

    public RsaEngine(RsaKeyGenerator.RsaKeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public RsaEngine(BigInteger n, BigInteger e, BigInteger d) {
        this.keyPair = new RsaKeyGenerator.RsaKeyPair(null, null, n, null, e, d);
    }

    public BigInteger encrypt(BigInteger plaintext) {
        if (plaintext == null) {
            throw new IllegalArgumentException("plaintext cannot be null");
        }
        if (plaintext.compareTo(keyPair.n) >= 0) {
            throw new IllegalArgumentException("plaintext must be less than modulus n");
        }
        return ModularExponentiation.modPow(plaintext, keyPair.e, keyPair.n);
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        if (ciphertext == null) {
            throw new IllegalArgumentException("ciphertext cannot be null");
        }
        if (ciphertext.compareTo(keyPair.n) >= 0) {
            throw new IllegalArgumentException("ciphertext must be less than modulus n");
        }
        return ModularExponentiation.modPow(ciphertext, keyPair.d, keyPair.n);
    }

    public List<BigInteger> encryptText(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("text cannot be null or empty");
        }
        List<BigInteger> blocks = TextEncoder.encodeBlocks(text, keyPair.n.bitLength());
        return blocks.stream()
            .map(this::encrypt)
            .toList();
    }

    public String decryptText(List<BigInteger> cipherBlocks) {
        if (cipherBlocks == null || cipherBlocks.isEmpty()) {
            throw new IllegalArgumentException("cipherBlocks cannot be null or empty");
        }
        List<BigInteger> plainBlocks = cipherBlocks.stream()
            .map(this::decrypt)
            .toList();
        return TextEncoder.decodeBlocks(plainBlocks);
    }

    public BigInteger getModulus() {
        return keyPair.n;
    }

    public BigInteger getPublicExponent() {
        return keyPair.e;
    }

    public BigInteger getPrivateExponent() {
        return keyPair.d;
    }
}