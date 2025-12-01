package aoc.day01;


import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01Test {

    @Test
    public void testPart1(){
        // Given
        String input = """
                L68
                L30
                R48
                L5
                R60
                L55
                L1
                L99
                R14
                L82""";
        String expected = "Count: 3";

        // When
        String result = new Day01().part1(input);

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void testPart2(){
        // Given
        String input = """
                L68
                L30
                R48
                L5
                R60
                L55
                L1
                L99
                R14
                L82""";

        // When
        String result = new Day01().part2(input);
        String expected = "Count: 6";

        // Then
        assertEquals(expected, result);
    }
}
