package samegame.tests;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import montecarlo.INmcsState;
import montecarlo.NestedMonteCarloSearch;
import montecarlo.Pair;
import samegame.BoardGenerator;
import samegame.SGBoard;
import samegame.SGNmctsState;

public class NmcsRunner {

	public static void main(String[] args) {
		runGame10Search();
	}
	
	public static void runGame10Search() {
		final Optional<int[][]> board = BoardGenerator.generateBoard("1,2,4,4,5,4,5,4,3,5,5,1,4,3,2;5,1,2,2,1,1,1,2,3,1,4,1,1,3,2;2,3,5,4,1,3,1,3,4,5,4,2,3,3,4;4,5,4,1,2,4,4,3,4,2,2,1,4,5,3;3,1,1,4,3,1,3,4,4,4,1,2,2,2,2;3,5,3,3,2,5,4,3,2,5,1,2,5,5,2;1,1,1,3,3,4,5,4,3,4,1,4,5,4,5;2,3,1,5,2,3,3,5,1,3,5,3,5,1,4;4,5,4,4,2,2,1,5,5,3,2,1,1,2,4;2,3,3,3,5,4,3,1,3,2,1,2,1,2,4;3,4,5,3,2,1,2,3,4,3,5,1,3,5,4;2,4,3,5,4,1,5,5,2,2,5,2,3,5,1;4,1,3,3,2,5,4,5,2,3,3,2,2,4,2;3,1,3,2,1,5,2,5,1,4,3,4,1,3,5;1,4,2,2,1,2,5,2,5,2,2,2,1,5,3;");
		final int level = 2;
		final long runningTimeMs = 2 * 60 * 1000;
		
		if (!board.isPresent())
			System.out.println("error while generating the board.");

		board.ifPresent(x -> {
			System.out.printf("Time: %s ms\n", runningTimeMs);
			System.out.printf("Level: %s\n", level);
			
			final INmcsState<SGBoard, Point> state = new SGNmctsState(x, initialSolutionGame10());

			final long endTimeMs = System.currentTimeMillis() + runningTimeMs;
			final Pair<Double, List<Point>> result = NestedMonteCarloSearch.executeSearch(state, level, () -> {
				return System.currentTimeMillis() > endTimeMs;
			});
			
			System.out.println("NMCS: " + result.item1);
			System.out.println(result.item2);
		});
	}
	
	private static Pair<Double, List<Point>> initialSolutionGame10() {
		final double score = 2381.0;
		final List<Point> path = new LinkedList<Point>(Arrays.asList(
				new Point(1, 1)
				, new Point(8, 2)
				, new Point(13, 5)
				, new Point(7, 5)
				, new Point(11, 9)
				, new Point(13, 3)
				, new Point(6, 3)
				, new Point(8, 7)
				, new Point(10, 3)
				, new Point(1, 4)
				, new Point(12, 3)
				, new Point(6, 10)
				, new Point(1, 7)
				, new Point(5, 1)
				, new Point(4, 6)
				, new Point(11, 4)
				, new Point(10, 9)
				, new Point(3, 8)
				, new Point(7, 2)
				, new Point(12, 3)
				, new Point(3, 9)
				, new Point(4, 0)
				, new Point(3, 3)
				, new Point(11, 5)
				, new Point(14, 12)
				, new Point(1, 10)
				, new Point(2, 8)
				, new Point(12, 3)
				, new Point(3, 6)
				, new Point(0, 9)
				, new Point(2, 0)
				, new Point(9, 4)
				, new Point(3, 4)
				, new Point(13, 2)
				, new Point(6, 2)
				, new Point(9, 10)
				, new Point(10, 8)
				, new Point(8, 6)
				, new Point(1, 6)
				, new Point(14, 1)
				, new Point(2, 4)
				, new Point(4, 1)
				, new Point(5, 5)
				, new Point(0, 4)
				, new Point(0, 3)
				, new Point(3, 0)
				, new Point(9, 1)
				, new Point(7, 5)
				, new Point(7, 0)
				, new Point(4, 4)
				, new Point(0, 5)
				, new Point(3, 0)
				, new Point(2, 0)
				, new Point(4, 1)
				, new Point(0, 2)
				, new Point(0, 0)
				, new Point(6, 0)
				, new Point(0, 0)
				, new Point(0, 1)
				, new Point(0, 0)
				, new Point(0, 1)
				, new Point(1, 2)
				, new Point(2, 0)
				, new Point(0, 0)
				));
		return Pair.of(score, path);
	}
}
