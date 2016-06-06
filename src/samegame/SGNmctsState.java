package samegame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import montecarlo.INmcsState;
import montecarlo.Pair;

public class SGNmctsState implements INmcsState<SGBoard, Point>{
	private final static Random rnd = new Random();
	private final SGBoard _sgBoard;
	private final ArrayList<Point> _allLegalActions;
	
	public SGNmctsState(int[][] board) {
		_sgBoard = new SGBoard(board);
		_allLegalActions = _sgBoard.findAllLegalMoves();
	}
	
	private SGNmctsState(SGBoard sgBoard) {
		_sgBoard = sgBoard;
		_allLegalActions = _sgBoard.findAllLegalMoves();
	}

	@Override
	public boolean isTerminalPosition() {
		return _allLegalActions.size() == 0;
	}

	@Override
	public ArrayList<Point> findAllLegalActions() {
		return _sgBoard.findAllLegalMoves();
	}

	@Override
	public INmcsState<SGBoard, Point> takeAction(final Point action) {
		final SGBoard newState = _sgBoard.copy();
		newState.removeGroup(action);
		return new SGNmctsState(newState);
	}

	@Override
	public Pair<Double, ArrayList<Point>> simulation() {
		final ArrayList<Point> simulation = new ArrayList<Point>();
		final SGBoard tempBoard = _sgBoard.copy();
		ArrayList<Point> groups = tempBoard.findAllLegalMoves();
		while (groups.size() > 0) {
			final Point randomMove = groups.get(rnd.nextInt(groups.size()));
			tempBoard.removeGroup(randomMove);
			simulation.add(randomMove);
			groups = tempBoard.findAllLegalMoves();
		}
		return Pair.of((double)tempBoard.getScore(), simulation);
	}
	
	@Override
	public String toString() {
		return _sgBoard.toString();
	}

	@Override
	public double getScore() {
		return _sgBoard.getScore();
	}
	
}