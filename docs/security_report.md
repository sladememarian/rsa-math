# RSA Security & Cryptanalysis Analysis Report

## 1. Miller-Rabin Primality Testing & Error Bounds

The Miller-Rabin primality test is a probabilistic test based on strong pseudoprimes.

### Error Probability Bound

For a composite odd integer $n$, the probability that a randomly chosen base $a$ acts as a strong liar is at most $\frac{1}{4}$.
When repeating the test across $k$ independent rounds with randomly chosen bases:
$$P(\text{Composite } n \text{ declared prime}) \le 4^{-k}$$

- For $k = 10$: $P(\text{Error}) \le 4^{-10} \approx 9.53 \times 10^{-7}$
- For $k = 40$: $P(\text{Error}) \le 4^{-40} \approx 8.27 \times 10^{-25}$ (Standard in production key gen).

---

## 2. Minimum Safe Modulus Bit-Lengths

The security of RSA depends on the difficulty of factoring $n = p \cdot q$.

- **Small Modulus ($\le 512$ bits):** Vulnerable to General Number Field Sieve (GNFS) and modern cluster factoring. Easily broken.
- **1024-bit Modulus:** Considered vulnerable to state-level adversaries; no longer recommended for cryptographic security.
- **2048-bit Modulus:** Current minimum industry standard. Provides approximately $112$-bits of security.
- **4096-bit Modulus:** High-security configuration used for long-term root certificates ($128$+ bits of security).

---

## 3. Wiener's Attack on Small Private Exponents

Wiener's attack uses continued fractions to recover the secret exponent $d$ in polynomial time if $d$ is too small relative to $n$.

### Vulnerability Threshold

Wiener established that if:
$$d < \frac{1}{3} n^{1/4}$$

The continued fraction expansion of $\frac{e}{n}$ will produce a convergent fraction $\frac{k}{d}$ that reveals $d$.

**Mitigation:** Always choose standard large public exponents like $e = 65537$ ($2^{16}+1$) to ensure $d$ is large enough ($d \approx n$).
