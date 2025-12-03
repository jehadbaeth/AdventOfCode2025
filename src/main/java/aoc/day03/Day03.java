package aoc.day03;

import aoc.Day;

public class Day03 implements Day {

    @Override
    public String part1(String input) {
        String[] lines = input.split("\n");
        int total = 0;
        for(String line: lines){
            int best = -1;
            int maxFirst = line.charAt(0) - '0';
            for (int i = 1; i < line.length(); i++) {
                int currentDigit = line.charAt(i) - '0';
                int candidate = 10 * maxFirst + currentDigit;
                best = Math.max(best, candidate);
                maxFirst = Math.max(maxFirst, currentDigit);
            }
            //System.out.println(best);
            total += best;
        }
        return "Highest Jolt: " + total;
    }

    @Override
    public String part2(String input) {
        String[] lines = input.split("\n");
        long total = 0;
        for (String line : lines) {
            String best12 = largestSubsequenceOfLengthK(line, 12);
            //System.out.println(best12);
            total +=Long.parseLong(best12);
        }
        return "Highest Jolt: "+ total;
    }
    public String largestSubsequenceOfLengthK(String digits, int k) {
        int n = digits.length();
        int toRemove = n - k;
        StringBuilder stack = new StringBuilder();

        for (char c : digits.toCharArray()) {
            // While we can remove digits, and top of stack is smaller than current
            while (toRemove > 0 && !stack.isEmpty() && stack.charAt(stack.length() - 1) < c) {
                stack.deleteCharAt(stack.length() - 1);
                toRemove--;
            }
            stack.append(c);
        }

        // If we still need to remove digits (e.g., non-decreasing input), truncate
        while (toRemove > 0) {
            stack.deleteCharAt(stack.length() - 1);
            toRemove--;
        }

        return stack.substring(0, k);
    }

}
