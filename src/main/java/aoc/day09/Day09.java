package aoc.day09;

import aoc.Day;

import java.util.*;

class Point {
    long x;
    long y;

    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public long areaWith(Point point) {
        return (Math.abs(x - point.x) + 1L) * (Math.abs(y - point.y) + 1L);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}


public class Day09 implements Day {

    @Override
    public String part1(String input) {
        String[] lines = input.split("\n");
        List<Point> points = Arrays.stream(lines).map(s -> {
            String[] temp = s.split(",");
            return new Point(Long.parseLong(temp[0]), Long.parseLong(temp[1]));
        }).toList();
        long max = 0;
        for(int i =0; i < points.size(); i++){
            for(int j =0; j< points.size(); j++){
                if(i == j) continue;
                max = Math.max(max, points.get(i).areaWith(points.get(j)));
            }
        }
        return String.valueOf(max);


    }

    // Helper method to check if a point 'p' lies on the segment defined by 'p1' and 'p2'.
    // Segments are guaranteed to be horizontal or vertical.
    private boolean isOnSegment(Point p, Point p1, Point p2) {
        // Vertical segment
        if (p1.x == p2.x) {
            return p.x == p1.x &&
                    p.y >= Math.min(p1.y, p2.y) && p.y <= Math.max(p1.y, p2.y);
        }
        // Horizontal segment
        if (p1.y == p2.y) {
            return p.y == p1.y &&
                    p.x >= Math.min(p1.x, p2.x) && p.x <= Math.max(p1.x, p2.x);
        }
        return false; // Should not happen based on problem constraints
    }

    // Helper method to check if a point 'p' is on the entire boundary loop (Red or Green boundary tiles).
    private boolean isOnBoundary(Point p, List<Point> redTiles) {
        int N = redTiles.size();
        for (int i = 0; i < N; i++) {
            Point p1 = redTiles.get(i);
            Point p2 = redTiles.get((i + 1) % N);
            if (isOnSegment(p, p1, p2)) {
                return true;
            }
        }
        return false;
    }

    // Point-in-Polygon Check (Ray Casting Algorithm)
    // Checks if a point 'p' is strictly INSIDE the polygon defined by 'redTiles'.
    private boolean isInside(Point p, List<Point> redTiles) {
        int N = redTiles.size();
        int crossings = 0;

        for (int i = 0; i < N; i++) {
            Point p1 = redTiles.get(i);
            Point p2 = redTiles.get((i + 1) % N);

            // Ignore boundary tiles in the ray casting algorithm
            if (isOnSegment(p, p1, p2)) {
                continue;
            }

            // Normalize points so a.y < b.y
            Point a = (p1.y < p2.y) ? p1 : p2;
            Point b = (p1.y < p2.y) ? p2 : p1;

            // Check if the ray (horizontal line from p to the right) crosses the segment (a, b)
            // The ray starts at p.y and moves right. We exclude the upper endpoint (b.y)
            // to handle shared vertices between segments.
            if (p.y >= a.y && p.y < b.y) {

                // Calculate the x-coordinate where the ray intersects the segment.
                // Uses long arithmetic to maintain precision for large coordinates.
                // x_intersect = a.x + (b.x - a.x) * (p.y - a.y) / (b.y - a.y)
                // Need to use BigInteger or carefully handle division/multiplication for intermediate steps
                // if coordinates were truly enormous, but long should suffice for typical AOC inputs.
                long numerator = (b.x - a.x) * (p.y - a.y);
                long denominator = (b.y - a.y);
                long x_intersect;

                // Vertical segment (denominator is 0) should be covered by a.y != b.y below.
                if (denominator != 0) {
                    x_intersect = a.x + numerator / denominator;
                } else {
                    // This block should ideally not be reached due to a.y < b.y check,
                    // but as a fallback, assume no crossing for horizontal segment at same y.
                    continue;
                }

                if (p.x < x_intersect) { // The point is to the left of the intersection
                    crossings++;
                }
            }
        }

        // Odd number of crossings means the point is inside
        return (crossings % 2) == 1;
    }

    // Combined Check: Is the point Red or Green (on boundary or inside)?
    private boolean isRedOrGreen(Point p, List<Point> redTiles) {
        if (isOnBoundary(p, redTiles)) {
            return true;
        }
        return isInside(p, redTiles);
    }
    // We need to add this new validation method to Day09.java
    private boolean isRectangleValid(Point p1, Point p2, List<Point> redTiles) {
        long minX = Math.min(p1.x, p2.x);
        long maxX = Math.max(p1.x, p2.x);
        long minY = Math.min(p1.y, p2.y);
        long maxY = Math.max(p1.y, p2.y);

        // 1. Check Top and Bottom Horizontal Edges
        for (long x = minX; x <= maxX; x++) {
            // Check top edge at maxY
            if (!isRedOrGreen(new Point(x, maxY), redTiles)) {
                return false;
            }
            // Check bottom edge at minY
            if (!isRedOrGreen(new Point(x, minY), redTiles)) {
                return false;
            }
        }

        // 2. Check Left and Right Vertical Edges
        // Start from minY + 1 and end at maxY - 1 to avoid double-checking the four corners
        // which are already covered by the horizontal loops above.
        for (long y = minY + 1; y < maxY; y++) {
            // Check left edge at minX
            if (!isRedOrGreen(new Point(minX, y), redTiles)) {
                return false;
            }
            // Check right edge at maxX
            if (!isRedOrGreen(new Point(maxX, y), redTiles)) {
                return false;
            }
        }

        // If every tile on the boundary is red or green, the rectangle is valid.
        return true;
    }

    // ---------------------------------------------
    @Override
    public String part2(String input) {
        String[] lines = input.trim().split("\n");
        List<Point> loop = Arrays.stream(lines)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    String[] temp = s.split(",");
                    return new Point(Long.parseLong(temp[0]), Long.parseLong(temp[1]));
                })
                .toList();

        long maxArea = 0;
        int N = loop.size();

        // The two necessary helper methods (isOnSegment, isInside, isRedOrGreen)
        // are assumed to be correctly defined in Day09 as provided in the previous response.
        // The new check is performed by isRectangleValid.

        // Iterate over all pairs of red tiles P_i and P_j
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                Point p1 = loop.get(i);
                Point p2 = loop.get(j);
                System.out.print(i+ "_");
                System.out.print(p1);
                System.out.print(" , ");
                System.out.println(p2);


                // Skip rectangles with zero width or height
                if (p1.x == p2.x || p1.y == p2.y) continue;

                // Calculate the area: (Width+1)*(Height+1)
                long currentArea = p1.areaWith(p2);

                // **Stronger Validation Check**
                if (isRectangleValid(p1, p2, loop)) {
                    maxArea = Math.max(maxArea, currentArea);
                }
            }
        }

        return String.valueOf(maxArea);
    }


}
