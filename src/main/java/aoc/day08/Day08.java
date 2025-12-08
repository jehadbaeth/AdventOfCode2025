package aoc.day08;

import aoc.Day;
import aoc.Utils;

import java.util.*;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class Day08 implements Day {

    @Override
    public String part1(String input) {
        List<long[]> points = new ArrayList<>();
        for (String line : input.split("\\R")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            long x = Long.parseLong(parts[0].trim());
            long y = Long.parseLong(parts[1].trim());
            long z = Long.parseLong(parts[2].trim());
            points.add(new long[]{x, y, z});
        }

        int n = points.size();
        if (n == 0) return "0";

        List<long[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = points.get(i)[0] - points.get(j)[0];
                long dy = points.get(i)[1] - points.get(j)[1];
                long dz = points.get(i)[2] - points.get(j)[2];
                long distSq = dx*dx + dy*dy + dz*dz;
                edges.add(new long[]{distSq, i, j});
            }
        }

        edges.sort(Comparator.comparingLong(e -> e[0]));

        // Use 10 for the example (n=20), 1000 for real
        int limit = (n == 20) ? 10 : 1000;
        limit = Math.min(limit, edges.size());

        int[] parent = new int[n];
        int[] size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Find with path compression
        java.util.function.IntUnaryOperator find = new java.util.function.IntUnaryOperator() {
            public int applyAsInt(int x) {
                if (parent[x] != x) {
                    parent[x] = this.applyAsInt(parent[x]);
                }
                return parent[x];
            }
        };

        for (int idx = 0; idx < limit; idx++) {
            long[] edge = edges.get(idx);
            int i = (int) edge[1];
            int j = (int) edge[2];
            int ri = find.applyAsInt(i);
            int rj = find.applyAsInt(j);
            if (ri != rj) {
                if (size[ri] < size[rj]) {
                    parent[ri] = rj;
                    size[rj] += size[ri];
                } else {
                    parent[rj] = ri;
                    size[ri] += size[rj];
                }
            }
        }

        // Count component sizes
        Map<Integer, Integer> compSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find.applyAsInt(i);
            compSizes.put(root, compSizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(compSizes.values());
        sizes.sort(Collections.reverseOrder());

        while (sizes.size() < 3) sizes.add(1);
        long result = (long) sizes.get(0) * sizes.get(1) * sizes.get(2);
        return String.valueOf(result);
    }

    @Override
    public String part2(String input) {
        List<long[]> points = new ArrayList<>();
        for (String line : input.split("\\R")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            long x = Long.parseLong(parts[0].trim());
            long y = Long.parseLong(parts[1].trim());
            long z = Long.parseLong(parts[2].trim());
            points.add(new long[]{x, y, z});
        }

        int n = points.size();
        if (n <= 1) return "0";

        // Generate all edges (distSq, i, j)
        List<long[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = points.get(i)[0] - points.get(j)[0];
                long dy = points.get(i)[1] - points.get(j)[1];
                long dz = points.get(i)[2] - points.get(j)[2];
                long distSq = dx * dx + dy * dy + dz * dz;
                edges.add(new long[]{distSq, i, j});
            }
        }

        edges.sort(Comparator.comparingLong(e -> e[0]));

        // Union-Find setup
        int[] parent = new int[n];
        int[] size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Find with path compression
       IntUnaryOperator find = new IntUnaryOperator() {
            public int applyAsInt(int x) {
                if (parent[x] != x) {
                    parent[x] = this.applyAsInt(parent[x]);
                }
                return parent[x];
            }
        };

        int components = n;
        long lastX1 = 0, lastX2 = 0;

        for (long[] edge : edges) {
            if (components == 1) break;

            int i = (int) edge[1];
            int j = (int) edge[2];
            int ri = find.applyAsInt(i);
            int rj = find.applyAsInt(j);

            if (ri != rj) {
                // This is a valid connection
                lastX1 = points.get(i)[0];
                lastX2 = points.get(j)[0];

                // Union
                if (size[ri] < size[rj]) {
                    parent[ri] = rj;
                    size[rj] += size[ri];
                } else {
                    parent[rj] = ri;
                    size[ri] += size[rj];
                }

                components--;

                if (components == 1) {
                    break;
                }
            }
        }

        return String.valueOf(lastX1 * lastX2);
    }

}
