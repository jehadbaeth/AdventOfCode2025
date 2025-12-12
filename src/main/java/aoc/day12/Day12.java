package aoc.day12;

import aoc.CharGrid;
import aoc.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static aoc.Utils.Util.parseIntegers;

class Region {
    public CharGrid grid;
    public List<Integer> presents;
    public Region(CharGrid grid, List<Integer> presents) {
        this.grid = grid;
        this.presents = presents;
    }
    static Region parse(String s) {
        var numbers = parseIntegers(s, "[^\\pN]+");
        return new Region(new CharGrid(numbers.get(0), numbers.get(1), ' '), numbers.subList(2, numbers.size()));
    }

    boolean definitelyFits() {
        return 9 * presents.stream().mapToInt(i -> i).sum() <= grid.rows() * grid.cols();
    }

//    boolean cantFit() {
//        long count = 0;
//        for (int i = 0; i < presents.size(); i++) count += Day12.sizes.get(i) * presents.get(i);
//        return count > (long) grid.rows() * grid.cols();
//    }
}


public class Day12 implements Day {
    static List<Long> sizes;
    List<CharGrid> shapes;
    List<Region> regions;

    void parse(String input) {
        shapes = new ArrayList<>();
        var inputs = input.split("\n\n");
        for (int i = 0; i < inputs.length - 1; i++) {
            shapes.add(CharGrid.parse(Stream.of(inputs[i].split("\n")).skip(1).toList()));
        }
        sizes = shapes.stream().map(s -> s.findAll('#').count()).toList();
        regions = Stream.of(inputs[inputs.length - 1].split("\n")).map(Region::parse).toList();
    }

    @Override
    public String part1(String input) {
        parse(input);
        return String.valueOf(regions.stream().filter(Region::definitelyFits).count());
    }

    @Override
    public String part2(String input) {
        return  "wait, what just happened?";
    }

}
