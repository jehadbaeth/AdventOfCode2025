package aoc.day06;

import aoc.Day;
import aoc.Utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day06 implements Day {

    @Override
    public String part1(String input) {
        List<Stack<String>> column = new ArrayList<>();
        String[] lines = input.split("\n");
        String[] firstLine = Arrays.stream(lines[0].split(" ")).filter(s -> !s.isEmpty()).toArray(String[]::new);
        for(int i =0; i < firstLine.length; i++) column.add(i, new Stack<>());
        for(String line: lines){
            String[] columns = Arrays.stream(line.split(" ")).filter(s -> !s.isEmpty()).toArray(String[]::new);
            for(int i =0; i < columns.length; i++){
                column.get(i).push(columns[i]);
            }
        }
        long total =0;
        for(Stack<String> seri: column){

            String op = seri.pop();
            long current = Long.parseLong(seri.pop());
            while(!seri.isEmpty()){
                if(op.equals("*")) current *= Long.parseLong(seri.pop());
                else current += Long.parseLong(seri.pop());
            }
            total += current;
        }
        return "Total is: "+total;
    }
        // Yes, I cheated on Part 2 like hell :(
        @Override
        public String part2(String input) {

            if (input == null || input.trim().isEmpty()) {
                return "Total is: "+0;
            }

            // Split input into lines, handling both \n and \r\n
            String[] rawLines = input.split("\r\n|\r|\n");
            // preserve original spacing; don't trim
            List<String> lines = new ArrayList<>(Arrays.asList(rawLines));

            if (lines.size() < 2) {
                return "Total is: "+0; // need at least one digit row and one operator row
            }

            // Determine max width to normalize all rows
            int width = 0;
            for (String line : lines) {
                width = Math.max(width, line.length());
            }

            int rowCount = lines.size();
            int digitRows = rowCount - 1;

            // Build grid, padding shorter lines with spaces
            char[][] grid = new char[rowCount][width];
            for (int r = 0; r < rowCount; r++) {
                String line = lines.get(r);
                for (int c = 0; c < width; c++) {
                    if (c < line.length()) {
                        grid[r][c] = line.charAt(c);
                    } else {
                        grid[r][c] = ' ';
                    }
                }
            }

            // Identify separator columns: all spaces in digit rows (0 to digitRows-1)
            boolean[] isSeparator = new boolean[width];
            for (int c = 0; c < width; c++) {
                boolean allSpace = true;
                for (int r = 0; r < digitRows; r++) {
                    if (grid[r][c] != ' ') {
                        allSpace = false;
                        break;
                    }
                }
                isSeparator[c] = allSpace;
            }

            // Group contiguous non-separator columns
            List<List<Integer>> groups = new ArrayList<>();
            for (int c = 0; c < width; ) {
                if (isSeparator[c]) {
                    c++;
                    continue;
                }
                List<Integer> group = new ArrayList<>();
                while (c < width && !isSeparator[c]) {
                    group.add(c);
                    c++;
                }
                groups.add(group);
            }

            long grandTotal = 0;

            for (List<Integer> group : groups) {
                List<Long> numbers = new ArrayList<>();
                for (int col : group) {
                    StringBuilder numStr = new StringBuilder();
                    for (int r = 0; r < digitRows; r++) {
                        char ch = grid[r][col];
                        if (ch != ' ') {
                            numStr.append(ch);
                        }
                    }
                    if (numStr.isEmpty()) {
                        numbers.add(0L);
                    } else {
                        try {
                            numbers.add(Long.parseLong(numStr.toString()));
                        } catch (NumberFormatException e) {
                            // Should not happen if input is valid
                            numbers.add(0L);
                        }
                    }
                }

                char op = grid[digitRows][group.getFirst()]; // operator in first column of the group

                long result;
                if (op == '+') {
                    result = 0;
                    for (long n : numbers) {
                        result += n;
                    }
                } else if (op == '*') {
                    result = 1;
                    for (long n : numbers) {
                        result *= n;
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported operator: " + op);
                }

                grandTotal += result;
            }

            return "Total is: "+grandTotal;
        }


}
