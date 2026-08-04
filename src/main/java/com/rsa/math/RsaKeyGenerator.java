package com.rsa.math;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RsaKeyGenerator {

    public static final BigInteger PUBLIC_EXPONENT = BigInteger.valueOf(65537);

    private static final SecureRandom RNG = new SecureRandom();
    private static final int MR_ROUNDS = 50;

    public static final class RsaKeyPair {
        public final BigInteger p;
        public final BigInteger q;
        public final BigInteger n;
        public final BigInteger phi;
        public final BigInteger e;
        public final BigInteger d;

        public RsaKeyPair(BigInteger p, BigInteger q, BigInteger n, BigInteger phi, BigInteger e, BigInteger d) {
            this.p = p;
            this.q = q;
            this.n = n;
            this.phi = phi;
            this.e = e;
            this.d = d;
        }
    }

    public static BigInteger randomPrime(int bits, int rounds) {
        if (bits < 2) {
            throw new IllegalArgumentException("bits must be at least 2");
        }
        while (true) {
            BigInteger candidate = new BigInteger(bits, RNG);
            candidate = candidate.setBit(0).setBit(bits - 1);
            if (MillerRabinPrimality.isProbablePrime(candidate, rounds)) {
                return candidate;
            }
        }
    }

    public static RsaKeyPair generateKeyPair(int bits, int rounds) {
        if (bits < 4) {
            throw new IllegalArgumentException("bits must be at least 4");
        }
        int primeBits = bits / 2;
        BigInteger p;
        BigInteger q;
        BigInteger n;
        do {
            do {
                p = randomPrime(primeBits, rounds);
                q = randomPrime(primeBits, rounds);
            } while (p.equals(q));
            n = p.multiply(q);
        } while (n.bitLength() != bits);

        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger d = EuclideanMath.modInverse(PUBLIC_EXPONENT, phi);
        return new RsaKeyPair(p, q, n, phi, PUBLIC_EXPONENT, d);
    }

    public static RsaKeyPair generateKeyPair(int bits) {
        return generateKeyPair(bits, MR_ROUNDS);
    }
}
