package samegame.tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;
import java.util.Optional;
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
	
	//@Test
	public void nmcsRunner() {
		final Optional<int[][]> board = BoardGenerator.generateBoard("1,2,4,4,5,4,5,4,3,5,5,1,4,3,2;5,1,2,2,1,1,1,2,3,1,4,1,1,3,2;2,3,5,4,1,3,1,3,4,5,4,2,3,3,4;4,5,4,1,2,4,4,3,4,2,2,1,4,5,3;3,1,1,4,3,1,3,4,4,4,1,2,2,2,2;3,5,3,3,2,5,4,3,2,5,1,2,5,5,2;1,1,1,3,3,4,5,4,3,4,1,4,5,4,5;2,3,1,5,2,3,3,5,1,3,5,3,5,1,4;4,5,4,4,2,2,1,5,5,3,2,1,1,2,4;2,3,3,3,5,4,3,1,3,2,1,2,1,2,4;3,4,5,3,2,1,2,3,4,3,5,1,3,5,4;2,4,3,5,4,1,5,5,2,2,5,2,3,5,1;4,1,3,3,2,5,4,5,2,3,3,2,2,4,2;3,1,3,2,1,5,2,5,1,4,3,4,1,3,5;1,4,2,2,1,2,5,2,5,2,2,2,1,5,3;");
		final int level = 4;
		final long runningTimeMs = 5 * 60 * 1000;
		
		if (!board.isPresent())
			fail();

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
		final long runningTimeMs = 30000;
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
