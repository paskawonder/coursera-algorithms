package coursera.third;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class JobScheduler {

    private static final String SPACE_REGEX = "\\s+";

    public static void main(String[] args) throws IOException {
        final List<Job> jobs = Files.lines(new File("/Users/pavelpolubentcev/Downloads/jobs.txt").toPath())
                .map(e -> e.split(SPACE_REGEX)).map(e -> new Job(Integer.parseInt(e[0]), Integer.parseInt(e[1]))).collect(Collectors.toList());
        final JobScheduler jobSchedulerDiff = new JobScheduler(getDiffComparator());
        long sum = jobSchedulerDiff.schedule(jobs);
        System.out.println(sum);
        final JobScheduler jobSchedulerOptimal = new JobScheduler(getRatioComparator());
        sum = jobSchedulerOptimal.schedule(jobs);
        System.out.println(sum);
    }

    private final Comparator<Job> comparator;

    public JobScheduler(final Comparator<Job> comparator) {
        this.comparator = comparator;
    }

    public long schedule(final List<Job> jobs) {
        final List<Job> sorted = new ArrayList<>(jobs);
        sorted.sort(comparator);
        long sum = 0;
        int completionTime = 0;
        for (final Job job: sorted) {
            completionTime += job.length;
            sum += completionTime * job.weight;
        }
        return sum;
    }

    private static final class Job {

        private final int weight;

        private final int length;

        public Job(final int weight, final int length) {
            this.weight = weight;
            this.length = length;
        }

    }

    private static Comparator<Job> getDiffComparator() {
        return Comparator.comparingInt((Job job) -> job.weight - job.length).thenComparing(job -> job.weight).reversed();
    }

    private static Comparator<Job> getRatioComparator() {
        return Comparator.comparingDouble((Job job) -> job.weight / (double) job.length).reversed();
    }

}
