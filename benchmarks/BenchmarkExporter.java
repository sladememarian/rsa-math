package benchmarks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class BenchmarkExporter {

    public static void exportToCSV(List<FactorizationBenchmark.BenchmarkResult> results, String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("BitSize,TrialDivision_ms,PollardRho_ms");
            for (FactorizationBenchmark.BenchmarkResult res : results) {
                writer.printf("%d,%.4f,%.4f%n", res.bitSize, res.trialDivisionMs, res.pollardRhoMs);
            }
            System.out.println("[+] Benchmark dataset exported to: " + filePath);
        } catch (IOException e) {
            System.err.println("[-] Error exporting dataset: " + e.getMessage());
        }
    }

    public static void printAsciiChart(List<FactorizationBenchmark.BenchmarkResult> results) {
        System.out.println("\n========== Factorization Runtime Benchmark (ASCII Chart) ==========");
        System.out.println("Bit Length | Pollard's Rho Runtime (ms)");
        System.out.println("--------------------------------------------------------------------");

        for (FactorizationBenchmark.BenchmarkResult res : results) {
            int bars = (int) Math.min(Math.ceil(res.pollardRhoMs * 2), 50);
            String barStr = "█".repeat(Math.max(1, bars));
            System.out.printf("%9d-bit | %-50s (%.3f ms)%n", res.bitSize, barStr, res.pollardRhoMs);
        }
        System.out.println("================================================--------------------\n");
    }
}