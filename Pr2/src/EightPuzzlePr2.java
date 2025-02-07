package aima.gui.demo.search;

import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.GraphSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.util.math.Biseccion;
import aima.core.environment.pr2.EightPuzzleGoalTest2;
import aima.core.environment.pr2.ManhattanHeuristicFunction2;
import aima.core.environment.pr2.MisplacedTilleHeuristicFunction2;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.environment.eightpuzzle.EightPuzzleFunctionFactory;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.gui.demo.search.GenerateInitialEightPuzzleBoard;



public class EightPuzzlePr2 {
	public static void main(String[] args) {
		EightPuzzleBoard board;
		EightPuzzleBoard goalboard;
		double B_IDS, B_BFS, B_MI, B_MA;
		int N_IDS, N_BFS, N_MI, N_MA;
		
		//Cabecera
		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println("||    ||      Nodos Generados                  ||                  b*                   ||");
		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println("|| d  ||    BFS  |    IDS  | A*h(1)  | A*h(2)  ||    BFS  |    IDS  | A*h(1)  | A*h(2)  ||");
		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println("------------------------------------------------------------------------------------------");
		
		//Bucle para generar experimentos para las profundidades deseadas
		for(int i = 2; i <= 24; i++) {
			//Reseteamos las variables
			N_MI = 0;
			N_MA = 0;
			N_IDS = 0;
			N_BFS = 0;

			
			//Se realizan 100 experimentos para cada profundidad
			for (int j = 0; j < 100; j++) {
				boolean profV = false;
				board = GenerateInitialEightPuzzleBoard.randomIni();
				goalboard = GenerateInitialEightPuzzleBoard.random(i, board);
				EightPuzzleGoalTest2 goal = new EightPuzzleGoalTest2();
				goal.DefEstadoF(goalboard);
				Problem prb = new Problem(board, EightPuzzleFunctionFactory.getActionsFunction(), EightPuzzleFunctionFactory.getResultFunction(), goal);
				while(!profV) {
					try {
						SearchAgent agent = new SearchAgent(prb, new AStarSearch(new GraphSearch(), new ManhattanHeuristicFunction2()));
						if (i == ((int)Float.parseFloat(agent.getInstrumentation().getProperty("pathCost")))) {
							profV = true;
						}
						else {
							goalboard = GenerateInitialEightPuzzleBoard.random(i, board);
							goal.DefEstadoF(goalboard);
							prb = new Problem(board, EightPuzzleFunctionFactory.getActionsFunction(), EightPuzzleFunctionFactory.getResultFunction(), goal);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				N_MI += eightPuzzleSearch(prb, new AStarSearch(new GraphSearch(), new MisplacedTilleHeuristicFunction2()));
				N_MA += eightPuzzleSearch(prb, new AStarSearch(new GraphSearch(), new ManhattanHeuristicFunction2()));			
				N_BFS += eightPuzzleSearch(prb, new BreadthFirstSearch(new GraphSearch()));
				if(i <= 10) {
					N_IDS += eightPuzzleSearch(prb, new IterativeDeepeningSearch());
				}
			}
			Biseccion bi = new Biseccion();
			bi.setDepth(i);
			
			bi.setGeneratedNodes(N_MI/100);
			B_MI = bi.metodoDeBiseccion(1.00001, 5, 1E-10);
			bi.setGeneratedNodes(N_MA/100);
			B_MA = bi.metodoDeBiseccion(1.00001, 5, 1E-10);
			bi.setGeneratedNodes(N_BFS/100);
			B_BFS = bi.metodoDeBiseccion(1.00001, 5, 1E-10);
			if(i <= 10) {
				bi.setGeneratedNodes(N_IDS/100);
				B_IDS = bi.metodoDeBiseccion(1.00001, 5, 1E-10);
				
				System.out.printf("|| %2d || %6d  | %6d  | %6d  | %6d  || %6.2f  | %6.2f  | %6.2f  | %6.2f  ||\n", i, N_BFS/100, N_IDS/100, N_MI/100, N_MA/100, B_BFS, B_IDS, B_MI, B_MA);
			}
			else {
				System.out.printf("|| %2d || %6d  |   ---   | %6d  | %6d  || %6.2f  |   ---   | %6.2f  | %6.2f  ||\n", i, N_BFS/100, N_MI/100, N_MA/100, B_BFS, B_MI, B_MA);
			}
			
		}
		System.out.println("------------------------------------------------------------------------------------------");
	}
	
	
	private static int eightPuzzleSearch(Problem problem, Search busqueda) {
		try {
			SearchAgent agent = new SearchAgent(problem, busqueda);
			int nodos = (int)Float.parseFloat(agent.getInstrumentation().getProperty("nodesGenerated"));
			return nodos;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}




