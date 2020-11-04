package coursera.first;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public final class MinCut {

    private static final String SPACE_REGEX = "\\s+";

    private final Random random = new Random();

    public static void main(String[] args) throws IOException {
        final MinCut minCut = new MinCut();
        final Map<Integer, Set<Integer>> graph = minCut.buildAdjListGraph("/Users/pavelpolubentcev/Downloads/KargerMinCut.txt");
        int minCutResult = minCut.minCutLoop(graph);
        System.out.println(minCutResult);
    }
    
    public int minCutLoop(final Map<Integer, Set<Integer>> inputGraph) {
        int min = Integer.MAX_VALUE;
        final double n = Math.pow(inputGraph.size(), 2);
        for (int i = 0; i < n; i++) {
            min = Math.min(min, minCut(inputGraph));
        }
        return min;
    }

    public int minCut(final Map<Integer, Set<Integer>> inputGraph) {
        final Map<Integer, Map<Integer, Integer>> graph = inputGraph.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().collect(Collectors.toMap(e -> e, e -> 1)))
                );
        while (graph.size() > 2) {
            final int[] vertices = get2RandomAdjs(graph);
            final int u = vertices[0], v = vertices[1];
            final Map<Integer, Integer> uAdjs = graph.get(u);
            uAdjs.remove(v);
            for (final Map.Entry<Integer, Integer> entry: graph.remove(v).entrySet()) {
                final int adj = entry.getKey();
                graph.get(adj).remove(v);
                if (adj != u) {
                    final int count = entry.getValue() + uAdjs.getOrDefault(adj, 0);
                    uAdjs.put(adj, count);
                    graph.get(adj).put(u, count);
                }
            }
        }
        return graph.values().stream().findAny().get().values().stream().findAny().get();
    }

    public int[] get2RandomAdjs(final Map<Integer, Map<Integer, Integer>> graph) {
        final int[] adjs = new int[2];
        List<Integer> vertices = new ArrayList<>(graph.keySet());
        int randomNumber = random.nextInt(vertices.size());
        adjs[0] = vertices.get(randomNumber);
        vertices = new ArrayList<>(graph.get(adjs[0]).keySet());
        randomNumber = random.nextInt(vertices.size());
        adjs[1] = vertices.get(randomNumber);
        if (graph.get(adjs[0]).size() > graph.get(adjs[1]).size()) {
            final int tmp = adjs[0];
            adjs[0] = adjs[1];
            adjs[1] = tmp;
        }
        return adjs;
    }

    public Map<Integer, Set<Integer>> buildAdjListGraph(final String filePath) throws IOException {
        final List<String[]> lines = Files.lines(new File(filePath).toPath())
                .map(e -> e.split(SPACE_REGEX)).collect(Collectors.toList());
        final Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (final String[] arr: lines) {
            final int src = Integer.parseInt(arr[0]);
            final Set<Integer> adjs = new HashSet<>();
            for (int i = 1; i < arr.length; i++) {
                final int vertex = Integer.parseInt(arr[i]);
                adjs.add(vertex);
                graph.computeIfAbsent(vertex, k -> new HashSet<>());
                graph.get(vertex).add(src);
            }
            graph.computeIfAbsent(src, k -> new HashSet<>());
            graph.get(src).addAll(adjs);
        }
        return graph;
    }

}
