package samegame.tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
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
	@Test
	public void NmctsIntegrationTest_smallBoard() {
		Optional<int[][]> board = BoardGenerator.generateBoard("1,1;2,2;");

		if (!board.isPresent())
			fail();

		board.ifPresent(x -> {
			INmcsState<SGBoard, Point> state = new SGNmctsState(x);
			Pair<Double, ArrayList<Point>> result = NestedMonteCarloSearch.executeSearch(state, 2);
			System.out.println("NMCS: " + result.item1);
			System.out.println(result.item2);
		});
	}

	@Test
	public void NmctsPropertyTest_FoundSolutionHasCorrectScore() {
		Random rnd = new Random();
		
		Stream.of(
				BoardGenerator.generateRandomBoard(rnd.nextInt(5) + 1, rnd.nextInt(5) + 1, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(5) + 1, rnd.nextInt(5) + 1, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(5) + 1, rnd.nextInt(5) + 1, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(5) + 1, rnd.nextInt(5) + 1, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(5) + 1, rnd.nextInt(5) + 1, rnd.nextInt(4) + 1),
				BoardGenerator.generateRandomBoard(rnd.nextInt(5) + 1, rnd.nextInt(5) + 1, rnd.nextInt(4) + 1))
			.forEach(board -> {
				INmcsState<SGBoard, Point> state = new SGNmctsState(board);
				Pair<Double, ArrayList<Point>> result = NestedMonteCarloSearch.executeSearch(state, rnd.nextInt(4) + 1);

				SGBoard sgBoard = new SGBoard(board);
				for (Point move : result.item2) {
					sgBoard.removeGroup(move);
				}
				
				assertEquals((double) sgBoard.getScore(), result.item1, 0.01);
			});
	}
	
	@Test
	public void Test() {
		int[][] board = BoardGenerator.generateRandomBoard(8, 8, 4);
		INmcsState<SGBoard, Point> state = new SGNmctsState(board);

		long start = System.currentTimeMillis();
		Pair<Double, ArrayList<Point>> resultNmcs = NestedMonteCarloSearch.executeSearch(state, 3);
		long runningTimeInMs = System.currentTimeMillis() - start;
		System.out.printf("NMCS: %s (time: %s ms)\n", resultNmcs.item1, runningTimeInMs);
		
		Pair<Double, ArrayList<Point>> resultRnd = SGRandomSearch.executeSearch(board, runningTimeInMs);
		System.out.println("RANDOM: " + resultRnd.item1);
	}
	
}