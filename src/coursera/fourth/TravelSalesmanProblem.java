package coursera.fourth;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public final class TravelSalesmanProblem {

    private static final String SPACE_REGEX = "\\s+";

    public static void main(final String[] args) throws IOException {
        List<Point> points = Files
                .lines(new File("/Users/pavelpolubentcev/Downloads/tsp.txt").toPath())
                .map(e -> e.split(SPACE_REGEX)).map(e -> new Point(Double.parseDouble(e[0]), Double.parseDouble(e[1])))
                .collect(Collectors.toList());
        final TravelSalesmanProblem tsp = new TravelSalesmanProblem();
        double result = tsp.dynamicProgramming(points);
        System.out.println(result);
        points = Files
                .lines(new File("/Users/pavelpolubentcev/Downloads/nn.txt").toPath())
                .map(e -> e.split(SPACE_REGEX)).map(e -> new Point(Double.parseDouble(e[1]), Double.parseDouble(e[2])))
                .collect(Collectors.toList());
        result = tsp.heuristic(points);
        System.out.println(result);
    }

    public double dynamicProgramming(final List<Point> points) {
        final int n = points.size();
        final double[][] dp = new double[n][n];
        for (int s = 1; s < n; s++) {
            for (int j = 0; j < n; j++) {
                dp[s][j] = Double.MAX_VALUE;
            }
        }
        for (int subProblemSize = 1; subProblemSize < n; subProblemSize++) {
            for (int s = 0; s < n; s++) {
                for (int j = 1; j <= s; j++) {
                    double min = dp[s - j][0] + dist(points.get(s - j), points.get(0));
                    for (int k = 1; k <= s; k++) {
                        min = Math.min(min, dp[s - j][k] + dist(points.get(s - j), points.get(k)));
                    }
                    dp[s][j] = min;
                }
            }
        }
        final Point src = points.get(0);
        double result = dp[n - 1][1] + dist(points.get(1), src);
        for (int j = 1; j < n; j++) {
            result = Math.min(result, dp[n - 1][j] + dist(points.get(j), src));
        }
        return result;
    }

    public double heuristic(final List<Point> points) {
        final int n = points.size();
        final boolean[] visited = new boolean[n];
        double result = 0;
        int count = 1;
        Point point = points.get(0);
        while (count < n) {
            int index = 1;
            while (visited[index]) {
                index++;
            }
            double min = dist(point, points.get(index));
            for (int i = index + 1; i < n; i++) {
                if (!visited[i]) {
                    final double dist = dist(point, points.get(i));
                    if (dist < min) {
                        min = dist;
                        index = i;
                    }
                }
            }
            point = points.get(index);
            visited[index] = true;
            result += min;
            count++;
        }
        return result + dist(point, points.get(0));
    }

    private static final class Point {

        private final double x;

        private final double y;

        private Point(final double x, final double y) {
            this.x = x;
            this.y = y;
        }

    }

    private double dist(final Point p1, final Point p2) {
        return Math.sqrt((Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)));
    }

}
