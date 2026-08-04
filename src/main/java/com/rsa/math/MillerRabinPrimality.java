package com.rsa.math;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MillerRabinPrimality {

    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger THREE = BigInteger.valueOf(3);

    public static boolean isProbablePrime(BigInteger n, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }
        if (n.compareTo(TWO) < 0) {
            return false;
        }
        if (n.equals(TWO)) {
            return true;
        }
        if (!n.testBit(0)) {
            return false;
        }
        if (n.equals(THREE)) {
            return true;
        }

        BigInteger nMinusOne = n.subtract(BigInteger.ONE);
        int s = nMinusOne.getLowestSetBit();
        BigInteger d = nMinusOne.shiftRight(s);

        SecureRandom rng = new SecureRandom();
        BigInteger a;
        for (int round = 0; round < k; round++) {
            do {
                a = new BigInteger(n.bitLength(), rng);
            } while (a.compareTo(TWO) < 0 || a.compareTo(nMinusOne) >= 0);

            BigInteger x = ModularExponentiation.modPow(a, d, n);
            if (x.equals(BigInteger.ONE) || x.equals(nMinusOne)) {
                continue;
            }
            boolean composite = true;
            for (int r = 1; r < s; r++) {
                x = x.multiply(x).mod(n);
                if (x.equals(nMinusOne)) {
                    composite = false;
                    break;
                }
            }
            if (composite) {
                return false;
            }
        }
        return true;
    }

    public static double errorBound(int k) {
        return Math.pow(4.0, -k);
    }
}
