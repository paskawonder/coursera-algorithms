package coursera.third;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MaximumWeightIndependentSet {

    public static void main(final String[] args) throws IOException {
        final List<Integer> pathGraph = Files.lines(new File("/Users/pavelpolubentcev/Downloads/mwis.txt").toPath()).map(Integer::parseInt).collect(Collectors.toList());
        final MaximumWeightIndependentSet maximumWeightIndependentSet = new MaximumWeightIndependentSet();
        final int[] dp = maximumWeightIndependentSet.build(pathGraph);
        final Set<Integer> vertices = maximumWeightIndependentSet.reconstruct(dp);
        Stream.of(1, 2, 3, 4, 17, 117, 517, 997).forEach(e -> System.out.print(vertices.contains(e) ? 1 : 0));
    }

    public int[] build(final List<Integer> pathGraph) {
        final int[] dp = new int[pathGraph.size()];
        dp[0] = pathGraph.get(0);
        dp[1] = pathGraph.get(1);
        for (int i = 2, size = pathGraph.size(); i < size; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + pathGraph.get(i));
        }
        return dp;
    }

    public Set<Integer> reconstruct(final int[] array) {
        final Set<Integer> vertices = new HashSet<>();
        for (int i = array.length - 1; i > 0; i -= 2) {
            if (array[i] <= array[i - 1]) {
                i--;
            }
            vertices.add(i);
        }
        return vertices;
    }

}
