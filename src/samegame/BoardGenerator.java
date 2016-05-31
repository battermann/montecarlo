package samegame;

import java.util.Random;
import java.util.Optional;

public class BoardGenerator {

	public static Optional<int[][]> generateBoard(final String boardAsString) {
		try {
			final String[] rows = boardAsString.split(";");
			final int[][] board = new int[rows.length][(rows[0].split(",")).length];
			for (int i = 0; i < rows.length; i++) {
				final String[] column = rows[i].split(",");
				for (int j = 0; j < column.length; j++) {
					final int color = Integer.parseInt(column[j]);
					board[i][j] = color;
				}
			}
			return Optional.of(board);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static int[][] generateRandomBoard(final int height, final int length, final int numberOfColors) {
		final int[][] board = new int[height][length];
		final Random rnd = new Random();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = rnd.nextInt(numberOfColors) + 1;
			}
		}
		return board;
	}
}
