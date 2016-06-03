package samegame.tests;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Optional;

import org.junit.Test;

import samegame.BoardGenerator;
import samegame.SGBoard;

public class SGBoardTests {

	@Test
	public void removeGroup_noBonus_scoreShouldBeCorrect() {
		Optional<int[][]> board = BoardGenerator.generateBoard("1,2,1,2,1;1,2,1,1,2;1,2,1,1,2;2,2,2,2,2;1,2,2,2,1;");

		if (!board.isPresent())
			fail();

		board.ifPresent(x -> {
			SGBoard sgBoard = new SGBoard(x);
			sgBoard.removeGroup(new Point(0, 2));
			sgBoard.removeGroup(new Point(0, 1));
			sgBoard.removeGroup(new Point(0, 0));

			assertEquals(158, sgBoard.getScore());
		});
	}

	@Test
	public void removeGroup_withBonus_scoreShouldBeCorrect() {
		Optional<int[][]> board = BoardGenerator.generateBoard("1,2,1,2,1;1,2,1,1,2;1,2,1,1,2;2,2,2,2,2;1,2,2,2,1;");

		if (!board.isPresent())
			fail();

		board.ifPresent(x -> {
			SGBoard sgBoard = new SGBoard(x);
			sgBoard.removeGroup(new Point(2, 2));
			sgBoard.removeGroup(new Point(0, 1));
			sgBoard.removeGroup(new Point(0, 0));

			assertEquals(1169, sgBoard.getScore());
		});
	}
}
