package aoc.day02;

import aoc.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day02 implements Day {

    @Override
    public String part1(String input) {
        String[] ranges = input.split(",");
        long total = 0;
        for (String range : ranges) {
            String[] bounds = range.split("-");
            long start = Long.parseLong(bounds[0]);
            long end = Long.parseLong(bounds[1]);
            System.out.println("Start: " + start + " and Ends at: " + end);
            for (long i = start; i <= end; i++) {
                if (isRepeating(i)) total += i;
            }
        }
        return "Total is: " + total;
    }

    public boolean isRepeating(long num) {
        String s = String.valueOf(num);
        if (s.length() % 2 != 0) return false; // can't split evenly → not repeating
        int mid = s.length() / 2;
        return s.substring(0, mid).equals(s.substring(mid));
    }

    @Override
    public String part2(String input) {
        //test equality of substrings of all common divisors of current num.length  instead of just splitting the number in half
        String[] ranges = input.split(",");
        long total = 0;
        for (String range : ranges) {
            String[] bounds = range.split("-");
            long start = Long.parseLong(bounds[0]);
            long end = Long.parseLong(bounds[1]);
            System.out.println("Start: " + start + " and Ends at: " + end);
            for (long i = start; i <= end; i++) {
                if (isValidPartTwo(i)) total += i;
            }
        }
        return "Total is: " + total;
    }

    public boolean isValidPartTwo(long num) {
        String number = String.valueOf(num);
        List<Integer> parts = getCommonDivisors(number.length());
        for (int i : parts) {
            String[] segments = splitStringIntoEqualParts(number, i);
            if (areAllElementsEqual(segments)) return true;
        }
        return false;
    }

    public List<Integer> getCommonDivisors(long num) {
        List<Integer> divisors = new ArrayList<>();
        for (int i = 2; i <= num; i++) { // we skip one as we don't consider the digit itself
            if (num % i == 0) divisors.add(i);
        }
        return divisors;
    }

    public boolean areAllElementsEqual(String[] arr) {
        return arr.length == 0 || Arrays.stream(arr).distinct().count() <= 1;
    }

    public static String[] splitStringIntoEqualParts(String str, int parts) {
        int partLength = str.length() / parts;
        String[] result = new String[parts];

        for (int i = 0; i < parts; i++) {
            result[i] = str.substring(i * partLength, (i + 1) * partLength);
        }

        return result;
    }

}
