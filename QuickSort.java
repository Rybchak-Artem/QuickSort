import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class QuickSort {

    public static void quickSort(double[] array, boolean ascending) {
        if (array == null || array.length < 2) {
            return;
        }
        quickSort(array, 0, array.length - 1, ascending);
    }

    private static void quickSort(double[] a, int low, int high, boolean ascending) {
        while (low < high) {
            int p = partition(a, low, high, ascending);

            if (p - low < high - p) {
                quickSort(a, low, p - 1, ascending);
                low = p + 1;
            } else {
                quickSort(a, p + 1, high, ascending);
                high = p - 1;
            }
        }
    }

    private static int partition(double[] a, int low, int high, boolean ascending) {
        double pivot = a[high];
        int i = low; 

        for (int j = low; j < high; j++) {
            if (isBefore(a[j], pivot, ascending)) {
                swap(a, i, j);
                i++;
            }
        }
        swap(a, i, high); 
        return i;
    }

    private static boolean isBefore(double x, double y, boolean ascending) {
        return ascending ? x < y : x > y;
    }

    private static void swap(double[] a, int i, int j) {
        if (i != j) {
            double tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }

    public static boolean isSorted(double[] array, boolean ascending) {
        for (int i = 1; i < array.length; i++) {
            if (ascending ? array[i - 1] > array[i] : array[i - 1] < array[i]) {
                return false;
            }
        }
        return true;
    }

    private static double[] randomArray(int size, Random random) {
        double[] a = new double[size];
        for (int i = 0; i < size; i++) {
            a[i] = random.nextDouble() * 2000 - 1000;
        }
        return a;
    }

    @FunctionalInterface
    private interface Sorter {
        void sort(double[] array, boolean ascending);
    }

    private static double measure(Sorter sorter, double[] source, boolean ascending) {
        double[] copy = Arrays.copyOf(source, source.length);
        long start = System.nanoTime();
        sorter.sort(copy, ascending);
        long end = System.nanoTime();

        if (!isSorted(copy, ascending)) {
            throw new IllegalStateException("Massive is not sorted!");
        }
        return (end - start) / 1_000_000.0;
    }

    public static void main(String[] args) {
        double[] demo = {3, 7, 0, 1, 4, 2};
        double[] asc = demo.clone();
        double[] desc = demo.clone();
        quickSort(asc, true);
        quickSort(desc, false);
        System.out.println("First:   " + Arrays.toString(demo));
        System.out.println("Ascending: " + Arrays.toString(asc));
        System.out.println("Descending:  " + Arrays.toString(desc));
        System.out.println();

        int[] sizes = {1_000, 10_000, 100_000};
        Random random = new Random(42);

        double[] warm = randomArray(5_000, random);
        for (int i = 0; i < 5; i++) {
            measure(QuickSort::quickSort, warm, true);
        }

        for (boolean ascending : new boolean[]{true, false}) {
            System.out.println(ascending ? "=== Ascending ===" : "=== Descending ===");
            System.out.printf("%-10s %12s%n",
                    "N", "QuickSort");

            for (int size : sizes) {
                double[] data = randomArray(size, random);

                double quick = measure(QuickSort::quickSort, data, ascending);

                System.out.println(String.format(Locale.ROOT,
                        "%-10d %9.3f ms",
                        size, quick));
            }
            System.out.println();
        }
    }
}

