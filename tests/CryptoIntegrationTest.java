package tests;

import com.rsa.math.RsaKeyGenerator;
import com.rsa.math.TextEncoder;
import com.rsa.crypto.RsaEngine;
import com.rsa.crypto.DigitalSignature;
import com.rsa.crypto.WienerAttack;
import com.rsa.crypto.EccComparison;
import java.math.BigInteger;
import java.util.List;

public class CryptoIntegrationTest {

    public static void main(String[] args) {
        System.out.println("=== Crypto Integration Test Suite ===\n");
        
        int passed = 0;
        int failed = 0;

        // Test 1: Key Generation
        System.out.println("Test 1: RSA Key Generation (1024-bit)");
        try {
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(1024);
            assert keyPair.n.bitLength() == 1024 : "Key size mismatch";
            assert keyPair.e.equals(BigInteger.valueOf(65537)) : "Public exponent mismatch";
            assert keyPair.d.compareTo(BigInteger.ZERO) > 0 : "Private exponent not positive";
            assert keyPair.p.compareTo(BigInteger.ZERO) > 0 && keyPair.q.compareTo(BigInteger.ZERO) > 0 : "Primes not positive";
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 2: RSA Encryption/Decryption - Numbers
        System.out.println("\nTest 2: RSA Encryption/Decryption (Numbers)");
        try {
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(1024);
            RsaEngine engine = new RsaEngine(keyPair);
            
            BigInteger plaintext = new BigInteger("123456789012345678901234567890");
            BigInteger encrypted = engine.encrypt(plaintext);
            BigInteger decrypted = engine.decrypt(encrypted);
            
            assert plaintext.equals(decrypted) : "Number decrypt mismatch";
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 3: RSA Encryption/Decryption - Text
        System.out.println("\nTest 3: RSA Encryption/Decryption (Text)");
        try {
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(1024);
            RsaEngine engine = new RsaEngine(keyPair);
            
            String plaintext = "Hello, RSA Integration Test!";
            List<BigInteger> encrypted = engine.encryptText(plaintext);
            String decrypted = engine.decryptText(encrypted);
            
            assert plaintext.equals(decrypted) : "Text decrypt mismatch";
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 4: Persian Text Encryption/Decryption
        System.out.println("\nTest 4: Persian Text Encryption/Decryption");
        try {
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(1024);
            RsaEngine engine = new RsaEngine(keyPair);
            
            String persianText = "\u0633\u0644\u0627\u0645 \u062f\u0646\u06cc\u0627! \u0627\u06cc\u0646 \u06cc\u06a9 \u062a\u0633\u062a \u0641\u0627\u0631\u0633\u06cc \u0627\u0633\u062a.";
            List<BigInteger> encrypted = engine.encryptText(persianText);
            String decrypted = engine.decryptText(encrypted);
            
            assert persianText.equals(decrypted) : "Persian text decrypt mismatch";
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 5: Digital Signature - Number Sign/Verify
        System.out.println("\nTest 5: Digital Signature (Number)");
        try {
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(1024);
            DigitalSignature ds = new DigitalSignature(keyPair);
            
            BigInteger message = new BigInteger("98765432109876543210");
            BigInteger signature = ds.sign(message);
            boolean verified = ds.verify(message, signature);
            
            assert verified : "Valid signature not verified";
            
            BigInteger wrongMessage = new BigInteger("11111111111111111111");
            boolean wrongVerified = ds.verify(wrongMessage, signature);
            assert !wrongVerified : "Invalid signature incorrectly verified";
            
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 6: Digital Signature - Text Sign/Verify
        System.out.println("\nTest 6: Digital Signature (Text SHA-256)");
        try {
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(1024);
            DigitalSignature ds = new DigitalSignature(keyPair);
            
            String message = "This is a test message for signing.";
            BigInteger signature = ds.signText(message);
            boolean verified = ds.verifyText(message, signature);
            
            assert verified : "Valid text signature not verified";
            
            boolean wrongVerified = ds.verifyText("Different message!", signature);
            assert !wrongVerified : "Invalid text signature incorrectly verified";
            
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 7: Digital Signature - Block Sign/Verify
        System.out.println("\nTest 7: Digital Signature (Block Signing)");
        try {
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(1024);
            DigitalSignature ds = new DigitalSignature(keyPair);
            
            String longMessage = "Long message for block signing. ".repeat(10);
            List<BigInteger> signatures = ds.signTextBlocks(longMessage);
            boolean verified = ds.verifyTextBlocks(longMessage, signatures);
            
            assert verified : "Block signatures not verified";
            
            String tampered = longMessage + " tampered";
            boolean wrongVerified = ds.verifyTextBlocks(tampered, signatures);
            assert !wrongVerified : "Tampered message incorrectly verified";
            
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 8: Wiener Attack - Vulnerable Key
        System.out.println("\nTest 8: Wiener Attack (Vulnerable Key)");
        try {
            // Create a vulnerable key with small d
            BigInteger p = new BigInteger("10007");
            BigInteger q = new BigInteger("10009");
            BigInteger n = p.multiply(q);
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            
            BigInteger d = new BigInteger("11");
            BigInteger e = com.rsa.math.EuclideanMath.modInverse(d, phi);
            
            assert WienerAttack.isVulnerable(d, n) : "Key should be vulnerable";
            
            BigInteger recoveredD = WienerAttack.solve(e, n);
            assert recoveredD != null && recoveredD.equals(d) : "Wiener attack failed to recover d";
            
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 9: Wiener Attack - Non-Vulnerable Key
        System.out.println("\nTest 9: Wiener Attack (Non-Vulnerable Key)");
        try {
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(1024);
            
            assert !WienerAttack.isVulnerable(keyPair.d, keyPair.n) : "Standard key should not be vulnerable";
            
            BigInteger recoveredD = WienerAttack.solve(keyPair.e, keyPair.n);
            assert recoveredD == null : "Wiener attack incorrectly succeeded on secure key";
            
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 10: TextEncoder - Block Encoding/Decoding
        System.out.println("\nTest 10: TextEncoder Block Encoding/Decoding");
        try {
            String text = "Test message for block encoding with special chars: !@#$%^&*()";
            List<BigInteger> blocks = TextEncoder.encodeBlocks(text, 1024);
            String decoded = TextEncoder.decodeBlocks(blocks);
            
            assert text.equals(decoded) : "Block encode/decode mismatch";
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 11: ECC Comparison Benchmark
        System.out.println("\nTest 11: ECC Comparison Benchmark (Smoke Test)");
        try {
            // Quick benchmark with 1 iteration to verify it runs
            EccComparison.BenchmarkResult rsaResult = EccComparison.benchmarkRSA(512, 1);
            EccComparison.BenchmarkResult eccResult = EccComparison.benchmarkECC(256, 1);
            
            assert rsaResult.keyGenTimeMs > 0 : "RSA keygen time not recorded";
            assert eccResult.keyGenTimeMs > 0 : "ECC keygen time not recorded";
            assert rsaResult.signTimeMs >= 0 : "RSA sign time not recorded";
            assert eccResult.signTimeMs >= 0 : "ECC sign time not recorded";
            
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Test 12: End-to-End Integration
        System.out.println("\nTest 12: End-to-End Integration (Encrypt -> Sign -> Verify -> Decrypt)");
        try {
            RsaKeyGenerator.RsaKeyPair keyPair = RsaKeyGenerator.generateKeyPair(1024);
            RsaEngine engine = new RsaEngine(keyPair);
            DigitalSignature ds = new DigitalSignature(keyPair);
            
            String originalMessage = "Secret message for end-to-end test.";
            
            // Encrypt
            List<BigInteger> encrypted = engine.encryptText(originalMessage);
            
            // Sign the encrypted blocks
            List<BigInteger> signatures = ds.signTextBlocks(originalMessage);
            
            // Verify signatures
            boolean verified = ds.verifyTextBlocks(originalMessage, signatures);
            assert verified : "Signatures not verified";
            
            // Decrypt
            String decrypted = engine.decryptText(encrypted);
            assert originalMessage.equals(decrypted) : "End-to-end decrypt mismatch";
            
            System.out.println("  ✓ PASSED");
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ FAILED: " + e.getMessage());
            failed++;
        }

        // Summary
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST SUMMARY");
        System.out.println("=".repeat(50));
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total:  " + (passed + failed));
        
        if (failed > 0) {
            System.exit(1);
        }
    }
}