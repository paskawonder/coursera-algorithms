package coursera.second;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Sum2 {

    public static void main(String[] args) throws IOException {
        final List<Long> nums = Files.lines(new File("/Users/pavelpolubentcev/Downloads/2sum.txt").toPath()).map(Long::parseLong).collect(Collectors.toList());
        final Sum2 sum2 = new Sum2();
        final Map<Long, Integer> map = nums.stream().collect(Collectors.toMap(num -> num, num -> 1, Integer::sum));
        final int result = (int) IntStream.range(-10000, 10001).filter(target -> sum2.sum2(map, target)).count();
        System.out.println(result);
    }

    public boolean sum2(final Map<Long, Integer> map, final int target) {
        return map.keySet().stream().anyMatch(num -> {
            final long pair = target - num;
            return pair == 0 || (map.containsKey(pair) && (num != pair || map.get(pair) > 1));
        });
    }

}
