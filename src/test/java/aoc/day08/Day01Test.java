package aoc.day08;


import aoc.day01.Day01;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01Test {

    @Test
    public void testPart1(){
        // Given
        String input = "test";

        // When
        String result = new Day01().part1(input);

        // Then
        assertEquals(input, result);
    }

    @Test
    public void testPart2(){
        // Given
        String input = "test";

        // When
        String result = new Day01().part2(input);

        // Then
        assertEquals(input, result);
    }
}
