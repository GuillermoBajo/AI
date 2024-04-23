package aima.core.environment.Canibales;

import aima.core.search.framework.GoalTest;

/**
 * @author Guillermo Bajo
 * 
 */
//Funcion que simplemente te dice si estas en el estado objetivo
public class CanibalesGoalTest implements GoalTest {
	CanibalesBoard goal = new CanibalesBoard(new int[] {0, 0, 1, 3, 3});

	public boolean isGoalState(Object state) {
		CanibalesBoard board = (CanibalesBoard) state;
		return board.equals(goal);
	}
}