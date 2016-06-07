package samegame.tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.Test;

import montecarlo.INmcsState;
import montecarlo.NestedMonteCarloSearch;
import montecarlo.Pair;
import samegame.BoardGenerator;
import samegame.SGBoard;
import samegame.SGNmctsState;
import samegame.SGRandomSearch;

public class IntegrationTests {
	
	@Test
	public void nmctsPropertyTest_FoundSolutionHasCorrectScore() {
		Random rnd = new Random();
		
		Stream.of(
				BoardGenerator.generateRandomBoard(rnd.nextInt(3) + 4, rnd.nextInt(3) + 4, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(3) + 4, rnd.nextInt(3) + 4, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(3) + 4, rnd.nextInt(3) + 4, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(3) + 4, rnd.nextInt(3) + 4, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(3) + 4, rnd.nextInt(3) + 4, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(3) + 4, rnd.nextInt(3) + 4, rnd.nextInt(4) + 1))
			.forEach(board -> {
				INmcsState<SGBoard, Point> state = new SGNmctsState(board);
				Pair<Double, List<Point>> result = NestedMonteCarloSearch.executeSearch(state, rnd.nextInt(3) + 1, () -> false);

				SGBoard sgBoard = new SGBoard(board);
				for (Point move : result.item2) {
					sgBoard.removeGroup(move);
				}
				
				assertNotEquals(0.0, result.item1, 0.01);
				assertEquals((double) sgBoard.getScore(), result.item1, 0.01);
			});
	}
	
	//@Test
	public void compare_strategies() {
		final long runningTimeMs = 30 * 1000;
		int[][] board = BoardGenerator.generateRandomBoard(20, 20, 4);

		System.out.printf("Time: %s ms\n", runningTimeMs);
		
		INmcsState<SGBoard, Point> state = new SGNmctsState(board);

		final long endTimeMs = System.currentTimeMillis() + runningTimeMs;
		Pair<Double, List<Point>> resultNmcs = NestedMonteCarloSearch.executeSearch(state, 2, () -> {
			return System.currentTimeMillis() > endTimeMs;
		});
		
		System.out.printf("NMCS: %s\n", resultNmcs.item1);
		
		Pair<Double, List<Point>> resultRnd = SGRandomSearch.executeSearch(board, runningTimeMs);
		System.out.println("RANDOM: " + resultRnd.item1);
	}	
}
