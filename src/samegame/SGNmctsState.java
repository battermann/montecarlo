package samegame;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import montecarlo.INmcsState;
import montecarlo.Pair;

public class SGNmctsState implements INmcsState<SGBoard, Point>{
	private final static Random rnd = new Random();
	private final SGBoard _sgBoard;
	private final List<Point> _allLegalActions;
	private final Pair<Double, List<Point>> _initialSolution;
	
	public SGNmctsState(final int[][] board) {
		this(new SGBoard(board));
	}
	
	public SGNmctsState(final int[][] board, final Pair<Double, List<Point>> initialSolution) {
		this(new SGBoard(board), initialSolution);
	}
	
	private SGNmctsState(final SGBoard sgBoard) {
		this(sgBoard, Pair.of((double) sgBoard.getScore(), new LinkedList<Point>()));
	}
	
	private SGNmctsState(final SGBoard sgBoard, final Pair<Double, List<Point>> initialSolution) {
		_sgBoard = sgBoard;
		_allLegalActions = _sgBoard.findAllLegalMoves();
		_initialSolution = initialSolution;
	}

	@Override
	public boolean isTerminalPosition() {
		return _allLegalActions.size() == 0;
	}

	@Override
	public List<Point> findAllLegalActions() {
		return _sgBoard.findAllLegalMoves();
	}

	@Override
	public INmcsState<SGBoard, Point> takeAction(final Point action) {
		final SGBoard newState = _sgBoard.copy();
		newState.removeGroup(action);
		return new SGNmctsState(newState);
	}

	@Override
	public Pair<Double, List<Point>> simulation() {
		final List<Point> simulation = new LinkedList<Point>();
		final SGBoard tempBoard = _sgBoard.copy();
		List<Point> groups = tempBoard.findAllLegalMoves();
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

	@Override
	public Pair<Double, List<Point>> getInitionSolution() {
		return _initialSolution;
	}
	
}