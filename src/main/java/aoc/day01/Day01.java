package aoc.day01;

import aoc.Day;

import java.util.List;

public class Day01 implements Day {
    public static final int DEFAULT_POSITION = 50;
    private static final int TRACK_SIZE = 100;

    @Override
    public String part1(String input) {
        int currentPosition = DEFAULT_POSITION;
        int stopsOnZero = 0;
        List<String> lines = List.of(input.split("\n"));
        for (String s : lines) {
            if (s.isEmpty()) continue;
            int temp = Integer.parseInt(s.substring(1));
            if (s.startsWith("L")) {
                currentPosition = (currentPosition - (temp % TRACK_SIZE)) % TRACK_SIZE;
                if (currentPosition < 0) currentPosition = TRACK_SIZE + currentPosition;
            } else {
                currentPosition = (currentPosition + (temp % TRACK_SIZE)) % TRACK_SIZE;
            }
            if (currentPosition == 0) stopsOnZero++;
        }
        return "Count: " + stopsOnZero;
    }

    @Override
    public String part2(String input) {
        int currentPosition = DEFAULT_POSITION;
        long totalZeroPasses = 0;

        for (String line : input.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            char direction = line.charAt(0);
            int steps = Integer.parseInt(line.substring(1));
            boolean isLeft = (direction == 'L');
            totalZeroPasses += countZeroPasses(currentPosition, steps, isLeft);
            if (isLeft) {
                currentPosition = Math.floorMod(currentPosition - steps, TRACK_SIZE);
            } else {
                currentPosition = (currentPosition + steps) % TRACK_SIZE;
            }
        }
        return "Count: " + totalZeroPasses;
    }

    private int countZeroPasses(int startPos, int steps, boolean isLeft) {
        if (steps <= 0) return 0;
        int stepsToFirstZero;
        if (!isLeft) {
            stepsToFirstZero = (Day01.TRACK_SIZE - startPos) % TRACK_SIZE;
        } else {
            stepsToFirstZero = startPos;
        }
        if (stepsToFirstZero == 0) stepsToFirstZero = TRACK_SIZE;
        if (steps < stepsToFirstZero) {
            return 0;
        }
        return 1 + (steps - stepsToFirstZero) / TRACK_SIZE;
    }

}
