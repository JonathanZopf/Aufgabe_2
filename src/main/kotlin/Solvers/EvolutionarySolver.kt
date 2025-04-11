package org.hszg.Solvers

import org.hszg.Chessboard
import org.hszg.Solver

class EvolutionarySolver : Solver {
    // Parameter des evolutionären Algorithmus
    private val populationSize = 100
    private val maxGenerations = 1000
    private val eliteSize = 10
    private val crossoverRate = 0.8
    private val mutationRate = 0.2
    private val random = java.util.Random()

    override fun solve(size: Int): List<Chessboard> {
        var population = initializePopulation(size)
        val solutions = mutableListOf<Chessboard>()

        for (generation in 0 until maxGenerations) {
            // Bewertung der Population
            val evaluatedPopulation = population.map { board ->
                Pair(board, fitnessFunction(board))
            }.sortedByDescending { it.second }

            // Überprüfen auf Lösungen
            val best = evaluatedPopulation.first()
            if (best.second == 1.0) { // Perfekte Lösung gefunden
                val solution = best.first.copy()
                if (solutions.none { it == solution }) {
                    solutions.add(solution)
                    if (solutions.size >= 3) { // Begrenze die Anzahl der Lösungen
                        return solutions
                    }
                }
            }

            // Neue Generation erstellen
            val newPopulation = mutableListOf<Chessboard>()

            // Elitismus: Die besten Individuen überleben
            newPopulation.addAll(evaluatedPopulation.take(eliteSize).map { it.first.copy() })

            // Crossover und Mutation
            while (newPopulation.size < populationSize) {
                // Selektion
                val parent1 = tournamentSelection(evaluatedPopulation)
                val parent2 = tournamentSelection(evaluatedPopulation)

                // Crossover
                val offspring = if (random.nextDouble() < crossoverRate) {
                    crossover(parent1, parent2)
                } else {
                    if (random.nextBoolean()) parent1.copy() else parent2.copy()
                }

                // Mutation
                val mutatedOffspring = if (random.nextDouble() < mutationRate) {
                    mutate(offspring)
                } else {
                    offspring
                }

                newPopulation.add(mutatedOffspring)
            }

            population = newPopulation
        }

        return solutions
    }

    private fun initializePopulation(size: Int): List<Chessboard> {
        return List(populationSize) {
            val board = Chessboard(size)
            // Jede Spalte hat genau eine Dame in einer zufälligen Zeile
            for (x in 0 until size) {
                val y = random.nextInt(size)
                board.addQueen(x, y)
            }
            board
        }
    }

    private fun fitnessFunction(board: Chessboard): Double {
        var conflicts = 0
        val queens = board.queens

        for (i in 0 until queens.size) {
            for (j in i + 1 until queens.size) {
                if (!queens[i].satisfiesConstraintsWithOther(queens[j])) {
                    conflicts++
                }
            }
        }

        // Fitness ist 1 / (1 + conflicts), sodass 1.0 eine perfekte Lösung ist
        return 1.0 / (1.0 + conflicts)
    }

    private fun tournamentSelection(population: List<Pair<Chessboard, Double>>): Chessboard {
        // Turniergröße von 5
        val tournament = population.shuffled().take(5)
        return tournament.maxByOrNull { it.second }!!.first.copy()
    }

    private fun crossover(parent1: Chessboard, parent2: Chessboard): Chessboard {
        val child = Chessboard(parent1.size)
        val crossoverPoint = random.nextInt(parent1.size)

        for (x in 0 until parent1.size) {
            val y = if (x < crossoverPoint) {
                parent1.queens[x].position.second
            } else {
                parent2.queens[x].position.second
            }
            child.addQueen(x, y)
        }

        return child
    }

    private fun mutate(board: Chessboard): Chessboard {
        val mutated = board.copy()
        // Wähle eine zufällige Spalte
        val column = random.nextInt(board.size)
        val currentRow = mutated.queens[column].position.second
        var newRow = currentRow

        // Stelle sicher, dass wir die Dame tatsächlich bewegen
        while (newRow == currentRow) {
            newRow = random.nextInt(board.size)
        }

        mutated.removeQueen(column, currentRow)
        mutated.addQueen(column, newRow)

        return mutated
    }

    private fun Chessboard.copy(): Chessboard {
        val newBoard = Chessboard(size)
        queens.forEach { newBoard.addQueen(it.position.first, it.position.second) }
        return newBoard
    }
}