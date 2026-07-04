import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int size;
    private double[] trialResults;
    private int totalTrials;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        size = n;
        totalTrials = trials;
        trialResults = new double[trials];

        executeTrials();
    }

    public double mean() {
        return StdStats.mean(trialResults);
    }

    public double stddev() {
        return StdStats.stddev(trialResults);
    }

    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(totalTrials));
    }

    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(totalTrials));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println(
            "95% confidence interval = ["
            + ps.confidenceLo() + ", "
            + ps.confidenceHi() + "]"
        );
    }

    private void executeTrials() {
        for (int i = 0; i < trialResults.length; i++) {
            Percolation percolation = new Percolation(size);
            while (!percolation.percolates()) {
                int rowIdx = StdRandom.uniformInt(1, size + 1);
                int colIdx = StdRandom.uniformInt(1, size + 1);
                percolation.open(rowIdx, colIdx);
            }
            double sitesOpened = (double) percolation.numberOfOpenSites();
            trialResults[i] = sitesOpened / (size * size);
        }
    }
}