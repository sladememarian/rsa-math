package com.rsa.math;

import java.math.BigInteger;

public class EuclideanMath {

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        a = a.abs();
        b = b.abs();
        while (!b.equals(BigInteger.ZERO)) {
            BigInteger remainder = a.mod(b);
            a = b;
            b = remainder;
        }
        return a;
    }

    public static BigInteger[] extendedGcd(BigInteger a, BigInteger b) {
        BigInteger x0 = BigInteger.ONE, x1 = BigInteger.ZERO;
        BigInteger y0 = BigInteger.ZERO, y1 = BigInteger.ONE;
        while (!b.equals(BigInteger.ZERO)) {
            BigInteger[] qr = a.divideAndRemainder(b);
            BigInteger q = qr[0];
            BigInteger nextA = b;
            BigInteger nextB = qr[1];

            BigInteger nextX = x0.subtract(q.multiply(x1));
            BigInteger nextY = y0.subtract(q.multiply(y1));

            a = nextA;
            b = nextB;
            x0 = x1;
            x1 = nextX;
            y0 = y1;
            y1 = nextY;
        }
        return new BigInteger[] { a, x0, y0 };
    }

    public static BigInteger modInverse(BigInteger a, BigInteger m) {
        if (m.signum() <= 0) {
            throw new IllegalArgumentException("modulus must be positive");
        }
        BigInteger[] result = extendedGcd(a, m);
        BigInteger g = result[0];
        BigInteger x = result[1];
        if (!g.equals(BigInteger.ONE)) {
            throw new ArithmeticException("no modular inverse: gcd(a, m) != 1");
        }
        return x.mod(m);
    }
}
