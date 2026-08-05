package com.rsa;

import benchmarks.BenchmarkExporter;
import benchmarks.FactorizationBenchmark;
import com.rsa.math.RsaKeyGenerator;
import com.rsa.crypto.EccComparison;
import tests.CryptoIntegrationTest;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Force UTF-8 stdout regardless of the host console's codepage. This
        // does NOT fix corrupted non-ASCII *source* literals if the file was
        // compiled without `-encoding UTF-8` (the bytes are already wrong by
        // then), which is why the banner below is kept plain ASCII on purpose.
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ignored) {
            // UTF-8 is always supported on standard JVMs; nothing to do.
        }

        // Non-interactive mode: `java -cp bin com.rsa.Main --all` runs every
        // menu option (1-4) once, in order, with no stdin required. Useful for
        // quickly smoke-testing all features without pressing keys manually.
        boolean autoMode = args.length > 0
                && (args[0].equalsIgnoreCase("--all") || args[0].equalsIgnoreCase("--auto"));

        printBanner();

        if (autoMode) {
            System.out.println("\n[Auto Mode] Running options 1, 2, 3, 4 sequentially...\n");
            for (String option : new String[] { "1", "2", "3", "4" }) {
                System.out.println("\n> " + option);
                runOption(option);
            }
            System.out.println("\n[Auto Mode] Complete. Exiting framework. Goodbye!");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            System.out.flush();

            String choice = scanner.nextLine().trim();
            if (choice.equals("5")) {
                running = false;
                System.out.println("Exiting framework. Goodbye!");
            } else {
                running = runOption(choice);
            }
        }
        scanner.close();
    }

    private static void printBanner() {
        System.out.println("====================================================");
        System.out.println("   RSA Cryptosystem & Discrete Math Framework      ");
        System.out.println("   K. N. Toosi University of Technology            ");
        System.out.println("====================================================");
    }

    private static void printMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Run Factorization Benchmark (Trial Division vs Pollard's Rho)");
        System.out.println("2. Run RSA vs ECC Performance Comparison");
        System.out.println("3. Run Full Crypto Integration Tests");
        System.out.println("4. Display Proof and Security Analysis Summary");
        System.out.println("5. Exit");
        System.out.print("> ");
    }

    /** Executes a single menu choice. Returns false only for an invalid one caught upstream. */
    private static boolean runOption(String choice) {
        switch (choice) {
            case "1":
                executeBenchmarkFlow();
                return true;
            case "2":
                System.out.println("\n[+] Running RSA vs ECC Benchmark...");
                EccComparison.main(new String[] { "5" });
                return true;
            case "3":
                System.out.println("\n[+] Running Integration Tests...");
                CryptoIntegrationTest.main(new String[] {});
                return true;
            case "4":
                displayDocsSummary();
                return true;
            default:
                System.out.println("Invalid selection. Try again.");
                return true;
        }
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
        System.out.println("Documentation Files Status:");
        System.out.println("1. Euler Proof: docs/proof_euler.md (Completed)");
        System.out.println("2. Security Analysis: docs/security_report.md (Completed)");
        System.out.println("----------------------------------------------------");
    }
}
