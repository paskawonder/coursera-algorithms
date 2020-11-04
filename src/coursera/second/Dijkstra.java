package coursera.second;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Dijkstra {

    private static final String SPACE_REGEX = "\\s+";

    private static final String COMMA_REGEX = ",";

    public static void main(String[] args) throws IOException {
        final List<String[]> lines = Files
                .lines(new File("/Users/pavelpolubentcev/Downloads/dijkstraData.txt").toPath())
                .map(e -> e.split(SPACE_REGEX)).collect(Collectors.toList());
        final Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        lines.forEach(strs -> {
            final int vertex = Integer.parseInt(strs[0]);
            final Map<Integer, Integer> outbound = new HashMap<>();
            for (int i = 1; i < strs.length; i++) {
                final String[] arc = strs[i].split(COMMA_REGEX);
                final int adj = Integer.parseInt(arc[0]);
                final int weight = Integer.parseInt(arc[1]);
                outbound.put(adj, weight);
                graph.computeIfAbsent(adj, k -> new HashMap<>()).put(vertex, weight);
            }
            graph.computeIfAbsent(vertex, k -> new HashMap<>()).putAll(outbound);
        });
        final Dijkstra dijkstra = new Dijkstra();
        final Map<Integer, Integer> distances = dijkstra.dijkstra(1, graph);
        Stream.of(7, 37, 59, 82, 99, 115, 133, 165, 188, 197).map(distances::get).forEach(System.out::println);
        Stream.of(7, 37, 59, 82, 99, 115, 133, 165, 188, 197).map(e -> dijkstra.dijkstra(1, e, graph)).forEach(System.out::println);
    }

    public Map<Integer, Integer> dijkstra(final int src, final Map<Integer, Map<Integer, Integer>> graph) {
        final Map<Integer, Integer> distances = new HashMap<>();
        distances.put(src, 0);
        final Queue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(src);
        while (!queue.isEmpty()) {
            final int vertex = queue.poll();
            final int dist = distances.get(vertex);
            for (final Map.Entry<Integer, Integer> entry : graph.get(vertex).entrySet()) {
                final int adj = entry.getKey();
                final int totalDist = dist + entry.getValue();
                if (totalDist < distances.getOrDefault(adj, totalDist + 1)) {
                    distances.put(adj, totalDist);
                    queue.add(adj);
                }
            }
        }
        return distances;
    }

    public int dijkstra(final int origin, final int target, final Map<Integer, Map<Integer, Integer>> graph) {
        final Map<Integer, Integer> distances = new HashMap<>();
        distances.put(origin, 0);
        final Queue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        queue.add(origin);
        while (!queue.isEmpty()) {
            final int vertex = queue.poll();
            final int dist = distances.get(vertex);
            if (vertex == target) {
                return dist;
            }
            for (final Map.Entry<Integer, Integer> entry : graph.get(vertex).entrySet()) {
                final int adj = entry.getKey();
                final int totalDist = dist + entry.getValue();
                if (totalDist < distances.getOrDefault(adj, totalDist + 1)) {
                    distances.put(adj, totalDist);
                    queue.add(adj);
                }
            }
        }
        throw new IllegalStateException();
    }

}
