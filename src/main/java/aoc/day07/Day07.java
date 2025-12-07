package aoc.day07;

import aoc.Day;

import java.util.*;

public class Day07 implements Day {
    private static int height, width;

    @Override
    public String part1(String input) {
        String[] lines = input.split("\n");
        int splits = 0;
        int start = lines[0].indexOf('S');
        Set<Integer> beams = new HashSet<>();
        beams.add(start);
        for (int i = 1; i < lines.length; i++) {
            if (!lines[i].contains("^")) continue;
            Set<Integer> toAdd = new HashSet<>();
            Set<Integer> toRemove = new HashSet<>();

            for (int beam : beams) {
                if (lines[i].charAt(beam) == '^') {
                    splits++;
                    toRemove.add(beam);
                    toAdd.add(beam + 1);
                    toAdd.add(beam - 1);
                }
            }
            beams.removeAll(toRemove);
            beams.addAll(toAdd);
        }
        return "Result:" + splits;
    }


    @Override
    public String part2(String input) {
        List<String> grid = Arrays.asList(input.split("\n"));
        height = grid.size();
        width = grid.getFirst().length();
        Map<String, Long> memo = new HashMap<>();
        int startCol = grid.getFirst().indexOf('S');
        return "" + countPaths(0, startCol, memo, grid);
    }

    private static long countPaths(int h, int w, Map<String, Long> memo, List<String> grid) {

        if (w < 0 || w >= width) {  // case 1 out of bounds
            return 0;
        }
        if (h == height - 1) { // case 2 bottom row
            return 1;
        }
        String key = h + "," + w;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        long currentCount = 0;
        char cell = grid.get(h).charAt(w);

        if (cell == '.' || cell == 'S') { // move down straight
            currentCount = countPaths(h + 1, w, memo, grid);
        } else if (cell == '^') { // split left and right
            currentCount = countPaths(h + 1, w - 1, memo, grid) + countPaths(h + 1, w + 1, memo, grid);
        }
        memo.put(key, currentCount);
        return currentCount;
    }

}
