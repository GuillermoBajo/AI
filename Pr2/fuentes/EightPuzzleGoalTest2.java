package aima.core.environment.pr2;

import aima.core.search.framework.GoalTest;
import aima.core.environment.eightpuzzle.*;

/**
 * @author Ravi Mohan
 * 
 */
//Funcion que simplemente te dice si estas en el estado objetivo
public class EightPuzzleGoalTest2 implements GoalTest {
	static EightPuzzleBoard goal = new EightPuzzleBoard(new int[] { 0, 1, 2, 3, 4, 5,
			6, 7, 8 });

	public boolean isGoalState(Object state) {
		EightPuzzleBoard board = (EightPuzzleBoard) state;
		return board.equals(goal);
	}
	
	public void DefEstadoF(EightPuzzleBoard a) {
		goal = a;
	}
}