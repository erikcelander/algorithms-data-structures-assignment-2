package uppgift;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Callable;

public class TimeIt {

  public static class TimingResult {
    public final int repetitions;
    public final long min;
    public final long median;
    public final long avg;
    public final long max;
    public final double stdDev;
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("0.00", symbols);

    public TimingResult(long min, long median, long avg, long max, double stdDev, int repetitions) {
      this.repetitions = repetitions;
      this.min = min;
      this.median = median;
      this.avg = avg;
      this.max = max;
      this.stdDev = stdDev;
    }

    @Override
    public String toString() {
      return avg + " ns ± " + df.format(stdDev) + " ns per loop (mean ± std. dev. of " + repetitions + " runs). " +
          "Min: " + min + " ns, Median: " + median + " ns, Max: " + max + " ns.";
    }

  }

  public static <T> TimingResult timeIt(Callable<T> code, int repetitions) throws Exception {
    long[] times = new long[repetitions];

    for (int i = 0; i < repetitions; i++) {
      long start = System.nanoTime();
      code.call();
      long end = System.nanoTime();
      times[i] = end - start;
    }

    Arrays.sort(times);
    long total = Arrays.stream(times).sum();
    long avg = total / repetitions;
    long min = times[0];
    long median = times[repetitions / 2];
    long max = times[repetitions - 1];

    double variance = 0;
    for (long time : times) {
      variance += Math.pow(time - avg, 2);
    }
    variance /= repetitions;
    double stdDev = Math.sqrt(variance);

    return new TimingResult(min, median, avg, max, stdDev, repetitions);
  }
}
