package com.rsa;

import benchmarks.BenchmarkExporter;
import benchmarks.FactorizationBenchmark;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("====================================================");
        System.out.println("   🔐 RSA Cryptosystem & Discrete Math Framework    ");
        System.out.println("   K. N. Toosi University of Technology  ");
        System.out.println("====================================================");

        boolean running = true;
        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Run Factorization Benchmark (Trial Division vs Pollard's Rho)");
            System.out.println("2. Display Proof and Security Analysis Summary");
            System.out.println("3. Exit");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    executeBenchmarkFlow();
                    break;
                case "2":
                    displayDocsSummary();
                    break;
                case "3":
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

        // Test modulus values of varying bit-length
        int[] bitSizes = { 16, 24, 32, 40, 48 };
        for (int bits : bitSizes) {
            BigInteger p = BigInteger.probablePrime(bits / 2, new java.util.Random());
            BigInteger q = BigInteger.probablePrime(bits / 2, new java.util.Random());
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