package com.rsa.crypto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WienerAttack {

    public static class ContinuedFraction {
        public final List<BigInteger> terms;

        public ContinuedFraction(List<BigInteger> terms) {
            this.terms = new ArrayList<>(terms);
        }

        public static ContinuedFraction fromRatio(BigInteger numerator, BigInteger denominator) {
            if (denominator.signum() <= 0) {
                throw new IllegalArgumentException("denominator must be positive");
            }
            List<BigInteger> terms = new ArrayList<>();
            BigInteger a = numerator;
            BigInteger b = denominator;
            while (b.signum() > 0) {
                BigInteger[] qr = a.divideAndRemainder(b);
                terms.add(qr[0]);
                a = b;
                b = qr[1];
            }
            return new ContinuedFraction(terms);
        }

        public List<BigInteger> getConvergents() {
            List<BigInteger> convergents = new ArrayList<>();
            if (terms.isEmpty()) return convergents;

            BigInteger p0 = terms.get(0);
            BigInteger p1 = BigInteger.ONE;
            BigInteger q0 = BigInteger.ONE;
            BigInteger q1 = BigInteger.ZERO;

            convergents.add(p0);

            for (int i = 1; i < terms.size(); i++) {
                BigInteger a = terms.get(i);
                BigInteger p2 = a.multiply(p1).add(p0);
                BigInteger q2 = a.multiply(q1).add(q0);

                convergents.add(p2);
                convergents.add(q2);

                p0 = p1;
                p1 = p2;
                q0 = q1;
                q1 = q2;
            }
            return convergents;
        }

        public List<BigInteger[]> getConvergentPairs() {
            List<BigInteger[]> pairs = new ArrayList<>();
            if (terms.isEmpty()) return pairs;

            BigInteger pMinus2 = BigInteger.ZERO;
            BigInteger pMinus1 = BigInteger.ONE;
            BigInteger qMinus2 = BigInteger.ONE;
            BigInteger qMinus1 = BigInteger.ZERO;

            for (int i = 0; i < terms.size(); i++) {
                BigInteger a = terms.get(i);
                BigInteger p = a.multiply(pMinus1).add(pMinus2);
                BigInteger q = a.multiply(qMinus1).add(qMinus2);

                pairs.add(new BigInteger[]{p, q});

                pMinus2 = pMinus1;
                pMinus1 = p;
                qMinus2 = qMinus1;
                qMinus1 = q;
            }
            return pairs;
        }
    }

    public static BigInteger solve(BigInteger e, BigInteger n) {
        if (e == null || n == null) {
            throw new IllegalArgumentException("e and n cannot be null");
        }
        if (e.compareTo(n) >= 0) {
            throw new IllegalArgumentException("e must be less than n");
        }

        ContinuedFraction cf = ContinuedFraction.fromRatio(e, n);
        List<BigInteger[]> convergents = cf.getConvergentPairs();

        for (BigInteger[] pair : convergents) {
            BigInteger k = pair[0];
            BigInteger d = pair[1];

            if (k.equals(BigInteger.ZERO)) continue;

            BigInteger edMinus1 = e.multiply(d).subtract(BigInteger.ONE);
            if (!edMinus1.mod(k).equals(BigInteger.ZERO)) continue;

            BigInteger phi = edMinus1.divide(k);

            BigInteger s = n.subtract(phi).add(BigInteger.ONE);
            BigInteger discriminant = s.multiply(s).subtract(BigInteger.valueOf(4).multiply(n));
            
            if (discriminant.signum() < 0) continue;

            BigInteger sqrtD = sqrt(discriminant);
            if (!sqrtD.multiply(sqrtD).equals(discriminant)) continue;

            if (!s.add(sqrtD).mod(BigInteger.TWO).equals(BigInteger.ZERO)) continue;

            BigInteger p = s.add(sqrtD).divide(BigInteger.TWO);
            BigInteger q = s.subtract(sqrtD).divide(BigInteger.TWO);

            if (p.multiply(q).equals(n)) {
                return d;
            }
        }

        return null;
    }

    private static BigInteger sqrt(BigInteger x) {
        if (x.signum() < 0) {
            throw new IllegalArgumentException("negative input");
        }
        if (x.equals(BigInteger.ZERO)) return BigInteger.ZERO;

        int bitLen = x.bitLength();
        BigInteger root = BigInteger.ONE.shiftLeft((bitLen + 1) / 2);

        while (true) {
            BigInteger next = root.add(x.divide(root)).shiftRight(1);
            if (next.equals(root) || next.equals(root.subtract(BigInteger.ONE))) {
                return next;
            }
            root = next;
        }
    }

    public static boolean isVulnerable(BigInteger d, BigInteger n) {
        BigInteger bound = n.sqrt().sqrt().multiply(BigInteger.valueOf(3)).divide(BigInteger.valueOf(3));
        return d.compareTo(bound) < 0;
    }

    public static BigInteger getWienerBound(BigInteger n) {
        BigInteger sqrtN = sqrt(n);
        BigInteger fourthRoot = sqrt(sqrtN);
        return fourthRoot.divide(BigInteger.valueOf(3));
    }
}