package coursera.third;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public final class PrimSpanningTree {

    private static final String SPACE_REGEX = "\\s+";

    public static void main(final String[] args) throws IOException {
        final List<Arc> arcs = Files.lines(new File("/Users/pavelpolubentcev/Downloads/edges.txt").toPath())
                .map(e -> e.split(SPACE_REGEX)).map(e -> new Arc(Integer.parseInt(e[0]), Integer.parseInt(e[1]), Integer.parseInt(e[2])))
                .collect(Collectors.toList());
        final Map<Integer, List<Arc>> graph = arcs.stream().map(e -> e.origin).distinct().collect(Collectors.toMap(e -> e, e -> new ArrayList<>()));
        for (final Arc arc: arcs) {
            graph.get(arc.origin).add(arc);
            graph.computeIfAbsent(arc.target, k -> new ArrayList<>());
            graph.get(arc.target).add(new Arc(arc.target, arc.origin, arc.weight));
        }
        final PrimSpanningTree primSpanningTree = new PrimSpanningTree();
        final List<Arc> mst = primSpanningTree.spanningTree(1, graph);
        System.out.println(mst.stream().mapToInt(e -> e.weight).sum());
    }

    public List<Arc> spanningTree(final int src, final Map<Integer, List<Arc>> graph) {
        final Map<Integer, List<Arc>> unvisited = new HashMap<>(graph);
        final Queue<Arc> queue = new PriorityQueue<>(Comparator.comparingInt(arc -> arc.weight));
        queue.addAll(unvisited.remove(src));
        final List<Arc> mst = new ArrayList<>();
        while (!unvisited.isEmpty()) {
            final Arc arc = queue.poll();
            final int vertex = arc.target;
            if (unvisited.containsKey(vertex)) {
                final List<Arc> outbounds = unvisited.remove(vertex);
                queue.addAll(outbounds);
                mst.add(arc);
            }
        }
        return mst;
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
