package coursera.second;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public final class MedianMaintenance {

    public static void main(final String[] args) throws IOException {
        final List<Integer> nums = Files.lines(new File("/Users/pavelpolubentcev/Downloads/median.txt").toPath()).map(Integer::parseInt).collect(Collectors.toList());
        final HeapMedian heapMedian = new HeapMedian();
        int result = 0;
        for (final int num: nums) {
            result += heapMedian.median(num);
        }
        System.out.println(result % 10000);
        final TreeMedian treeMedian = new TreeMedian();
        result = 0;
        for (final int num: nums) {
            result += treeMedian.median(num);
        }
        System.out.println(result % 10000);
    }

    public static final class HeapMedian {

        private final Queue<Integer> low = new PriorityQueue<>(Comparator.reverseOrder());

        private final Queue<Integer> high = new PriorityQueue<>();

        private int size = 0;

        // O(logn)
        public int median(final int num) {
            if (size % 2 == 0) {
                low.add(num);
            } else {
                high.add(num);
            }
            size++;
            if (size > 1) {
                final int down = low.poll();
                final int up = high.poll();
                if (down > up) {
                    low.add(up);
                    high.add(down);
                } else {
                    low.add(down);
                    high.add(up);
                }
            }
            return low.peek();
        }

    }

    public static final class TreeMedian {

        private Node root = null;

        // O(height)
        public int median(final int num) {
            if (root == null) {
                root = new Node(num, null, null, 1);
                return num;
            }
            final Node parent = dfs(root, num);
            if (num < parent.value) {
                parent.left = new Node(num, null, null, 1);
            } else {
                parent.right = new Node(num, null, null, 1);
            }
            final int m = root.size % 2 == 0 ? root.size / 2 : (root.size + 1) / 2;
            return findMedian(root, m);
        }

        public Node dfs(final Node node, final int num) {
            node.size++;
            if (num < node.value) {
                return node.left == null ? node : dfs(node.left, num);
            }
            return node.right == null ? node : dfs(node.right, num);
        }

        public int findMedian(final Node node, final int m) {
            final int median;
            final int a = node.left != null ? node.left.size : 0;
            if (a == m - 1) {
                median = node.value;
            } else if (a >= m) {
                median = findMedian(node.left, m);
            } else {
                median = findMedian(node.right, m - a - 1);
            }
            return median;
        }

        public static final class Node {

            private final int value;

            private Node left;

            private Node right;

            private int size;

            public Node(final int value, final Node left, final Node right, final int size) {
                this.value = value;
                this.left = left;
                this.right = right;
                this.size = size;
            }

        }

    }

}