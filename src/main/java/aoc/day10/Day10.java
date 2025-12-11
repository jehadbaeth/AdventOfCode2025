package aoc.day10;

import aoc.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 implements Day {


    @Override
    public String part1(String input) {
        int total = 0;
        for (String line : input.split("\n")) {
            int[] current = getCurrent(line);
            List<int[]> buttons = getButtonGroups(line);
            int[] goal = new int[current.length]; // all zeros

            int presses = dfs(buttons, current, goal, 0);
            total += presses;
            //System.out.println("Current Count: " + total);
        }
        return String.valueOf(total);
    }

    public int dfs(List<int[]> buttons, int[] current, int[] goal, int start) {
        if (Arrays.equals(current, goal)) {
            return 0;
        }
        if (start >= buttons.size()) {
            return -1; // no more buttons
        }

        // skip current
        int skip = dfs(buttons, current, goal, start + 1);

        // press current
        int[] group = buttons.get(start);
        flip(current, group);
        int use = dfs(buttons, current, goal, start + 1);
        flip(current, group); // unpress


        // this was the hardest part to figure out :(
        if (use == -1 && skip == -1) return -1;
        if (skip == -1) return use + 1;
        if (use == -1) return skip;
        return Math.min(skip, use + 1);
    }

    private void flip(int[] state, int[] buttons) {
        for (int idx : buttons) {
            state[idx] = 1 - state[idx];
        }
    }
    // I have no idea how to approach this
    @Override
    public String part2(String input) {
        return "Defeat!";
    }

    //parsing functions.
    public int[] getCurrent(String line) {
        String part1Str = line.replaceAll("^\\[(.*?)].*", "$1");
        return part1Str.chars()
                .map(c -> c == '#' ? 1 : 0)
                .toArray();
    }

    public List<int[]> getButtonGroups(String line) {
        List<int[]> part2 = new ArrayList<>();
        Matcher m2 = Pattern.compile("\\(([^)]+)\\)").matcher(line);
        while (m2.find()) {
            String[] tokens = m2.group(1).split(",");
            int[] numbers = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                numbers[i] = Integer.parseInt(tokens[i].trim());
            }
            part2.add(numbers);
        }
        return part2;
    }

    public int[] getRequirements(String line) {
        String part3Str = line.replaceAll(".*\\{(.*)}.*", "$1");
        return Arrays.stream(part3Str.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

}
