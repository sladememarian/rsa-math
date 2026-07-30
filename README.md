# RSA Cryptosystem & Discrete Mathematics Analysis (Java)

A comprehensive, zero-dependency Java implementation of the RSA Cryptosystem created for the Discrete Mathematics university project. This repository includes key pair generation, Miller-Rabin primality testing, fast modular exponentiation, Persian text encoding, digital signatures, cryptanalysis attacks, and performance benchmarking.

---

## 👥 Team Members & Contributions (شرح مشارکت اعضا)

| Member | Role | Contribution Description |
| :--- | :--- | :--- |
| **Amirpouyan (Leader)** | Core Math & Key Gen | Implemented Euclidean & Extended Euclidean algorithms, Fast Modular Exponentiation, Miller-Rabin Primality Test, 1024-bit RSA Key Pair Generator, and Persian UTF-8 Byte Text Encoder (`com.rsa.math`). |
| **Nima** | Crypto Engine & Security | Implemented core RSA Encryption/Decryption engine, RSA Digital Signatures, Wiener's Attack via continued fractions, ECC vs RSA benchmark helper, and unit test suite (`com.rsa.crypto`, `tests/`). |
| **Moradi** | Analysis & Benchmarks | Authored formal mathematical decryption proofs via Euler's Totient Theorem, implemented prime factorizers (Trial Division / Pollard's Rho), benchmark data exporters (`data/`), security report, and CLI `Main.java`. |

---

## 📁 Repository Structure

```text
rsa-discrete-math-java/
├── src/
│   └── main/java/com/rsa/
│       ├── math/          # Euclidean math, Miller-Rabin, Key Generator, Persian Text Encoder
│       ├── crypto/        # RSA Engine, Digital Signatures, Wiener Attack, ECC Comparison
│       ├── analysis/      # Factorization Benchmarking & Exporters
│       └── Main.java      # Interactive CLI Entry Point
├── tests/                 # Integration and Unit Test Suite
├── docs/                  # Mathematical Decryption Proof & Security Report
├── benchmarks/            # Modulus Factorization Execution Analysis Scripts
├── data/                  # Inputs, outputs, sample Persian texts, generated CSV benchmark data
└── README.md
