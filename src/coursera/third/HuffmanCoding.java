package coursera.third;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public final class HuffmanCoding {

    public static void main(final String[] args) throws IOException {
        final List<Integer> input = Files.lines(new File("/Users/pavelpolubentcev/Downloads/huffman.txt").toPath()).map(Integer::parseInt).collect(Collectors.toList());
        final Map<String, Integer> weights = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            weights.put(String.valueOf(i), input.get(i));
        }
        final HuffmanCoding huffmanCoding = new HuffmanCoding();
        final Node root = huffmanCoding.huffmanCodingTree(weights);
        final Map<String, String> characterStringMap = huffmanCoding.huffmanTable(root);
        final List<Integer> lengths = characterStringMap.values().stream().map(String::length).collect(Collectors.toList());
        System.out.println(lengths.stream().min(Comparator.comparingInt(e -> e)));
        System.out.println(lengths.stream().max(Comparator.comparingInt(e -> e)));
    }

    public Map<String, String> huffmanTable(final Node root) {
        final Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        final Queue<String> codes = new ArrayDeque<>();
        codes.add("");
        final Map<String, String> table = new HashMap<>();
        while (!queue.isEmpty()) {
            final Node node = queue.poll();
            final String code = codes.poll();
            if (node.left != null) {
                queue.add(node.left);
                codes.add(code + "0");
            }
            if (node.right != null) {
                queue.add(node.right);
                codes.add(code + "1");
            }
            if (node.left == null && node.right == null) {
                table.put(node.c, code);
            }
        }
        return table;
    }

    public Node huffmanCodingTree(final Map<String, Integer> frequencies) {
        final Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(e -> e.freq));
        queue.addAll(frequencies.entrySet().stream().map(e -> new Node(e.getKey(), e.getValue(),null, null)).collect(Collectors.toList()));
        while (queue.size() > 1) {
            final Node node1 = queue.poll();
            Node node2 = null;
            int node2Freq = 0;
            if (queue.size() > 0) {
                node2 = queue.poll();
                node2Freq = node2.freq;
            }
            final Node predecessor = new Node(null, node1.freq + node2Freq, node1, node2);
            queue.add(predecessor);
        }
        return queue.poll();
    }

    private static final class Node {

        private final String c;

        private final int freq;

        private final Node left;

        private final Node right;

        public Node(final String c, final int freq, final Node left, final Node right) {
            this.c = c;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

    }

}
