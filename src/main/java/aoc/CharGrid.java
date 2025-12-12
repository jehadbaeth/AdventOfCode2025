package aoc;

import java.util.List;
import java.util.stream.Stream;

/**
 * A bounded grid of char values, indexed by Location.
 * CAUTION: In (x, y) coordinates, x is the column and y the row, i.e. use new Location(y, x)
 */
record Location(int row, int col) {
}

public class CharGrid {
    private char[][] grid;

    private CharGrid() {
    }

    /**
     * Constructs a grid with given dimensions and initial char value.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     * @param c    the initial value for all elements
     */
    public CharGrid(int rows, int cols, char c) {
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = c;
    }

    /**
     * Parses a CharGrid in the standard AoC format.
     *
     * @param lines the lines containing the grid
     * @return the grid
     */
    public static CharGrid parse(List<String> lines) {
        var result = new CharGrid();
        result.grid = lines.stream().map(String::toCharArray).toArray(char[][]::new);
        return result;
    }

    public int rows() {
        return grid.length;
    }

    public int cols() {
        return grid[0].length;
    }

    /**
     * Checks if the given location is a valid key in this grid.
     *
     * @param p a (row, col) location
     * @return true if the location is valid
     */
    public boolean isValid(Location p) {
        return p.row() >= 0 && p.row() < grid.length && p.col() >= 0 && p.col() < grid[0].length;
    }

    public Stream<Location> locations() {
        return Stream.iterate(
                new Location(0, 0),
                this::isValid,
                l -> l.col() < cols() - 1 ? new Location(l.row(), l.col() + 1) : new Location(l.row() + 1, 0));
    }

    /**
     * Gets the element at a location.
     *
     * @param p the (row, col) location
     * @return the element or null if the location is not valid
     */
    public Character get(Location p) {
        if (isValid(p))
            return grid[p.row()][p.col()];
        else
            return null;
    }

    /**
     * Gets the location of the first occurrence of a given character, in row-major order.
     *
     * @param c the character to find
     * @return the location of the first occurrence of c, or null if c does not occur
     */
    public Location findFirst(char c) {
        return locations().filter(p -> grid[p.row()][p.col()] == c).findFirst().orElse(null);
    }

    /**
     * Gets the locations of all occurrences of a given character, in row-major order.
     *
     * @param c the character to find
     * @return the locations at which c occurs
     */
    public Stream<Location> findAll(char c) {
        return locations().filter(p -> grid[p.row()][p.col()] == c);
    }

    /**
     * Yields the standard AoC string representation.
     */
    public String toString() {
        var result = new StringBuilder();
        for (int i = 0; i < rows(); i++) {
            result.append(grid[i]);
            result.append('\n');
        }
        return result.toString();
    }
}