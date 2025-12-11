package aoc.day11;

import aoc.Day;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Day11 implements Day {

    private static Map<String, List<String>> getGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] lines = input.split("\n");
        for (String line : lines) {
            String head = line.split(":")[0];
            List<String> tail = Arrays.stream(line.split(":")[1].split(" "))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            graph.put(head, tail);
        }
        return graph;
    }

    @Override
    public String part1(String input) {
        String[] lines = input.split("\n");
        Map<String, String[]> paths = new HashMap<>();
        for (String line : lines) {
            String head = line.split(":")[0];
            String[] tail = Arrays.stream(line.split(":")[1].split(" ")).filter(s -> !s.isEmpty()).toArray(String[]::new);
            paths.put(head, tail);
        }
//        paths.forEach((key, value) -> {
//            System.out.println(key);
//            System.out.println(Arrays.toString(value));
//        });
        int count = 0;
        Queue<String[]> queue = new LinkedList<>();
        queue.add(paths.get("you"));
        while (!queue.isEmpty()) {
            String[] current = queue.poll();
            for (String next : current) {
                if (next.equals("out")) count++;
                else {
                    if (paths.containsKey(next))
                        queue.add(paths.get(next));
                }
            }
        }

        return String.valueOf(count);

    }

    @Override
    public String part2(String input) {
        Map<String, List<String>> graph = getGraph(input);
        Map<String, Long> memo = new HashMap<>();

        String start = "svr";
        String end = "out";
        String c1 = "dac";
        String c2 = "fft";

        // svr -> dac -> fft -> out
        long n_s_c1 = countPaths(start, c1, graph, memo);
        long n_c1_c2 = countPaths(c1, c2, graph, memo);
        long n_c2_e = countPaths(c2, end, graph, memo);
        long countA = n_s_c1 * n_c1_c2 * n_c2_e;

        // svr -> fft -> dac -> out
        long n_s_c2 = countPaths(start, c2, graph, memo);
        long n_c2_c1 = countPaths(c2, c1, graph, memo);
        long n_c1_e = countPaths(c1, end, graph, memo);
        long countB = n_s_c2 * n_c2_c1 * n_c1_e;

        long totalPaths = countA + countB;
        return String.valueOf(totalPaths);
    }

    private long countPaths(String start, String end, Map<String, List<String>> graph, Map<String, Long> memo) {
        String key = start + "->" + end;
        if (memo.containsKey(key)) return memo.get(key);
        // Found the destination
        if (start.equals(end)) return 1L; // A single path of length zero from a node to itself
        // Dead end before reaching the destination
        if (!graph.containsKey(start)) return 0L;
        long pathCount = 0;
        for (String neighbor : graph.get(start)) {
            pathCount += countPaths(neighbor, end, graph, memo);
        }
        memo.put(key, pathCount);
        return pathCount;
    }

}
