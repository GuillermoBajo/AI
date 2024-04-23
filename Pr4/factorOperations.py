# factorOperations.py
# -------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).

from typing import List, ValuesView
from bayesNet import Factor
import functools
from util import raiseNotDefined

def joinFactorsByVariableWithCallTracking(callTrackingList=None):


    def joinFactorsByVariable(factors: List[Factor], joinVariable: str):
        """
        Input factors is a list of factors.
        Input joinVariable is the variable to join on.

        This function performs a check that the variable that is being joined on 
        appears as an unconditioned variable in only one of the input factors.

        Then, it calls your joinFactors on all of the factors in factors that 
        contain that variable.

        Returns a tuple of 
        (factors not joined, resulting factor from joinFactors)
        """

        if not (callTrackingList is None):
            callTrackingList.append(('join', joinVariable))

        currentFactorsToJoin =    [factor for factor in factors if joinVariable in factor.variablesSet()]
        currentFactorsNotToJoin = [factor for factor in factors if joinVariable not in factor.variablesSet()]

        # typecheck portion
        numVariableOnLeft = len([factor for factor in currentFactorsToJoin if joinVariable in factor.unconditionedVariables()])
        if numVariableOnLeft > 1:
            print("Factors failed joinFactorsByVariable typecheck: ", factors)
            raise ValueError("The joinBy variable can only appear in one factor as an \nunconditioned variable. \n" +  
                               "joinVariable: " + str(joinVariable) + "\n" +
                               ", ".join(map(str, [factor.unconditionedVariables() for factor in currentFactorsToJoin])))
        
        joinedFactor = joinFactors(currentFactorsToJoin)
        return currentFactorsNotToJoin, joinedFactor

    return joinFactorsByVariable

joinFactorsByVariable = joinFactorsByVariableWithCallTracking()

########### ########### ###########
########### QUESTION 2  ###########
########### ########### ###########

def joinFactors(factors: ValuesView[Factor]):
    """
    factors: can iterate over it as if it was a list, and convert to a list.
    
    You should calculate the set of unconditioned variables and conditioned 
    variables for the join of those factors.

    Return a new factor that has those variables and whose probability entries 
    are product of the corresponding rows of the input factors.

    You may assume that the variableDomainsDict for all the input 
    factors are the same, since they come from the same BayesNet.

    joinFactors will only allow unconditionedVariables to appear in 
    one input factor (so their join is well defined).

    Hint: Factor methods that take an assignmentDict as input 
    (such as getProbability and setProbability) can handle 
    assignmentDicts that assign more variables than are in that factor.

    Useful functions:
    Factor.getAllPossibleAssignmentDicts
    Factor.getProbability
    Factor.setProbability
    Factor.unconditionedVariables
    Factor.conditionedVariables
    Factor.variableDomainsDict
    """

    # typecheck portion
    setsOfUnconditioned = [set(factor.unconditionedVariables()) for factor in factors]
    if len(factors) > 1:
        intersect = functools.reduce(lambda x, y: x & y, setsOfUnconditioned)
        if len(intersect) > 0:
            print("Factors failed joinFactors typecheck: ", factors)
            raise ValueError("unconditionedVariables can only appear in one factor. \n"
                    + "unconditionedVariables: " + str(intersect) + 
                    "\nappear in more than one input factor.\n" + 
                    "Input factors: \n" +
                    "\n".join(map(str, factors)))


    setsOfConditioned = [set(factor.conditionedVariables()) for factor in factors]
    if len(factors) > 1:
        intersect2 = functools.reduce(lambda x, y: x & y, setsOfConditioned)

    factors_list = [factor for factor in factors]
    variableDomainsDict = dict(factors_list[0].variableDomainsDict())
    

# Calcular el conjunto de variables no condicionadas y condicionadas para la unión
    unconditioned_vars = set()
    conditioned_vars = set()

    for factor in factors_list:
        unconditioned_vars.update(factor.unconditionedVariables())
        conditioned_vars.update(factor.conditionedVariables())

    # Eliminar variables comunes del conjunto no condicionado
    conditioned_vars = conditioned_vars - unconditioned_vars

    # Crear un nuevo factor con las variables unidas
    joined_factor = Factor(list(unconditioned_vars), list(conditioned_vars), variableDomainsDict)

    # Calcular el producto de las filas correspondientes de los factores de entrada y establecerlo en el nuevo factor
    for assignment_dict in joined_factor.getAllPossibleAssignmentDicts():
        probability = 1.0
        for factor in factors_list:
            # Utilizar getProbability con assignment_dict que incluye tanto variables no condicionadas como condicionadas
            probability *= factor.getProbability(assignment_dict)
        joined_factor.setProbability(assignment_dict, probability)

    return joined_factor


########### ########### ###########
########### QUESTION 3  ###########
########### ########### ###########

def eliminateWithCallTracking(callTrackingList=None):

    def eliminate(factor: Factor, eliminationVariable: str):
        """
        Input factor is a single factor.
        Input eliminationVariable is the variable to eliminate from factor.
        eliminationVariable must be an unconditioned variable in factor.
        
        You should calculate the set of unconditioned variables and conditioned 
        variables for the factor obtained by eliminating the variable
        eliminationVariable.

        Return a new factor where all of the rows mentioning
        eliminationVariable are summed with rows that match
        assignments on the other variables.

        Useful functions:
        Factor.getAllPossibleAssignmentDicts
        Factor.getProbability
        Factor.setProbability
        Factor.unconditionedVariables
        Factor.conditionedVariables
        Factor.variableDomainsDict
        """
        # autograder tracking -- don't remove
        if not (callTrackingList is None):
            callTrackingList.append(('eliminate', eliminationVariable))

        # typecheck portion
        if eliminationVariable not in factor.unconditionedVariables():
            print("Factor failed eliminate typecheck: ", factor)
            raise ValueError("Elimination variable is not an unconditioned variable " \
                            + "in this factor\n" + 
                            "eliminationVariable: " + str(eliminationVariable) + \
                            "\nunconditionedVariables:" + str(factor.unconditionedVariables()))
        
        if len(factor.unconditionedVariables()) == 1:
            print("Factor failed eliminate typecheck: ", factor)
            raise ValueError("Factor has only one unconditioned variable, so you " \
                    + "can't eliminate \nthat variable.\n" + \
                    "eliminationVariable:" + str(eliminationVariable) + "\n" +\
                    "unconditionedVariables: " + str(factor.unconditionedVariables()))


        # Obtener las variables no condicionadas, condicionadas y dominios de variables del factor original
        fact = factor.variableDomainsDict()
        conditionedvars = factor.conditionedVariables()
        unconditionedvars = factor.unconditionedVariables()

        # Eliminar la variable de eliminación del conjunto de variables no condicionadas
        unconditionedvars.remove(eliminationVariable)

        # Crear un nuevo factor con las variables actualizadas
        newFactor = Factor(unconditionedvars, conditionedvars, fact)

        # Obtener todas las asignaciones posibles para las variables en el nuevo factor
        asigs = newFactor.getAllPossibleAssignmentDicts()

        # Actualizar las probabilidades en el nuevo factor después de eliminar la variable
        for asig in asigs:
            probs = 0
            elimVar = factor.variableDomainsDict()[eliminationVariable]

            # Iterar sobre los posibles valores de la variable de eliminación
            for ev in elimVar:
                asig[eliminationVariable] = ev
                probs += factor.getProbability(asig)

            # Establecer la probabilidad en el nuevo factor para la asignación actual
            newFactor.setProbability(asig, probs)

        # Devolver el nuevo factor actualizado después de la eliminación de la variable
        return newFactor


    return eliminate

eliminate = eliminateWithCallTracking()

