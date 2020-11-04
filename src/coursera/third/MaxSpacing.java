package coursera.third;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/*
    Note: can be found as (PrimsSpanningTree - 4 biggest edges) as well
*/

public final class MaxSpacing {

    private static final String SPACE_REGEX = "\\s+";

    public static void main(final String[] args) throws IOException {
        final List<Arc> tmpArcs = Files.lines(new File("/Users/pavelpolubentcev/Downloads/clustering1.txt").toPath())
                .map(e -> e.split(SPACE_REGEX)).map(e -> new Arc(Integer.parseInt(e[0]), Integer.parseInt(e[1]), Integer.parseInt(e[2])))
                .collect(Collectors.toList());
        final List<Arc> arcs = new ArrayList<>(tmpArcs);
        for (final Arc arc: tmpArcs) {
            arcs.add(new Arc(arc.target, arc.origin, arc.weight));
        }
        final MaxSpacing maxSpacing = new MaxSpacing();
        final int result = maxSpacing.maxSpacing(4, arcs);
        System.out.println(result);
    }

    public int maxSpacing(final int k, final List<Arc> arcs) {
        final Map<Integer, Set<Integer>> clusters = arcs.stream().map(e -> e.origin).distinct().collect(Collectors.toMap(e -> e, e -> new HashSet<>(Collections.singletonList(e))));
        final Map<Integer, Integer> ufTable = arcs.stream().map(e -> e.origin).distinct().collect(Collectors.toMap(e -> e, e -> e));
        final Queue<Arc> queue = new PriorityQueue<>(Comparator.comparingInt(arc -> arc.weight));
        queue.addAll(arcs);
        int result = 0;
        while (clusters.size() >= k) {
            final Arc arc = queue.poll();
            int origin = ufTable.get(arc.origin);
            int target = ufTable.get(arc.target);
            if (origin != target) {
                result = arc.weight;
                Set<Integer> to = clusters.get(origin);
                Set<Integer> from = clusters.get(target);
                if (from.size() > to.size()) {
                    final int tmpVertex = target;
                    target = origin;
                    origin = tmpVertex;
                    final Set<Integer> tmp = from;
                    from = to;
                    to = tmp;
                }
                to.addAll(from);
                for (final int vertex: clusters.remove(target)) {
                    ufTable.put(vertex, origin);
                }
            }
        }
        return result;
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
