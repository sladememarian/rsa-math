package benchmarks;

import com.rsa.math.EuclideanMath;
import java.math.BigInteger;

public class FactorizationBenchmark {

    // Trial Division Factorization
    public static BigInteger trialDivision(BigInteger n) {
        BigInteger two = BigInteger.valueOf(2);
        if (n.mod(two).equals(BigInteger.ZERO))
            return two;

        BigInteger i = BigInteger.valueOf(3);
        while (i.multiply(i).compareTo(n) <= 0) {
            if (n.mod(i).equals(BigInteger.ZERO)) {
                return i;
            }
            i = i.add(two);
        }
        return n;
    }

    // Pollard's Rho Factorization Algorithm
    public static BigInteger pollardsRho(BigInteger n) {
        if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO))
            return BigInteger.valueOf(2);

        BigInteger x = BigInteger.valueOf(2);
        BigInteger y = BigInteger.valueOf(2);
        BigInteger d = BigInteger.ONE;
        BigInteger c = BigInteger.ONE;

        while (d.equals(BigInteger.ONE)) {
            x = f(x, c, n);
            y = f(f(y, c, n), c, n);

            // Replaced BigInteger.gcd with custom EuclideanMath.gcd
            BigInteger absDiff = x.subtract(y).abs();
            d = EuclideanMath.gcd(absDiff, n);

            if (d.equals(n)) {
                // Retry with different random constant if cycle fails
                c = c.add(BigInteger.ONE);
                x = BigInteger.valueOf(2);
                y = BigInteger.valueOf(2);
                d = BigInteger.ONE;
            }
        }
        return d;
    }

    private static BigInteger f(BigInteger x, BigInteger c, BigInteger n) {
        return x.multiply(x).add(c).mod(n);
    }

    // Benchmark Helper
    public static BenchmarkResult runBenchmark(BigInteger modulus, int bitSize) {
        long startTD = System.nanoTime();
        trialDivision(modulus);
        long endTD = System.nanoTime();

        long startPR = System.nanoTime();
        pollardsRho(modulus);
        long endPR = System.nanoTime();

        double tdTimeMs = (endTD - startTD) / 1e6;
        double prTimeMs = (endPR - startPR) / 1e6;

        return new BenchmarkResult(bitSize, tdTimeMs, prTimeMs);
    }

    public static class BenchmarkResult {
        public int bitSize;
        public double trialDivisionMs;
        public double pollardRhoMs;

        public BenchmarkResult(int bitSize, double trialDivisionMs, double pollardRhoMs) {
            this.bitSize = bitSize;
            this.trialDivisionMs = trialDivisionMs;
            this.pollardRhoMs = pollardRhoMs;
        }
    }
}