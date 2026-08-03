# RSA Cryptosystem & Discrete Mathematics Analysis (Java)

A zero-dependency Java implementation of a complete public-key RSA cryptosystem built from first principles, created as a Discrete Mathematics course project at K. N. Toosi University of Technology. The project explores discrete math topics including modular arithmetic, Miller-Rabin primality testing, Extended Euclidean inverse computation, fast modular exponentiation, and cryptanalysis — all without external cryptography libraries.

Main idea: generate secure RSA key pairs, encrypt/decrypt numerical and text payloads, prove decryption correctness via Euler's Totient Theorem, and benchmark modulus factorization to demonstrate RSA security in practice.

---

## 👥 Team Members & Contributions

| Member | Role | Contribution Description |
| :--- | :--- | :--- |
| **Amirpouyan Memarian (Leader)** | Core Math & Key Gen | Implemented Euclidean & Extended Euclidean algorithms, fast modular exponentiation, Miller-Rabin primality testing, 1024-bit RSA key pair generator, and Persian UTF-8 byte text encoder (`com.rsa.math`). |
| **Nima** | Crypto Engine & Security | Implemented the core RSA encryption/decryption engine, RSA digital signatures, Wiener's Attack via continued fractions, ECC vs RSA benchmark helper, and the integration test suite (`com.rsa.crypto`, `tests/`). |
| **Moradi** | Analysis & Benchmarks | Authored formal mathematical decryption proofs via Euler's Totient Theorem, implemented prime factorizers (Trial Division / Pollard's Rho), benchmark data exporters, the security report, and the interactive CLI entry point. |

---

## 📁 Repository Structure

```text
rsa-discrete-math-java/
├── src/
│   └── main/java/com/rsa/
│       ├── math/                          <-- Leader (Amirpouyan)
│       │   ├── EuclideanMath.java         [Task 1 - Amirpouyan]
│       │   ├── ModularExponentiation.java [Task 2 - Amirpouyan]
│       │   ├── MillerRabinPrimality.java  [Task 3 - Amirpouyan]
│       │   ├── RsaKeyGenerator.java       [Task 4 - Amirpouyan]
│       │   └── TextEncoder.java           [Task 5 - Amirpouyan]
│       ├── crypto/                        <-- Nima
│       │   ├── RsaEngine.java             [Task 6 - Nima]
│       │   ├── DigitalSignature.java      [Task 7 - Nima]
│       │   ├── WienerAttack.java          [Task 8 - Nima]
│       │   └── EccComparison.java         [Task 9 - Nima]
│       └── Main.java                      [Task 15 - Moradi]
├── tests/
│   └── CryptoIntegrationTest.java         [Task 10 - Nima]
├── docs/
│   ├── proof_euler.md                     [Task 11 - Moradi]
│   └── security_report.md                 [Task 14 - Moradi]
├── benchmarks/
│   ├── FactorizationBenchmark.java        [Task 12 - Moradi]
│   └── BenchmarkExporter.java             [Task 13 - Moradi]
├── data/                                  <-- Shared Data Folder
│   ├── sample_persian_text.txt
│   └── factorization_results.csv
└── README.md
```

---

## 🛠️ Install Dependencies

No third-party dependencies. Only requires:

- **JDK 11 or later** (uses native `java.math.BigInteger` for arbitrary-precision arithmetic)

Verify your JDK:

```sh
java -version
```

---

## 💻 How to Run

Compile all sources:

```sh
javac -d bin src/main/java/com/rsa/math/*.java src/main/java/com/rsa/crypto/*.java benchmarks/*.java src/main/java/com/rsa/Main.java tests/*.java
```

Run the interactive CLI application:

```sh
java -cp bin com.rsa.Main
```

Run the integration tests:

```sh
java -cp bin CryptoIntegrationTest
```
