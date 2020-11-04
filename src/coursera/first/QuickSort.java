package coursera.first;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public final class QuickSort {

    public static void main(String[] args) throws IOException {
        final List<String> lines = Files.lines(new File("/Users/pavelpolubentcev/Downloads/QuickSort.txt").toPath()).collect(Collectors.toList());
        final int n = lines.size() - 1;
        int comparisons = new QuickSort(new PivotStrategy1()).quickSort(lines.stream().mapToInt(Integer::valueOf).toArray(), 0, n);
        System.out.println(comparisons);
        comparisons = new QuickSort(new PivotStrategy2()).quickSort(lines.stream().mapToInt(Integer::valueOf).toArray(), 0, n);
        System.out.println(comparisons);
        comparisons = new QuickSort(new PivotStrategy3()).quickSort(lines.stream().mapToInt(Integer::valueOf).toArray(), 0, n);
        System.out.println(comparisons);
    }

    private final PivotStrategy pivotStrategy;

    public QuickSort(final PivotStrategy pivotStrategy) {
        this.pivotStrategy = pivotStrategy;
    }

    public int quickSort(final int[] array, final int l, final int r) {
        if (l >= r) {
            return 0;
        }
        final int pivotIndex = partition(array, l, r);
        int comparisons = r - l;
        comparisons += quickSort(array, l, pivotIndex - 1);
        comparisons += quickSort(array, pivotIndex + 1, r);
        return comparisons;
    }

    private int partition(final int[] array, final int l, final int r) {
        final int pivotIndex = pivotStrategy.getPivot(array, l, r);
        final int pivot = array[pivotIndex];
        array[pivotIndex] = array[l];
        array[l] = pivot;
        int i = l + 1;
        for (int j = i; j <= r; j++) {
            if (array[j] < pivot) {
                final int tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
                i++;
            }
        }
        i--;
        array[l] = array[i];
        array[i] = pivot;
        return i;
    }

    public interface PivotStrategy {

        int getPivot(final int[] array, final int l, final int r);

    }

    public static final class PivotStrategy1 implements PivotStrategy {

        public int getPivot(final int[] array, final int l, final int r) {
            return l;
        }

    }

    public static final class PivotStrategy2 implements PivotStrategy {

        public int getPivot(final int[] array, final int l, final int r) {
            return r;
        }

    }

    public static final class PivotStrategy3 implements PivotStrategy {

        public int getPivot(final int[] array, final int l, final int r) {
            final int m = (r - l) / 2 + l;
            int median;
            if (array[l] < array[m] && array[l] < array[r]) {
                median = Math.min(array[m], array[r]);
            } else if (array[l] > array[r] && array[m] > array[r]) {
                median = Math.min(array[l], array[m]);
            } else {
                median = Math.min(array[l], array[r]);
            }
            return median == array[l] ? l : median == array[m] ? m : r;
        }

    }

}
