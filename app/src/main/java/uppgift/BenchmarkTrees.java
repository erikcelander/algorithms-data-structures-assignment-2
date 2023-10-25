package uppgift;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.Random;

public class BenchmarkTrees {

    private static final int[] sizes = {200, 400, 800, 1600};
    private static final int iterations = 7;
    private static final int repetitions = 5000;
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("0.000000", symbols);

    public static void main(String[] args) throws Exception {
        run();
    }

    public static void run() throws Exception {
        for (int size : sizes) {

            long totalBSTInsertionTime = 0;
            long totalBSTDeletionTime = 0;
            long totalRBTInsertionTime = 0;
            long totalRBTDeletionTime = 0;

            for (int iteration = 0; iteration < iterations; iteration++) {

                int[] randomValues = generateRandomValues(size);

                Callable<Void> bstInsertCode = () -> {
                    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
                    for (int value : randomValues) {
                        bst.add(value);
                    }
                    return null;
                };
                TimeIt.TimingResult bstInsertResult = TimeIt.timeIt(bstInsertCode, repetitions);
                totalBSTInsertionTime += bstInsertResult.avg;

                Callable<Void> rbtInsertCode = () -> {
                    RedBlackTree<Integer> rbt = new RedBlackTree<>();
                    for (int value : randomValues) {
                        rbt.add(value);
                    }
                    return null;
                };
                TimeIt.TimingResult rbtInsertResult = TimeIt.timeIt(rbtInsertCode, repetitions);
                totalRBTInsertionTime += rbtInsertResult.avg;

                // After insertion, we'll delete half of the nodes (this creates an average scenario for trees)
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
            }

            long averageBSTInsertionTime = totalBSTInsertionTime / iterations;
            long averageBSTDeletionTime = totalBSTDeletionTime / iterations;
            long averageRBTInsertionTime = totalRBTInsertionTime / iterations;
            long averageRBTDeletionTime = totalRBTDeletionTime / iterations;

            System.out.println("Size: " + size);
            System.out.println("BST Insertion Average Time: " + df.format(averageBSTInsertionTime / 1_000_000.0) + " ms");
            System.out.println("BST Deletion Average Time: " + df.format(averageBSTDeletionTime / 1_000_000.0) + " ms");
            System.out.println("RBT Insertion Average Time: " + df.format(averageRBTInsertionTime / 1_000_000.0) + " ms");
            System.out.println("RBT Deletion Average Time: " + df.format(averageRBTDeletionTime / 1_000_000.0) + " ms");
            System.out.println("-------------------------------------------------");
        }
    }

    // Helper method to generate random values for testing
    private static int[] generateRandomValues(int size) {
        Random random = new Random();
        int[] values = new int[size];
        for (int i = 0; i < size; i++) {
            values[i] = random.nextInt(100_000);
        }
        return values;
    }
}
