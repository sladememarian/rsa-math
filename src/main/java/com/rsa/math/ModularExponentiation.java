package com.rsa.math;

import java.math.BigInteger;

public class ModularExponentiation {

    public static BigInteger modPow(BigInteger base, BigInteger exponent, BigInteger modulus) {
        if (modulus.signum() <= 0) {
            throw new IllegalArgumentException("modulus must be positive");
        }
        BigInteger result = BigInteger.ONE.mod(modulus);
        BigInteger b = base.mod(modulus);
        BigInteger e = exponent;
        while (e.signum() > 0) {
            if (e.testBit(0)) {
                result = result.multiply(b).mod(modulus);
            }
            e = e.shiftRight(1);
            b = b.multiply(b).mod(modulus);
        }
        return result;
    }
}
