package uppgift;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.Random;

public class BenchmarkTrees {

    private static final int[] sizes = { 200, 400, 800, 1600, 3200 };
    private static final int iterations = 8;
    private static final int repetitions = 2500;
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("0.000000", symbols);

    public static void main(String[] args) throws Exception {
        run();
    }
    public static void run() throws Exception {
        for (int size : sizes) {
            long totalBSTInsertionTimeSkewed = 0, totalRBTInsertionTimeSkewed = 0, totalBSTHeightSkewed = 0, totalRBTHeightSkewed = 0;
            long totalBSTInsertionTimeNonSkewed = 0, totalRBTInsertionTimeNonSkewed = 0, totalBSTHeightNonSkewed = 0, totalRBTHeightNonSkewed = 0;
            long totalBSTSearchTimeSkewed = 0, totalRBTSearchTimeSkewed = 0, totalBSTSearchTimeNonSkewed = 0, totalRBTSearchTimeNonSkewed = 0;
            long totalBSTDeletionTimeSkewed = 0, totalRBTDeletionTimeSkewed = 0, totalBSTDeletionTimeNonSkewed = 0, totalRBTDeletionTimeNonSkewed = 0;

            for (int iteration = 0; iteration < iterations; iteration++) {
                boolean skewed = iteration % 2 == 0;
                int[] randomValues = generateRandomValues(size, skewed);
                final int[] bstHeight = new int[1];
                final int[] rbtHeight = new int[1];

                // Insertion for BST
                long totalBSTInsertionTime = 0;
                Callable<Void> bstInsertCode = () -> {
                    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
                    for (int value : randomValues) {
                        bst.add(value);
                    }
                    bstHeight[0] = bst.height();
                    return null;
                };
                TimeIt.TimingResult bstInsertResult = TimeIt.timeIt(bstInsertCode, repetitions);
                totalBSTInsertionTime += bstInsertResult.avg;

                // Insertion for RBT
                long totalRBTInsertionTime = 0;
                Callable<Void> rbtInsertCode = () -> {
                    RedBlackTree<Integer> rbt = new RedBlackTree<>();
                    for (int value : randomValues) {
                        rbt.add(value);
                    }
                    rbtHeight[0] = rbt.height();
                    return null;
                };
                TimeIt.TimingResult rbtInsertResult = TimeIt.timeIt(rbtInsertCode, repetitions);
                totalRBTInsertionTime += rbtInsertResult.avg;

                // Search in BST
                long totalBSTSearchTime = 0;
                Callable<Void> bstSearchCode = () -> {
                    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
                    for (int value : randomValues) {
                        bst.add(value);
                    }
                    for (int value : randomValues) {
                        bst.contains(value);
                    }
                    return null;
                };
                TimeIt.TimingResult bstSearchResult = TimeIt.timeIt(bstSearchCode, repetitions);
                totalBSTSearchTime += bstSearchResult.avg;

                // Search in RBT
                long totalRBTSearchTime = 0;
                Callable<Void> rbtSearchCode = () -> {
                    RedBlackTree<Integer> rbt = new RedBlackTree<>();
                    for (int value : randomValues) {
                        rbt.add(value);
                    }
                    for (int value : randomValues) {
                        rbt.contains(value);
                    }
                    return null;
                };
                TimeIt.TimingResult rbtSearchResult = TimeIt.timeIt(rbtSearchCode, repetitions);
                totalRBTSearchTime += rbtSearchResult.avg;

                // Deletion in BST
                long totalBSTDeletionTime = 0;
                Callable<Void> bstDeleteCode = () -> {
                    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
                    for (int value : randomValues) {
                        bst.add(value);
                    }
                    for (int i = 0; i < randomValues.length / 2; i++) {
                        bst.remove(randomValues[i]);
                    }
                    return null;
                };
                TimeIt.TimingResult bstDeleteResult = TimeIt.timeIt(bstDeleteCode, repetitions);
                totalBSTDeletionTime += bstDeleteResult.avg;

                // Deletion in RBT
                long totalRBTDeletionTime = 0;
                Callable<Void> rbtDeleteCode = () -> {
                    RedBlackTree<Integer> rbt = new RedBlackTree<>();
                    for (int value : randomValues) {
                        rbt.add(value);
                    }
                    for (int i = 0; i < randomValues.length / 2; i++) {
                        rbt.delete(randomValues[i]);
                    }
                    return null;
                };
                TimeIt.TimingResult rbtDeleteResult = TimeIt.timeIt(rbtDeleteCode, repetitions);
                totalRBTDeletionTime += rbtDeleteResult.avg;


                if (skewed) {
                    totalBSTInsertionTimeSkewed += totalBSTInsertionTime / repetitions;
                    totalRBTInsertionTimeSkewed += totalRBTInsertionTime / repetitions;
                    totalBSTHeightSkewed += bstHeight[0];
                    totalRBTHeightSkewed += rbtHeight[0];
                    totalBSTSearchTimeSkewed += totalBSTSearchTime / repetitions;
                    totalRBTSearchTimeSkewed += totalRBTSearchTime / repetitions;
                    totalBSTDeletionTimeSkewed += totalBSTDeletionTime / repetitions;
                    totalRBTDeletionTimeSkewed += totalRBTDeletionTime / repetitions;
                } else {
                    totalBSTInsertionTimeNonSkewed += totalBSTInsertionTime / repetitions;
                    totalRBTInsertionTimeNonSkewed += totalRBTInsertionTime / repetitions;
                    totalBSTHeightNonSkewed += bstHeight[0];
                    totalRBTHeightNonSkewed += rbtHeight[0];
                    totalBSTSearchTimeNonSkewed += totalBSTSearchTime / repetitions;
                    totalRBTSearchTimeNonSkewed += totalRBTSearchTime / repetitions;
                    totalBSTDeletionTimeNonSkewed += totalBSTDeletionTime / repetitions;
                    totalRBTDeletionTimeNonSkewed += totalRBTDeletionTime / repetitions;
                }
            }

            int halfIterations = iterations / 2;
            printAverages(size, "Skewed", totalBSTInsertionTimeSkewed, totalRBTInsertionTimeSkewed, totalBSTHeightSkewed, totalRBTHeightSkewed, totalBSTSearchTimeSkewed, totalRBTSearchTimeSkewed, totalBSTDeletionTimeSkewed, totalRBTDeletionTimeSkewed, halfIterations);
            printAverages(size, "Non-Skewed", totalBSTInsertionTimeNonSkewed, totalRBTInsertionTimeNonSkewed, totalBSTHeightNonSkewed, totalRBTHeightNonSkewed, totalBSTSearchTimeNonSkewed, totalRBTSearchTimeNonSkewed, totalBSTDeletionTimeNonSkewed, totalRBTDeletionTimeNonSkewed, halfIterations);
        }
        System.out.println("Done");
    }

    private static void printAverages(int size, String dataType, long bstInsertTime, long rbtInsertTime, long bstHeight, long rbtHeight, long bstSearchTime, long rbtSearchTime, long bstDeleteTime, long rbtDeleteTime, int iterations) {
        System.out.println("Size: " + size + ", Data Type: " + dataType);
        System.out.println("-------------------------------------------------");
        System.out.println("BST Insertion Average Time: " + df.format((bstInsertTime / iterations) / 1_000_000.0) + " ms, Average Height: " + (bstHeight / iterations));
        System.out.println("RBT Insertion Average Time: " + df.format((rbtInsertTime / iterations) / 1_000_000.0) + " ms, Average Height: " + (rbtHeight / iterations));
        System.out.println("-------------------------------------------------");
        System.out.println("BST Search Average Time: " + df.format((bstSearchTime / iterations) / 1_000_000.0) + " ms");
        System.out.println("RBT Search Average Time: " + df.format((rbtSearchTime / iterations) / 1_000_000.0) + " ms");
        System.out.println("-------------------------------------------------");
        System.out.println("BST Deletion Average Time: " + df.format((bstDeleteTime / iterations) / 1_000_000.0) + " ms");
        System.out.println("RBT Deletion Average Time: " + df.format((rbtDeleteTime / iterations) / 1_000_000.0) + " ms");
        System.out.println("-------------------------------------------------");
    }

    
  
    

    private static int[] generateRandomValues(int size, boolean skewed) {
        Random random = new Random(42); // Using a fixed seed for reproducibility
        int[] values = new int[size];
        if (skewed) {
            for (int i = 0; i < size; i++) {
                values[i] = i; // Produces a sorted (skewed) array
            }
        } else {
            for (int i = 0; i < size; i++) {
                values[i] = random.nextInt(100_000);
            }
        }
        return values;
    }
}

// Benchmarking results that report is based on.
//
// Size: 200, Data Type: Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.000039 ms, Average Height: 199
// RBT Insertion Average Time: 0.000003 ms, Average Height: 7
// -------------------------------------------------
// BST Search Average Time: 0.000068 ms
// RBT Search Average Time: 0.000003 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.000039 ms
// RBT Deletion Average Time: 0.000006 ms
// -------------------------------------------------
// Size: 200, Data Type: Non-Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.000002 ms, Average Height: 13
// RBT Insertion Average Time: 0.000004 ms, Average Height: 10
// -------------------------------------------------
// BST Search Average Time: 0.000004 ms
// RBT Search Average Time: 0.000004 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.000003 ms
// RBT Deletion Average Time: 0.000006 ms
// -------------------------------------------------
// Size: 400, Data Type: Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.000175 ms, Average Height: 399
// RBT Insertion Average Time: 0.000006 ms, Average Height: 8
// -------------------------------------------------
// BST Search Average Time: 0.000303 ms
// RBT Search Average Time: 0.000009 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.000174 ms
// RBT Deletion Average Time: 0.000015 ms
// -------------------------------------------------
// Size: 400, Data Type: Non-Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.000006 ms, Average Height: 16
// RBT Insertion Average Time: 0.000007 ms, Average Height: 11
// -------------------------------------------------
// BST Search Average Time: 0.000008 ms
// RBT Search Average Time: 0.000009 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.000008 ms
// RBT Deletion Average Time: 0.000014 ms
// -------------------------------------------------
// Size: 800, Data Type: Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.000732 ms, Average Height: 799
// RBT Insertion Average Time: 0.000016 ms, Average Height: 9
// -------------------------------------------------
// BST Search Average Time: 0.001272 ms
// RBT Search Average Time: 0.000022 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.000733 ms
// RBT Deletion Average Time: 0.000040 ms
// -------------------------------------------------
// Size: 800, Data Type: Non-Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.000020 ms, Average Height: 19
// RBT Insertion Average Time: 0.000016 ms, Average Height: 12
// -------------------------------------------------
// BST Search Average Time: 0.000027 ms
// RBT Search Average Time: 0.000025 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.000025 ms
// RBT Deletion Average Time: 0.000042 ms
// -------------------------------------------------
// Size: 1600, Data Type: Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.003244 ms, Average Height: 1599
// RBT Insertion Average Time: 0.000043 ms, Average Height: 10
// -------------------------------------------------
// BST Search Average Time: 0.005668 ms
// RBT Search Average Time: 0.000062 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.003241 ms
// RBT Deletion Average Time: 0.000101 ms
// -------------------------------------------------
// Size: 1600, Data Type: Non-Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.000045 ms, Average Height: 22
// RBT Insertion Average Time: 0.000049 ms, Average Height: 14
// -------------------------------------------------
// BST Search Average Time: 0.000075 ms
// RBT Search Average Time: 0.000083 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.000062 ms
// RBT Deletion Average Time: 0.000118 ms
// -------------------------------------------------
// Size: 3200, Data Type: Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.013692 ms, Average Height: 3199
// RBT Insertion Average Time: 0.000102 ms, Average Height: 11
// -------------------------------------------------
// BST Search Average Time: 0.024032 ms
// RBT Search Average Time: 0.000152 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.013687 ms
// RBT Deletion Average Time: 0.000230 ms
// -------------------------------------------------
// Size: 3200, Data Type: Non-Skewed
// -------------------------------------------------
// BST Insertion Average Time: 0.000111 ms, Average Height: 24
// RBT Insertion Average Time: 0.000132 ms, Average Height: 14
// -------------------------------------------------
// BST Search Average Time: 0.000189 ms
// RBT Search Average Time: 0.000215 ms
// -------------------------------------------------
// BST Deletion Average Time: 0.000155 ms
// RBT Deletion Average Time: 0.000289 ms
// -------------------------------------------------