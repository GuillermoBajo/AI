package aima.gui.search;

import java.util.List;

import aima.core.agent.Action;
import aima.core.environment.Canibales.CanibalesBoard;
import aima.core.environment.Canibales.CanibalesFunctionFactory;
import aima.core.environment.Canibales.CanibalesGoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.ResultFunction;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.search.uninformed.BreadthFirstSearch;


public class CanibalesPr1 {
	static CanibalesBoard initial = new CanibalesBoard (new int[] {3,3,0,0,0});
	
	public static void main(String[] args) {
		// Realizamos las busquedas con los distintos algoritmos
		CanibalesSearch(new BreadthFirstSearch(new GraphSearch()), initial, "BFS");
		System.out.println();
		CanibalesSearch(new DepthLimitedSearch(11), initial, "DLS(11)");
		System.out.println();
		CanibalesSearch(new IterativeDeepeningSearch(), initial, "IDLS");
	};
	
	
  	public static void executeActions(List<Action> actions, Problem problem) {
		
		Object initialState = problem.getInitialState();
		ResultFunction resultFunction = problem.getResultFunction();
		
		Object state = initialState;
		System.out.println("INITIAL STATE");
		System.out.println(state);
		
		for (Action action : actions) {
			System.out.println(action.toString());
			state = resultFunction.result(state, action);
			System.out.println(state);
			System.out.println("- - -");
		}
	};
	
	public static void CanibalesSearch(Search search, Object state, String alg) {
			int depth,expandedNodes,queueSize,maxQueueSize;
			
				try {
					System.out.println("Misioneros y canibales " + alg + " -->"); 
					Problem problema = new Problem(state, CanibalesFunctionFactory.getActionsFunction(),
					CanibalesFunctionFactory.getResultFunction(), new CanibalesGoalTest());
					long time = System.currentTimeMillis();
					
					SearchAgent agent = new SearchAgent(problema, search);
					List<Action> actions = agent.getActions();
					time = System.currentTimeMillis() - time;
					
					
					String pathcostM =agent.getInstrumentation().getProperty("pathCost");
					if (pathcostM!=null) depth = (int)Float.parseFloat(pathcostM);
					else depth = 0;
					if (agent.getInstrumentation().getProperty("nodesExpanded")==null) expandedNodes= 0;
					else expandedNodes =
					(int)Float.parseFloat(agent.getInstrumentation().getProperty("nodesExpanded"));
					if (agent.getInstrumentation().getProperty("queueSize")==null) queueSize=0;
					else queueSize = (int)Float.parseFloat(agent.getInstrumentation().getProperty("queueSize"));
					if (agent.getInstrumentation().getProperty("maxQueueSize")==null) maxQueueSize= 0;
					else maxQueueSize =
					(int)Float.parseFloat(agent.getInstrumentation().getProperty("maxQueueSize"));
					
					System.out.printf("pathCost : %d\n", depth);
					System.out.printf("nodesExpanded : %d\n", expandedNodes);
					if(alg == "BFS") {
						System.out.printf("queueSize : %d\n", queueSize);
						System.out.printf("maxQueueSize : %d\n", maxQueueSize);
					}
					System.out.printf("Tiempo : %d\n", time);
					System.out.println("SOLUCIÓN:");
					System.out.println("GOAL STATE");
					System.out.println("RIBERA-IZQ --RIO-- BOTE M M M C C C RIBERA-DCH");
					System.out.println("CAMINO ENCONTRADO");
					executeActions(actions, problema);
				}
					catch (Exception e) {
						e.printStackTrace();
					}			

	};
}