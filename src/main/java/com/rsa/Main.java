package com.rsa;

import benchmarks.BenchmarkExporter;
import benchmarks.FactorizationBenchmark;
import com.rsa.math.RsaKeyGenerator;
import com.rsa.crypto.EccComparison;
import tests.CryptoIntegrationTest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("====================================================");
        System.out.println("   🔐 RSA Cryptosystem & Discrete Math Framework    ");
        System.out.println("   K. N. Toosi University of Technology             ");
        System.out.println("====================================================");

        boolean running = true;
        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Run Factorization Benchmark (Trial Division vs Pollard's Rho)");
            System.out.println("2. Run RSA vs ECC Performance Comparison");
            System.out.println("3. Run Full Crypto Integration Tests");
            System.out.println("4. Display Proof and Security Analysis Summary");
            System.out.println("5. Exit");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    executeBenchmarkFlow();
                    break;
                case "2":
                    System.out.println("\n[+] Running RSA vs ECC Benchmark...");
                    EccComparison.main(new String[] { "5" });
                    break;
                case "3":
                    System.out.println("\n[+] Running Integration Tests...");
                    CryptoIntegrationTest.main(new String[] {});
                    break;
                case "4":
                    displayDocsSummary();
                    break;
                case "5":
                    running = false;
                    System.out.println("Exiting framework. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        }
        scanner.close();
    }

    private static void executeBenchmarkFlow() {
        System.out.println("\n[+] Running Factorization Benchmarks...");
        List<FactorizationBenchmark.BenchmarkResult> results = new ArrayList<>();

        int[] bitSizes = { 16, 24, 32, 40, 48 };
        for (int bits : bitSizes) {
            int primeBits = bits / 2;
            BigInteger p = RsaKeyGenerator.randomPrime(primeBits, 20);
            BigInteger q = RsaKeyGenerator.randomPrime(primeBits, 20);
            BigInteger n = p.multiply(q);

            FactorizationBenchmark.BenchmarkResult res = FactorizationBenchmark.runBenchmark(n, bits);
            results.add(res);
            System.out.printf("Completed %d-bit modulus factoring...%n", bits);
        }

        BenchmarkExporter.printAsciiChart(results);
        BenchmarkExporter.exportToCSV(results, "data/factorization_results.csv");
    }

    private static void displayDocsSummary() {
        System.out.println("\n----------------------------------------------------");
        System.out.println("📄 Documentation Files Status:");
        System.out.println("1. Euler Proof: docs/proof_euler.md (Completed)");
        System.out.println("2. Security Analysis: docs/security_report.md (Completed)");
        System.out.println("----------------------------------------------------");
    }
}