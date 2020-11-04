package coursera.third;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public final class Knapsack {

    private static final String SPACE_REGEX = "\\s+";

    public static void main(final String[] args) throws IOException {
        final List<Item> items = Files.lines(new File("/Users/pavelpolubentcev/Downloads/knapsack.txt").toPath())
                .map(e -> e.split(SPACE_REGEX)).map(e -> new Item(Integer.parseInt(e[0]), Integer.parseInt(e[1]))).collect(Collectors.toList());
        final Knapsack knapsack = new Knapsack();
        final int capacity = 2000000;
        final int result = knapsack.knapsack(items, capacity);
        System.out.println(result);
    }

    public int knapsack(final List<Item> items, final int capacity) {
        final int[][] dp = new int[2][capacity + 1];
        for (final Item item: items) {
            for (int j = item.weight; j <= capacity; j++) {
                dp[1][j] = Math.max(dp[0][j], item.value + dp[0][j - item.weight]);
            }
            System.arraycopy(dp[1], 0, dp[0], 0, capacity + 1);
        }
        return dp[1][capacity];
    }

    private static final class Item {

        private final int value;

        private final int weight;

        public Item(final int value, final int weight) {
            this.value = value;
            this.weight = weight;
        }

    }

}
