package coursera.third;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class HammingDistance {

    private static final String SPACE_REGEX = " ";

    public static void main(final String[] args) throws IOException {
        final int n = 24;
        final Set<String> vertices = Files.lines(new File("/Users/pavelpolubentcev/Downloads/clustering_big.txt").toPath()).map(e -> e.replace(SPACE_REGEX, "")).collect(Collectors.toSet());
        final List<Arc> arcs = new ArrayList<>();
        for (final String vertex : vertices) {
            final List<String> neighbors = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    final StringBuilder sb = new StringBuilder(vertex);
                    sb.setCharAt(i, vertex.charAt(i) == '0' ? '1' : '0');
                    sb.setCharAt(j, vertex.charAt(j) == '0' ? '1' : '0');
                    neighbors.add(sb.toString());
                }
            }
            neighbors.stream().filter(vertices::contains).forEach(adj -> arcs.add(new Arc(vertex, adj)));
        }
        final HammingDistance hammingDistance = new HammingDistance();
        final int result = hammingDistance.maxSpacing(vertices, arcs);
        System.out.println(result);
    }

    public int maxSpacing(final Set<String> vertices, final List<Arc> arcs) {
        final Map<String, Set<String>> clusters = vertices.stream().collect(Collectors.toMap(v -> v, v -> new HashSet<>(Collections.singletonList(v))));
        final Map<String, String> ufTable = vertices.stream().collect(Collectors.toMap(v -> v, v -> v));
        for (final Arc arc : arcs) {
            String origin = ufTable.get(arc.origin);
            String target = ufTable.get(arc.target);
            if (!origin.equals(target)) {
                Set<String> to = clusters.get(origin);
                Set<String> from = clusters.get(target);
                if (from.size() > to.size()) {
                    final String tmpVertex = target;
                    target = origin;
                    origin = tmpVertex;
                    final Set<String> tmp = from;
                    from = to;
                    to = tmp;
                }
                to.addAll(from);
                for (final String vertex : clusters.remove(target)) {
                    ufTable.put(vertex, origin);
                }
            }
        }
        return clusters.size();
    }

    private static final class Arc {

        private final String origin;

        private final String target;

        public Arc(final String origin, final String target) {
            this.origin = origin;
            this.target = target;
        }

    }

}

