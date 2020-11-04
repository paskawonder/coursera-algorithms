package coursera.fourth;

import coursera.second.Dijkstra;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class AllPairsShortestPath {

    private static final String SPACE_REGEX = "\\s+";

    public static void main(String[] args) throws IOException {
        final Johnsons johnsons = new Johnsons(new BellmanFord(), new Dijkstra());
        for (int i = 1; i <= 3; i++) {
            final List<String[]> lines = Files
                    .lines(new File("/Users/pavelpolubentcev/Downloads/g" + i + ".txt").toPath())
                    .map(e -> e.split(SPACE_REGEX)).collect(Collectors.toList());
            final Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
            lines.forEach(strs -> {
                final int origin = Integer.parseInt(strs[0]);
                final int target = Integer.parseInt(strs[1]);
                final int weight = Integer.parseInt(strs[2]);
                graph.computeIfAbsent(origin, k -> new HashMap<>());
                graph.get(origin).put(target, weight);
            });
            final Map<Integer, Map<Integer, Integer>> allPairsShortestPaths = johnsons.calculate(graph);
            System.out.println(allPairsShortestPaths.entrySet().stream().flatMap(e -> e.getValue().values().stream()).min(Comparator.comparingInt(e -> e)));
        }
        final List<String[]> lines = Files
                .lines(new File("/Users/pavelpolubentcev/Downloads/large.txt").toPath())
                .map(e -> e.split(SPACE_REGEX)).collect(Collectors.toList());
        final Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        lines.forEach(strs -> {
            final int origin = Integer.parseInt(strs[0]);
            final int target = Integer.parseInt(strs[1]);
            final int weight = Integer.parseInt(strs[2]);
            graph.computeIfAbsent(origin, k -> new HashMap<>());
            graph.get(origin).put(target, weight);
        });
        final Map<Integer, Map<Integer, Integer>> allPairsShortestPaths = johnsons.calculate(graph);
        System.out.println(allPairsShortestPaths.entrySet().stream().flatMap(e -> e.getValue().values().stream()).min(Comparator.comparingInt(e -> e)));
    }

    private static final class Johnsons {

        private final BellmanFord bellmanFord;

        private final Dijkstra dijkstra;

        public Johnsons(final BellmanFord bellmanFord, final Dijkstra dijkstra) {
            this.bellmanFord = bellmanFord;
            this.dijkstra = dijkstra;
        }

        public Map<Integer, Map<Integer, Integer>> calculate(final Map<Integer, Map<Integer, Integer>> graph) {
            final Map<Integer, Map<Integer, Integer>> copy = graph.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
            copy.put(0, graph.keySet().stream().collect(Collectors.toMap(e -> e, e -> 0)));
            final Map<Integer, Integer> bf = bellmanFord.calculate(0, copy);
            if (bf.isEmpty()) {
                return Collections.emptyMap();
            }
            copy.remove(0);
            copy.replaceAll((vertex, outbound) -> outbound.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() + bf.get(vertex) - bf.get(e.getKey()))));
            final Map<Integer, Map<Integer, Integer>> result = copy.keySet().stream().collect(Collectors.toMap(vertex -> vertex, vertex -> dijkstra.dijkstra(vertex, copy)));
            result.replaceAll((vertex, outbound) -> outbound.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() - bf.get(vertex) + bf.get(e.getKey()))));
            return result;
        }

    }

    private static final class BellmanFord {

        public Map<Integer, Integer> calculate(final int src, final Map<Integer, Map<Integer, Integer>> graph) {
            final Map<Integer, Integer> dp = new HashMap<>();
            dp.put(src, 0);
            final List<Arc> arcs = graph.entrySet().stream().flatMap(e -> e.getValue().entrySet().stream().map(outbound -> new Arc(e.getKey(), outbound.getKey(), outbound.getValue()))).collect(Collectors.toList());
            for (int i = 1, n = graph.size(); i < n; i++) {
                for (final Arc arc: arcs) {
                    final int origin = arc.origin;
                    final int target = arc.target;
                    final int distance = dp.get(origin) + arc.weight;
                    if (dp.containsKey(origin) && distance < dp.getOrDefault(target, distance + 1)) {
                        dp.put(target, distance);
                    }
                }
            }
            return arcs.stream().anyMatch(arc -> dp.get(arc.target) > dp.get(arc.origin) + arc.weight) ? Collections.emptyMap() : dp;
        }

        private static final class Arc {

            private final int origin;

            private final int target;

            private final int weight;

            public Arc(final int origin, final int target, final int weight) {
                this.origin = origin;
                this.target = target;
                this.weight = weight;
            }

        }

    }

}
