package aoc.day04;

import aoc.Day;

import java.util.ArrayList;
import java.util.List;

class Point{
    int x ,y;
    public Point(int x, int y){
        this.x= x;
        this.y = y;
    }
}

public class Day04 implements Day {

    @Override
    public String part1(String input) {
        char[][] charMap = convertToCharArray(input);
        int total = 0;
        for (int i = 0; i < charMap.length; i++) {
            for (int j = 0; j < charMap[i].length; j++) {
                if (charMap[i][j] == '@' && getAdjacentRollsCount(charMap, i, j) < 4) total++;
            }
        }
        return "Moveable rolls count: " + total;
    }

    public char[][] convertToCharArray(String input) {
        String[] lines = input.split("\n");
        char[][] array = new char[lines.length][lines[0].length()];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = lines[i].charAt(j);
            }
        }
        return array;
    }

    public int getAdjacentRollsCount(char[][] array, int x, int y) {
        int count = 0;
        int rows = array.length;
        int cols = array[0].length;
        // Adjacent points are:
        // (X-1,y-1)  (X,y-1)  (X+1, Y-1)
        // (X-1,y)     (X,y)   (X+1,y)
        // (X-1 Y+1)  (X, Y+1) (X+1 Y+1
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx, ny = y + dy;
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && array[nx][ny] == '@') {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String part2(String input) {
        char[][] grid = convertToCharArray(input);
        List<Point> toRemove;
        int total = 0;
        boolean hasRemoveableRolls = true;
        while(hasRemoveableRolls){
            toRemove = new ArrayList<>();
            int current =0;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == '@' && getAdjacentRollsCount(grid, i, j) < 4){
                        current++;
                        toRemove.add(new Point(i,j));
                    }
                }
            }
            removeRolls(toRemove, grid);
            total += current;
            hasRemoveableRolls = current > 0;
        }

        return "Moveable rolls count: " + total;
    }

    public void removeRolls(List<Point> detected, char[][] array){
        for(Point point: detected){
            array[point.x][point.y] = 'X';

        }
    }

}
