package org.hszg

import org.hszg.Solvers.RecursiveSolver

fun main() {
    val queensToPlace = 8
    val solver = RecursiveSolver()
    val startingTime = System.currentTimeMillis()
    val allSolutions = solver.solve(queensToPlace)
    val endingTime = System.currentTimeMillis()
    val uniqueSolutions = filterOnlyUniqueSolutions(allSolutions)

    for ((i, board) in uniqueSolutions.withIndex()) {
        println("Unique Solution ${i + 1}:")
        board.printBoard()
        println()
    }

    println("Found ${uniqueSolutions.size} unique solutions out of ${allSolutions.size} total solutions in ${endingTime - startingTime} ms.")
}

private fun filterOnlyUniqueSolutions(allSolutions: List<Chessboard>): List<Chessboard> {
    val uniqueSolutions = mutableListOf<Chessboard>()
    for (solution in allSolutions) {
        if (uniqueSolutions.none { it == solution }) {
            uniqueSolutions.add(solution)
        }
    }
    return uniqueSolutions
}
