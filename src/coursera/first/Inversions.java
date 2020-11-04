package coursera.first;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public final class Inversions {

    public static void main(String[] args) throws IOException {
        final List<Integer> ints = Files
                .lines(new File("/Users/pavelpolubentcev/Downloads/array.txt").toPath())
                .map(Integer::parseInt).collect(Collectors.toList());
        final int[] array = new int[ints.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = ints.get(i);
        }
        final Inversions inversions = new Inversions();
        final double result = inversions.inversions(array);
        System.out.printf("%.0f", result);
    }

    public double inversions(final int[] array) {
        return inversions(array, 0, array.length - 1, new int[array.length]);
    }

    private double inversions(final int[] array, final int i, final int j, final int[] aux) {
        if (i == j) {
            return 0;
        }
        final int m = (j - i) / 2 + i;
        final double leftInvs = inversions(array, i, m, aux);
        final double rightInvs = inversions(array, m + 1, j, aux);
        final double globalInversions = merge(array, i, m + 1, j, aux);
        return leftInvs + rightInvs + globalInversions;
    }

    public double merge(final int[] array, final int l, final int m, final int r, final int[] aux) {
        int i = l, j = m, index = 0;
        double inversions = 0;
        while (i < m && j <= r) {
            if (array[i] <= array[j]) {
                aux[index++] = array[i++];
            } else {
                aux[index++] = array[j++];
                inversions += m - i;
            }
        }
        while (i < m) {
            aux[index++] = array[i++];
        }
        while (j <= r) {
            aux[index++] = array[j++];
        }
        System.arraycopy(aux, 0, array, l, index);
        return inversions;
    }

}
