package aima.gui.demo.search;

import java.util.HashSet;
import java.util.Set;

import aima.core.environment.nqueens.AttackingPairsHeuristic;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFitnessFunction;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.search.framework.Problem;
import aima.core.search.framework.SearchAgent;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.local.Individual;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * @author Guillermo Bajo
 * 
 */

public class NQueensLocal {

	private static final int _boardSize = 8;
	
	public static void main(String[] args) {

		newNQueensLocal();
	}

	private static void newNQueensLocal() {
		
		nQueensHillClimbingSearch_Statistics(1000);
		
		System.out.println("\n");
		
		NQueensRandomRestartHillClimbing();
		
		System.out.println("\n");
		
		nQueensSimulatedAnnealing_Statistics(1000);
		
		System.out.println("\n");
		
		NQueensSimulatedAnnealingRestart();
		
		System.out.println("\n");
		
		nQueensGeneticAlgorithmSearch();
	}
	
	
	
	
	private static void nQueensHillClimbingSearch_Statistics(int numExperiments) {
		System.out.printf("\nNQueens HillClimbing con %d estados iniciales diferentes  -->\n", numExperiments);
		int pasosExito = 0, exitos = 0, fallos = 0, costeFallos = 0;
		try {
			Set<NQueensBoard> setNQ = generateSetNQ(_boardSize, numExperiments);
			for (NQueensBoard board : setNQ) {
				
				Problem problem = new Problem(board,
						NQueensFunctionFactory.getCActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				HillClimbingSearch search = new HillClimbingSearch(
						new AttackingPairsHeuristic());
				SearchAgent agent = new SearchAgent(problem, search);
				if (search.getOutcome().toString().contentEquals("SOLUTION_FOUND")) {
					exitos++;
					pasosExito += agent.getActions().size();
				} else {
					fallos++;
					costeFallos += agent.getActions().size();
				}
			}
			
			System.out.printf("Fallos: %.2f%%\n", (double) fallos/numExperiments*100);
			System.out.printf("Coste medio fallos: %.2f\n", (double) costeFallos / fallos);
			System.out.printf("Exitos: %.2f%%\n", (double) exitos/numExperiments*100);
			System.out.printf("Coste medio exitos: %.2f\n", (double) pasosExito / exitos);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static void NQueensRandomRestartHillClimbing() {
		boolean exito = false;
		int intentos = 0, pasosExito = 0, costeFallos = 0;
		try {
			while (!exito) {
				Problem problem = new Problem(generateRandomNQ(_boardSize),
						NQueensFunctionFactory.getCActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				HillClimbingSearch search = new HillClimbingSearch(
						new AttackingPairsHeuristic());
				SearchAgent agent = new SearchAgent(problem, search);
				
				intentos++;
				if(search.getOutcome().toString().contentEquals("SOLUTION_FOUND")) {
					pasosExito += agent.getActions().size();
					
					System.out.println("Search Outcome=" + search.getOutcome().toString());
					System.out.println("Final State=\n" + search.getLastSearchState());
					System.out.printf("Numero de intentos: %d\n", intentos);
					System.out.printf("Coste medio fallos: %.2f\n", (double) costeFallos/(intentos-1));
					System.out.println("Coste exito: " + (pasosExito + costeFallos));
					System.out.printf("Coste medio exito: %d\n", pasosExito);
					
					exito = true;
				}
				else {
					costeFallos += agent.getActions().size();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private static void nQueensSimulatedAnnealing_Statistics(int numExperiments) {
		System.out.printf("NQueens Simulated Annealing con %d estados iniciales diferentes -->\n", numExperiments);
		int pasosExito = 0, exitos = 0, fallos = 0, costeFallos = 0;
		try {
			int k = 10, limit = 500;
			double lam = 0.1;
			Scheduler sched = new Scheduler(k, lam, limit);
			System.out.printf("Parametros Scheduler: Scheduler (%d, %.1f, %d)\n\n", k, lam, limit);
			
			Set<NQueensBoard> setNQ = generateSetNQ(_boardSize, numExperiments);
			for (NQueensBoard board : setNQ) {
				
				Problem problem = new Problem(board,
						NQueensFunctionFactory.getCActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(
						new AttackingPairsHeuristic(), sched);
				SearchAgent agent = new SearchAgent(problem, search);
				if (search.getOutcome().toString().contentEquals("SOLUTION_FOUND")) {
					exitos++;
					pasosExito += agent.getActions().size();
				} else {
					fallos++;
					costeFallos += agent.getActions().size();
				}
			}
			System.out.printf("Fallos: %.2f%%\n", (double) fallos/numExperiments*100);
			System.out.printf("Coste medio fallos: %.2f\n", (double) costeFallos / fallos);
			System.out.printf("Exitos: %.2f%%\n", (double) exitos/numExperiments*100);
			System.out.printf("Coste medio exitos: %.2f\n", (double) pasosExito / exitos);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private static void NQueensSimulatedAnnealingRestart() {
		boolean exito = false;
		int intentos = 0, pasosExito = 0, costeFallos = 0;
		try {
			int k = 10, limit = 500;
			double lam = 0.1;
			Scheduler sched = new Scheduler(k, lam, limit);
			while (!exito) {
				Problem problem = new Problem(generateRandomNQ(_boardSize),
						NQueensFunctionFactory.getCActionsFunction(),
						NQueensFunctionFactory.getResultFunction(),
						new NQueensGoalTest());
				SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(
						new AttackingPairsHeuristic(), sched);
				SearchAgent agent = new SearchAgent(problem, search);
				
				intentos++;
				if(search.getOutcome().toString().contentEquals("SOLUTION_FOUND")) {
					pasosExito += agent.getActions().size();
					
					System.out.println("Search Outcome=" + search.getOutcome().toString());
					System.out.println("Final State=\n" + search.getLastSearchState());
					System.out.printf("Numero de intentos: %d\n", intentos);
					System.out.printf("Fallos: %d\n", intentos-1);
					System.out.println("Coste exito: " + (pasosExito + costeFallos));
					
					exito = true;
				}
				else {
					costeFallos += agent.getActions().size();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void nQueensGeneticAlgorithmSearch() {
		System.out.println("\nGeneticAlgorithm");
		try {
			NQueensFitnessFunction fitnessFunction = new NQueensFitnessFunction();
			// Generate an initial population
			Set<Individual<Integer>> population = new HashSet<Individual<Integer>>();
			for (int i = 0; i < 50; i++) {
				population.add(fitnessFunction
						.generateRandomIndividual(_boardSize));
			}

			GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<Integer>(
					_boardSize,
					fitnessFunction.getFiniteAlphabetForBoardOfSize(_boardSize),
					0.15);

			// Run for a set amount of time
			Individual<Integer> bestIndividual = ga.geneticAlgorithm(
					population, fitnessFunction, fitnessFunction, 1000L);

			// Run till goal is achieved
			bestIndividual = ga.geneticAlgorithm(population, fitnessFunction,
					fitnessFunction, 0L);
			System.out.println("Parámetros iniciales: \tPoblación: "
					+ ga.getPopulationSize() + ", " + "Probabilidad mutación: 0.15");
			System.out.println("Mejor individuo=\n"
					+ fitnessFunction.getBoardForIndividual(bestIndividual));
			System.out.println("Tamaño tablero      = " + _boardSize);
			System.out.println("Fitness             = "
					+ fitnessFunction.getValue(bestIndividual));
			System.out.println("Es objetivo         = "
					+ fitnessFunction.isGoalState(bestIndividual));
			System.out.println("Tamaño de población = " + ga.getPopulationSize());
			System.out.println("Iteraciones         = " + ga.getIterations());
			System.out.println("Tiempo              = "
					+ ga.getTimeInMilliseconds() + "ms.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static NQueensBoard generateRandomNQ(int size) {
		NQueensBoard board = new NQueensBoard(size);
		for (int c = 0; c < size; c++) {
			board.addQueenAt(new XYLocation(c, (int)(Math.random()*size)));
		}
		return board;
	}
	
	public static Set<NQueensBoard> generateSetNQ(int size, int sizelist) {
		Set<NQueensBoard> set = new HashSet<NQueensBoard>();
		while (set.size() < sizelist) {
			set.add(generateRandomNQ(size));
		}
		return set;
	}

}