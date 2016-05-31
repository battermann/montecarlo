package samegame;

import java.awt.Point;
import java.util.*;

public class SGBoard {
	private int getBonus() {
		return 1000;
	}

	private final int[][] _board;

	public int[][] getBoardCopy() {
		return copyBoard(_board);
	}

	private int _score;

	public int getScore() {
		return _score;
	}

	private int _numberOfStones;

	public int getNumberOfStones() {
		return _numberOfStones;
	}

	/**
	 * Copy constructor
	 * 
	 * @param sgBoard
	 */
	public SGBoard(final SGBoard sgBoard) {
		this(sgBoard._board, sgBoard.getScore(), sgBoard.getNumberOfStones());
	}

	public static SGBoard copy(final SGBoard board) {
		return new SGBoard(board);
	}
	
	public SGBoard copy() {
		return new SGBoard(this);
	}

	public static SGBoard empty() {
		return new SGBoard(new int[0][0], 0, 0);
	}

	public SGBoard(final int[][] board) {
		_board = copyBoard(board);
		_numberOfStones = countStones();
	}

	private SGBoard(final int[][] board, final int score, final int numberOfStones) {
		_board = copyBoard(board);
		_score = score;
		_numberOfStones = numberOfStones;
	}

	private static int[][] copyBoard(int[][] board) {
		final int[][] copy = new int[board.length][board[0].length];
		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy[0].length; j++) {
				copy[i][j] = board[i][j];
			}
		}
		return copy;
	}

	public void removeGroup(final Point selectedPoint) {
		if (_numberOfStones <= 1)
			return;

		final Set<Point> group = findGroupWhichContainsStone(selectedPoint);
		if (!tryRemoveGroup(group))
			return;

		letStonesFallDown();
		final int size = group.size();
		_numberOfStones -= size;
		_score += specifyScore(size);
	}

	private void letStonesFallDown() {
		final ArrayList<Integer> emptyColumns = new ArrayList<Integer>();
		moveAllZerosUp(emptyColumns);
		if (emptyColumnExists(emptyColumns))
			moveEmptyColumnlZerosRight(emptyColumns);
	}

	private void moveAllZerosUp(final ArrayList<Integer> emptyColumns) {
		for (int column = 0; column < _board[0].length; column++) {
			moveAllZerosInRowUp(column);
			if (isColumnEmpty(column))
				emptyColumns.add(0, column);
		}
	}

	private boolean emptyColumnExists(ArrayList<Integer> emptyColumns) {
		return emptyColumns.size() > 0 && emptyColumns.size() < _board[0].length;
	}

	private void moveEmptyColumnlZerosRight(final ArrayList<Integer> emptyColumns) {
		int movedEmptyColumns = 0;
		for (Integer emptyColumn : emptyColumns) {
			moveZerosInColumnToRight(movedEmptyColumns, emptyColumn);
			movedEmptyColumns++;
		}
	}

	private void moveZerosInColumnToRight(int movedEmptyColumns, Integer emptyColumn) {
		for (int currentColumn = emptyColumn; currentColumn < _board[0].length - movedEmptyColumns - 1; currentColumn++)
			moveZeroToRight(currentColumn);
	}

	private void moveZeroToRight(final int currentColumn) {
		for (int row = 0; row < _board.length; row++) {
			_board[row][currentColumn] = _board[row][currentColumn + 1];
			_board[row][currentColumn + 1] = 0;
		}
	}

	private boolean isColumnEmpty(final int column) {
		return _board[_board.length - 1][column] == 0;
	}

	private void moveAllZerosInRowUp(final int column) {
		int movedZeros = 0;
		for (int row = 1; row < _board.length; row++)
			if (_board[row][column] == 0) {
				moveZeroUp(column, row, movedZeros);
				movedZeros++;
			}
	}

	private void moveZeroUp(final int column, final int row, final int movedZeros) {
		for (int i = row; i > movedZeros; i--) {
			_board[i][column] = _board[i - 1][column];
			_board[i - 1][column] = 0;
		}
	}

	public Set<Point> findGroupWhichContainsStone(final Point stone) {
		final Set<Point> group = new HashSet<Point>();
		if (!isOnBoard(stone))
			return group;
		if (colorOfStoneEquals(stone, 0))
			return group;
		else {
			group.add(stone);
			return searchForStonesRecursive(stone, getColorOfStone(stone), group);
		}
	}

	private Set<Point> searchForStonesRecursive(final Point stone, final int color, final Set<Point> stones) {
		final Point pointUp = new Point(stone.x, stone.y + 1);
		if (stoneBelongsToGroup(color, stones, pointUp)) {
			stones.add(pointUp);
			searchForStonesRecursive(pointUp, color, stones);
		}
		final Point pointDown = new Point(stone.x, stone.y - 1);
		if (stoneBelongsToGroup(color, stones, pointDown)) {
			stones.add(pointDown);
			searchForStonesRecursive(pointDown, color, stones);
		}
		final Point pointLeft = new Point(stone.x - 1, stone.y);
		if (stoneBelongsToGroup(color, stones, pointLeft)) {
			stones.add(pointLeft);
			searchForStonesRecursive(pointLeft, color, stones);
		}
		final Point pointRight = new Point(stone.x + 1, stone.y);
		if (stoneBelongsToGroup(color, stones, pointRight)) {
			stones.add(pointRight);
			searchForStonesRecursive(pointRight, color, stones);
		}
		return stones;
	}

	private boolean stoneBelongsToGroup(final int color, final Set<Point> stones, final Point stone) {
		return isOnBoard(stone) && !stones.contains(stone) && colorOfStoneEquals(stone, color);
	}

	private boolean colorOfStoneEquals(final Point stone, final int color) {
		return _board[rowOfStone(stone)][columnOfStone(stone)] == color;
	}

	private boolean tryRemoveGroup(final Set<Point> group) {
		if (group.size() < 2)
			return false;

		setColorsToZero(group);
		return true;
	}

	private void setColorsToZero(final Set<Point> points) {
		for (Point point : points)
			_board[rowOfStone(point)][columnOfStone(point)] = 0;
	}

	private int specifyScore(final int numberOfRemovedStones) {
		if (_numberOfStones == 0)
			return (numberOfRemovedStones - 2) * (numberOfRemovedStones - 2) + getBonus();

		return (numberOfRemovedStones - 2) * (numberOfRemovedStones - 2);
	}

	private int rowOfStone(final Point p) {
		return _board.length - 1 - p.y;
	}

	private int columnOfStone(final Point p) {
		return p.x;
	}

	public boolean isOnBoard(final Point point) {
		if (point.x < 0 || point.x >= _board[0].length)
			return false;
		return point.y >= 0 && point.y < _board.length;
	}

	private int countStones() {
		int stones = 0;
		for (int i = 0; i < _board.length; i++)
			for (int j = 0; j < _board[i].length; j++)
				if (_board[i][j] != 0)
					stones++;
		return stones;
	}

	public int getColorOfStone(final Point p) {
		return _board[rowOfStone(p)][columnOfStone(p)];
	}

	private void addToPointsToSkip(final ArrayList<Point> pointsToSkip, final Set<Point> group) {
		for (Point point : group)
			pointsToSkip.add(point);
	}

	public ArrayList<Point> findAllLegalMoves() {
		final ArrayList<Point> moves = new ArrayList<Point>();
		final ArrayList<Point> pointsToSkip = new ArrayList<Point>();
		for (int i = 0; i < _board[0].length; i++) {
			for (int j = 0; j < _board.length; j++) {
				final Point move = new Point(i, j);
				if (isNewAndNotEmpty(pointsToSkip, move)) {
					final Set<Point> group = findGroupWhichContainsStone(move);
					if (group.size() >= 2) {
						addToPointsToSkip(pointsToSkip, group);
						moves.add(move);
					}
				}
			}
		}
		return moves;
	}

	private boolean isNewAndNotEmpty(final ArrayList<Point> pointsToSkip, final Point move) {
		return getColorOfStone(move) != 0 && !pointsToSkip.contains(move);
	}
	
	@Override
	public String toString() {
		return String.format("%s\nScore: %s", Arrays.deepToString(_board), _score);
	}
	
}
