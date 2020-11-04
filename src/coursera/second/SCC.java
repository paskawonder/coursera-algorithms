package coursera.second;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public final class SCC {

    private static final String SPACE_REGEX = "\\s+";

    public static void main(String[] args) throws IOException {
        final SCC scc = new SCC();
        final List<String[]> strs = Files
                .lines(new File("/Users/pavelpolubentcev/Downloads/SCC.txt").toPath())
                .map(e -> e.split(SPACE_REGEX)).collect(Collectors.toList());
        final Map<Integer, Set<Integer>> graph = new HashMap<>();
        strs.forEach(e -> {
            final int target = Integer.parseInt(e[1]);
            graph.computeIfAbsent(Integer.parseInt(e[0]), k -> new HashSet<>()).add(target);
            graph.computeIfAbsent(target, k -> new HashSet<>());
        });
        final List<Set<Integer>> sccs = scc.sccs(graph);
        final Queue<Integer> biggest = new PriorityQueue<>(Comparator.comparingInt((Integer e) -> e).reversed());
        biggest.addAll(sccs.stream().map(Set::size).collect(Collectors.toList()));
        for (int i = 0; i < 5; i++) {
            System.out.println(biggest.poll());
        }
    }

    public List<Set<Integer>> sccs(final Map<Integer, Set<Integer>> graph) {
        final Deque<Integer> stack = new ArrayDeque<>();
        final Set<Integer> visited = new HashSet<>();
        graph.keySet().forEach(k -> dfs(k, graph, visited, stack));
        final Map<Integer, Set<Integer>> reversed = graph.keySet().stream().collect(Collectors.toMap(e -> e, e -> new HashSet<>()));
        graph.forEach((vertex, adjs) -> adjs.forEach(adj -> reversed.get(adj).add(vertex)));
        final List<Set<Integer>> sccs = new ArrayList<>();
        while (!stack.isEmpty()) {
            final int vertex = stack.pop();
            if (reversed.containsKey(vertex)) {
                final Set<Integer> scc = new HashSet<>();
                dfs(vertex, reversed, scc);
                sccs.add(scc);
            }
        }
        return sccs;
    }

    public void dfs(final int vertex,
                    final Map<Integer, Set<Integer>> graph, final Set<Integer> visited, final Deque<Integer> stack) {
        if (visited.contains(vertex)) {
            return;
        }
        visited.add(vertex);
        graph.get(vertex).forEach(adj -> dfs(adj, graph, visited, stack));
        stack.push(vertex);
    }

    public void dfs(final int vertex, final Map<Integer, Set<Integer>> graph, final Set<Integer> scc) {
        if (!graph.containsKey(vertex)) {
            return;
        }
        scc.add(vertex);
        graph.remove(vertex).forEach(adj -> dfs(adj, graph, scc));
    }

}
