package coursera.fourth;

import coursera.second.SCC;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SAT2 {

    private static final String SPACE_REGEX = "\\s+";

    private final SCC scc;

    public SAT2(final SCC scc) {
        this.scc = scc;
    }

    public static void main(String[] args) throws IOException {
        final SCC scc = new SCC();
        final SAT2 sat2 = new SAT2(scc);
        for (int i = 1; i < 7; i++) {
            final List<String[]> strs = Files
                    .lines(new File("/Users/pavelpolubentcev/Downloads/2sat" + i + ".txt").toPath())
                    .map(e -> e.split(SPACE_REGEX)).collect(Collectors.toList());
            final Map<Integer, Set<Integer>> graph = new HashMap<>();
            strs.forEach(e -> {
                final int v1 = Integer.parseInt(e[0]);
                final int v2 = Integer.parseInt(e[1]);
                Stream.of(-v1, v1, -v2, v2).forEach(v -> graph.computeIfAbsent(v, k -> new HashSet<>()));
                graph.get(-v1).add(v2);
                graph.get(-v2).add(v1);
            });
            final boolean sat2Result = sat2.sat2(graph);
            System.out.println(sat2Result);
        }
    }

    public boolean sat2(final Map<Integer, Set<Integer>> graph) {
        return scc.sccs(graph).stream().noneMatch(scc -> scc.stream().anyMatch(vertex -> scc.contains(-vertex)));
    }

}
