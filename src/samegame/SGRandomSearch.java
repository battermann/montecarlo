package samegame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import montecarlo.Pair;

public class SGRandomSearch {
	
	public static Pair<Double, ArrayList<Point>> executeSearch(final int[][] board, final long runningTimeInMs) {
		final long start = System.currentTimeMillis();
		final long end = start + runningTimeInMs;
		
		final Random rnd = new Random();
		Pair<Double, ArrayList<Point>> bestResult = simulation(rnd, board);
		
		while(System.currentTimeMillis() < end) {
			final Pair<Double, ArrayList<Point>> currentResult = simulation(rnd, board);
			if(currentResult.item1 > bestResult.item1)
				bestResult = currentResult;
		}
		
		return bestResult;
	}
	
	public static Pair<Double, ArrayList<Point>> simulation(final Random rnd, final int[][] board) {
		final ArrayList<Point> simulation = new ArrayList<Point>();
		final SGBoard tempBoard = new SGBoard(board);
		ArrayList<Point> groups = tempBoard.findAllLegalMoves();
		while (groups.size() > 0) {
			final Point randomMove = groups.get(rnd.nextInt(groups.size()));
			tempBoard.removeGroup(randomMove);
			simulation.add(randomMove);
			groups = tempBoard.findAllLegalMoves();
		}
		return Pair.of((double)tempBoard.getScore(), simulation);
	}
}
