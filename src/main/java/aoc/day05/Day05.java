package aoc.day01;

import aoc.Day;
import java.util.Map;
import java.util.TreeMap;

// I'll try to avoid using Guava's RangeSet
class CustomRangeSet {

    private final TreeMap<Long, Long> intervals = new TreeMap<>();

    // -points for cheating while implementing this function.
    // I kep messing up exclusive and inclusive ranges
    public void addRange(long left, long right) {
        long newStart = left;
        long newEnd = right;

        Map.Entry<Long, Long> startEntry = intervals.floorEntry(left);

        if (startEntry != null) {
            long prevStart = startEntry.getKey();
            long prevEnd = startEntry.getValue();
            if (prevEnd >= left) {
                newStart = prevStart;
                newEnd = Math.max(newEnd, prevEnd);
                intervals.remove(prevStart);
            }
        }
        Map.Entry<Long, Long> currentEntry = intervals.ceilingEntry(newStart);

        while (currentEntry != null && currentEntry.getKey() <= newEnd) {
            newEnd = Math.max(newEnd, currentEntry.getValue());
            intervals.remove(currentEntry.getKey());
            currentEntry = intervals.ceilingEntry(newStart);
        }

        intervals.put(newStart, newEnd);
    }
    public long getElementsCount(){
        long total =0;
        for(Map.Entry<Long, Long> entry: intervals.entrySet()){
            total += entry.getValue() - entry.getKey();
        }
        return total;
    }

    public boolean containsValue(long value) {
        Map.Entry<Long, Long> entry = intervals.floorEntry(value);
        if (entry == null) return false;
        return value < entry.getValue();
    }
}

public class Day05 implements Day {

    @Override
    public String part1(String input) {
        int count = 0;
        CustomRangeSet rangeSet = new CustomRangeSet();
        String[] lines = input.split("\n");

        for (String line : lines) {
            if (line.isBlank()) continue;

            if (line.contains("-")) {
                String[] parts = line.split("-");
                long start = Long.parseLong(parts[0]);
                long end = Long.parseLong(parts[1]);
                rangeSet.addRange(start, end + 1);

            } else {
                if (rangeSet.containsValue(Long.parseLong(line))) {
                    count++;
                }
            }
        }
        return "Count: " + count;
    }

    @Override
    public String part2(String input) {
        int count = 0;
        CustomRangeSet rangeSet = new CustomRangeSet();
        String[] lines = input.split("\n");

        for (String line : lines) {
            if (line.isBlank()) continue;

            if (line.contains("-")) {
                String[] parts = line.split("-");
                long start = Long.parseLong(parts[0]);
                long end = Long.parseLong(parts[1]);
                rangeSet.addRange(start, end + 1);

            } else { // keep so we don't have to copy and paste the input again in tests.
                if (rangeSet.containsValue(Long.parseLong(line))) {
                    count++;
                }
            }
        }
        return "Count: " + rangeSet.getElementsCount();
    }
}