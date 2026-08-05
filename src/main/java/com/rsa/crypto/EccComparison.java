package com.rsa.crypto;

import com.rsa.math.RsaKeyGenerator;
import com.rsa.math.ModularExponentiation;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class EccComparison {

    public static class BenchmarkResult {
        public final String algorithm;
        public final int keySizeBits;
        public final long keyGenTimeMs;
        public final long encryptTimeMs;
        public final long decryptTimeMs;
        public final long signTimeMs;
        public final long verifyTimeMs;
        public final int iterations;

        public BenchmarkResult(String algorithm, int keySizeBits, long keyGenTimeMs,
                               long encryptTimeMs, long decryptTimeMs,
                               long signTimeMs, long verifyTimeMs, int iterations) {
            this.algorithm = algorithm;
            this.keySizeBits = keySizeBits;
            this.keyGenTimeMs = keyGenTimeMs;
            this.encryptTimeMs = encryptTimeMs;
            this.decryptTimeMs = decryptTimeMs;
            this.signTimeMs = signTimeMs;
            this.verifyTimeMs = verifyTimeMs;
            this.iterations = iterations;
        }

        @Override
        public String toString() {
            return String.format("%-12s | %5d-bit | KeyGen: %5dms | Enc: %5dms | Dec: %5dms | Sign: %5dms | Verify: %5dms (%d iter)",
                    algorithm, keySizeBits, keyGenTimeMs, encryptTimeMs, decryptTimeMs,
                    signTimeMs, verifyTimeMs, iterations);
        }
    }

    private static final SecureRandom RNG = new SecureRandom();
    private static final String TEST_MESSAGE = "Benchmark test message for performance comparison.";

    public static BenchmarkResult benchmarkRSA(int keySizeBits, int iterations) {
        long totalKeyGen = 0;
        long totalEncrypt = 0;
        long totalDecrypt = 0;
        long totalSign = 0;
        long totalVerify = 0;

        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(keySizeBits);
            totalKeyGen += System.nanoTime() - start;

            RsaEngine engine = new RsaEngine(keyPair);
            DigitalSignature ds = new DigitalSignature(keyPair);
            BigInteger message = new BigInteger(1, TEST_MESSAGE.getBytes());

            start = System.nanoTime();
            BigInteger encrypted = engine.encrypt(message);
            totalEncrypt += System.nanoTime() - start;

            start = System.nanoTime();
            BigInteger decrypted = engine.decrypt(encrypted);
            totalDecrypt += System.nanoTime() - start;

            start = System.nanoTime();
            BigInteger signature = ds.signText(TEST_MESSAGE);
            totalSign += System.nanoTime() - start;

            start = System.nanoTime();
            boolean verified = ds.verifyText(TEST_MESSAGE, signature);
            totalVerify += System.nanoTime() - start;

            if (!verified) {
                throw new RuntimeException("RSA verification failed during benchmark");
            }
        }

        return new BenchmarkResult("RSA", keySizeBits,
                totalKeyGen / 1_000_000 / iterations,
                totalEncrypt / 1_000_000 / iterations,
                totalDecrypt / 1_000_000 / iterations,
                totalSign / 1_000_000 / iterations,
                totalVerify / 1_000_000 / iterations,
                iterations);
    }

    public static BenchmarkResult benchmarkECC(int keySizeBits, int iterations) {
        long totalKeyGen = 0;
        long totalSign = 0;
        long totalVerify = 0;

        String curve;
        switch (keySizeBits) {
            case 160: curve = "secp160r1"; break;
            case 192: curve = "secp192r1"; break;
            case 224: curve = "secp224r1"; break;
            case 256: curve = "secp256r1"; break;
            case 384: curve = "secp384r1"; break;
            case 521: curve = "secp521r1"; break;
            default: curve = "secp256r1";
        }

        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            KeyPairGenerator keyGen;
            try {
                keyGen = KeyPairGenerator.getInstance("EC");
                keyGen.initialize(keySizeBits, RNG);
            } catch (Exception e) {
                throw new RuntimeException("ECC curve not available: " + curve, e);
            }
            KeyPair keyPair = keyGen.generateKeyPair();
            totalKeyGen += System.nanoTime() - start;

            try {
                java.security.Signature ecdsa = java.security.Signature.getInstance("SHA256withECDSA");
                
                start = System.nanoTime();
                ecdsa.initSign(keyPair.getPrivate());
                ecdsa.update(TEST_MESSAGE.getBytes());
                byte[] signature = ecdsa.sign();
                totalSign += System.nanoTime() - start;

                start = System.nanoTime();
                ecdsa.initVerify(keyPair.getPublic());
                ecdsa.update(TEST_MESSAGE.getBytes());
                boolean verified = ecdsa.verify(signature);
                totalVerify += System.nanoTime() - start;

                if (!verified) {
                    throw new RuntimeException("ECDSA verification failed during benchmark");
                }
            } catch (Exception e) {
                throw new RuntimeException("ECDSA operation failed", e);
            }
        }

        return new BenchmarkResult("ECDSA", keySizeBits,
                totalKeyGen / 1_000_000 / iterations,
                -1, -1,
                totalSign / 1_000_000 / iterations,
                totalVerify / 1_000_000 / iterations,
                iterations);
    }

    public static List<BenchmarkResult> runComparison(int iterations) {
        List<BenchmarkResult> results = new ArrayList<>();

        int[] rsaSizes = {512, 1024, 2048};
        for (int size : rsaSizes) {
            System.out.println("Benchmarking RSA-" + size + "...");
            results.add(benchmarkRSA(size, iterations));
        }

        int[] eccSizes = {160, 192, 224, 256, 384, 521};
        for (int size : eccSizes) {
            System.out.println("Benchmarking ECC-" + size + "...");
            try {
                results.add(benchmarkECC(size, iterations));
            } catch (Exception e) {
                System.out.println("  Skipping ECC-" + size + ": " + e.getMessage());
            }
        }

        return results;
    }

    public static void printComparisonTable(List<BenchmarkResult> results) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("RSA vs ECC Performance Comparison");
        System.out.println("=".repeat(120));
        System.out.printf("%-12s | %7s | %12s | %9s | %9s | %9s | %11s | %s%n",
                "Algorithm", "KeySize", "KeyGen (ms)", "Enc (ms)", "Dec (ms)", "Sign (ms)", "Verify (ms)", "Iterations");
        System.out.println("-".repeat(120));

        for (BenchmarkResult r : results) {
            String enc = r.encryptTimeMs >= 0 ? String.valueOf(r.encryptTimeMs) : "N/A";
            String dec = r.decryptTimeMs >= 0 ? String.valueOf(r.decryptTimeMs) : "N/A";
            System.out.printf("%-12s | %7d | %12d | %9s | %9s | %9d | %11d | %d%n",
                    r.algorithm, r.keySizeBits, r.keyGenTimeMs, enc, dec, r.signTimeMs, r.verifyTimeMs, r.iterations);
        }

        System.out.println("=".repeat(120));
        System.out.println("\nEquivalent Security Levels (NIST SP 800-57):");
        System.out.println("RSA-1024  ~ ECC-160  (80-bit security)  - DEPRECATED");
        System.out.println("RSA-2048  ~ ECC-224  (112-bit security)");
        System.out.println("RSA-3072  ~ ECC-256  (128-bit security)");
        System.out.println("RSA-7680  ~ ECC-384  (192-bit security)");
        System.out.println("RSA-15360 ~ ECC-521  (256-bit security)");
    }

    public static void main(String[] args) {
        int iterations = args.length > 0 ? Integer.parseInt(args[0]) : 5;
        System.out.println("Running RSA vs ECC comparison with " + iterations + " iterations per test...");
        
        List<BenchmarkResult> results = runComparison(iterations);
        printComparisonTable(results);
    }
}