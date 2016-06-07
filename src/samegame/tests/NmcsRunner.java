package samegame.tests;

import java.awt.Point;
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
		final int level = 3;
		final long runningTimeMs = 20 * 60 * 1000;
		
		if (!board.isPresent())
			System.out.println("error while generating the board.");

		board.ifPresent(x -> {
			System.out.printf("Time: %s ms\n", runningTimeMs);
			System.out.printf("Level: %s\n", level);
			
			final INmcsState<SGBoard, Point> state = new SGNmctsState(x);

			final long endTimeMs = System.currentTimeMillis() + runningTimeMs;
			final Pair<Double, List<Point>> result = NestedMonteCarloSearch.executeSearch(state, level, () -> {
				return System.currentTimeMillis() > endTimeMs;
			});
			
			System.out.println("NMCS: " + result.item1);
			System.out.println(result.item2);
		});
	}
}
