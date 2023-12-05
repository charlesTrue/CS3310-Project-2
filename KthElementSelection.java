import java.util.Arrays;
import java.util.Random;

public class KthElementSelection {

    public static void main(String[] args) {
        execute(new int[][]{
                {100,  1, 2, 3},        // Example data for 100 elements
                {500,  1, 2, 3},        // Example data for 500 elements
                {1000,  1, 2, 3},
                {5000,  1, 2, 3},
                {10000,  1, 2, 3},
                {50000,  1, 2, 3},
                {100000,  1, 2, 3},
                {500000,  1, 2, 3},
                {1000000,  1, 2, 3},
                {5000000,  1, 2, 3},
                {10000000,  1, 2, 3},
                {50000000,  1, 2, 3},
                {100000000,  1, 2, 3},

        });
    }

        private static void execute(int[][] ints) {
        for (int[] data : ints) {
            int dimension = data[0];
            System.out.println("Running for dimension: " + dimension);

            // You can call your selection methods here and print the results
            int[] arr = new Random().ints(dimension, 0, dimension).toArray();

            int k = dimension / 2; // Adjust the value of k as needed

            long start = System.nanoTime();
            int resultMergeSort = kthMergeSort(Arrays.copyOf(arr, arr.length), k);
            long end = System.nanoTime();
            System.out.println("MergeSort result: " + resultMergeSort + " | Time taken: " + (end - start) / 1e9 + " seconds");

            start = System.nanoTime();
            int resultPartition = kthPartition(Arrays.copyOf(arr, arr.length), k, 0, arr.length);
            end = System.nanoTime();
            System.out.println("Partition result: " + resultPartition + " | Time taken: " + (end - start) / 1e9 + " seconds");

            start = System.nanoTime();
            int resultMM = kthMM(Arrays.copyOf(arr, arr.length), k, 0, arr.length);
            end = System.nanoTime();
            System.out.println("Median of Medians result: " + resultMM + " | Time taken: " + (end - start) / 1e9 + " seconds");

            System.out.println(); // Add a newline for better readability
        }
    }

    // MISC. HELPERS
    static class Pair<T, U> {
        T first;
        U second;

        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    // ALGO 1 HELPER
    static void mergeSort(int[] arr) {
        if (arr.length <= 1) {
            return;
        }

        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);

        mergeSort(left);
        mergeSort(right);

        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] < right[j]) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }

        while (i < left.length) {
            arr[k++] = left[i++];
        }

        while (j < right.length) {
            arr[k++] = right[j++];
        }
    }

    // ALGO 2 HELPER
    static int partition(int[] arr, int start, int end, Integer pivot) {
        if (pivot != null) {
            int switchIndex = Arrays.asList(Arrays.copyOfRange(arr, start, end)).indexOf(pivot) + start;
            int temp = arr[switchIndex];
            arr[switchIndex] = arr[end - 1];
            arr[end - 1] = temp;
        } else {
            pivot = arr[end - 1];
        }

        int i = start;
        for (int j = start; j < end; j++) {
            if (arr[j] < pivot) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
            }
        }

        int temp = arr[i];
        arr[i] = arr[end - 1];
        arr[end - 1] = temp;

        return i;
    }

    // ALGO 3 HELPER
    static int findMedian(int[] arr) {
        Arrays.sort(arr);
        return arr[arr.length / 2];
    }

    static int medianOfMedians(int[] arr, int r) {
        if (arr.length < r * r) {
            return findMedian(arr);
        }

        int[][] sublists = new int[(int) Math.ceil((double) arr.length / r)][r];
        for (int i = 0; i < arr.length; i += r) {
            sublists[i / r] = Arrays.copyOfRange(arr, i, Math.min(i + r, arr.length));
        }

        int[] medians = new int[sublists.length];
        for (int i = 0; i < sublists.length; i++) {
            medians[i] = findMedian(sublists[i]);
        }

        return medianOfMedians(medians, r);
    }

    // SELECTION METHODS
    static int kthMergeSort(int[] arr, int k) {
        mergeSort(arr);
        return arr[k];
    }

    static int kthPartition(int[] arr, int k, int start, int end) {
        int pivotPos = partition(arr, start, end, null);
        if (k == pivotPos) {
            return arr[pivotPos];
        } else if (k < pivotPos) {
            return kthPartition(arr, k, start, pivotPos);
        } else {
            return kthPartition(arr, k, pivotPos + 1, end);
        }
    }

    static int kthMM(int[] arr, int k, int start, int end) {
        int pivot = medianOfMedians(arr, 5);
        int pivotPos = partition(arr, start, end, pivot);
        if (k == pivotPos) {
            return arr[pivotPos];
        } else if (k < pivotPos) {
            return kthMM(arr, k, start, pivotPos);
        } else {
            return kthMM(arr, k, pivotPos + 1, end);
        }
    }
}
