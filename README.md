# RSA Cryptosystem & Discrete Mathematics Analysis (Java)

A comprehensive, zero-dependency Java implementation of the RSA Cryptosystem created for the Discrete Mathematics university project. This repository includes key pair generation, primality testing, fast exponentiation, Persian text encoding, digital signatures, cryptanalysis attacks, and performance benchmarking.

---

## 👥 Team Members

* **Amirpouyan (Leader)** – Discrete Math Utilities & RSA Key Generation Pipeline
* **Nima** – Encryption/Decryption Engine, Digital Signatures & Wiener's Attack
* **Moradi** – Mathematical Decryption Proofs, Factorization Benchmarks & Security Analysis

---

## 🚀 Key Features

### Core Mechanics
* **Custom Primality Testing:** Miller-Rabin probabilistic test implemented from scratch in Java.
* **Modular Arithmetic:** Euclidean, Extended Euclidean ($d = e^{-1} \bmod \phi(n)$), and Fast Modular Exponentiation ($a^b \bmod m$).
* **1024-bit RSA Support:** Full support for large composite keys using Java `BigInteger`.
* **Mathematical Proof:** Rigorous mathematical proof proving RSA validity using Euler's Totient Theorem ($\phi(n)$).
* **Factorization Benchmarks:** Empirical timing analysis for recovering prime factors $p$ and $q$ across different modulus bit lengths.

### Bonus Features Included
* **Persian Text Support:** Complete UTF-8 byte encoding converting Persian text to numerical blocks and back.
* **Digital Signatures:** Message signing using private keys and signature verification using public keys.
* **Wiener's Vulnerability Attack:** Implementation exploiting small private exponents where $d < \frac{1}{3} n^{1/4}$.
* **RSA vs. ECC Comparison:** Benchmark performance analysis evaluating key size and runtime differences between RSA and ECC.

---

## 🛠️ Project Architecture

```text
src/
└── main/
    └── java/
        └── com/
            └── rsa/
                ├── math/
                │   ├── EuclideanMath.java          # GCD & Extended Euclidean Algorithm
                │   ├── ModularExponentiation.java  # Fast Modular Exponentiation
                │   ├── MillerRabinPrimality.java   # Miller-Rabin Primality Test
                │   ├── RsaKeyGenerator.java        # Key Pair Generator
                │   └── TextEncoder.java            # Persian UTF-8 Byte Encoder
                ├── crypto/
                │   ├── RsaEngine.java              # Core Encryption & Decryption
                │   ├── DigitalSignature.java       # RSA Digital Signatures
                │   ├── WienerAttack.java           # Cryptanalysis for small d
                │   └── EccComparison.java          # ECC vs RSA Metrics
                ├── analysis/
                │   ├── FactorizationBenchmark.java # Modulus Factorization Analysis
                │   └── BenchmarkExporter.java      # Result Exporter & Formatter
                └── Main.java                       # Interactive CLI Entry Point
docs/
├── proof_euler.md                                  # Euler's Totient Decryption Proof
└── security_report.md                              # Security & Error Probability Analysis
