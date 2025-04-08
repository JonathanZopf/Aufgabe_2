package org.hszg

import org.hszg.Solvers.RecursiveSolver

fun main() {
    val queensToPlace = 12
    val solver = RecursiveSolver()
    val startingTimeSolve = System.currentTimeMillis()
    val allSolutions = solver.solve(queensToPlace)
    val endingTimeSolve = System.currentTimeMillis()
    val startingTimeFilter = System.currentTimeMillis()
    val uniqueSolutions = filterOnlyUniqueSolutions(allSolutions)
    val endingTimeFilter = System.currentTimeMillis()

    for ((i, board) in uniqueSolutions.withIndex()) {
        println("Unique Solution ${i + 1}:")
        board.printBoard()
        println()
    }

    println("Found ${uniqueSolutions.size} unique solutions out of ${allSolutions.size} total solutions.")
    println("Time taken to solve: ${endingTimeSolve - startingTimeSolve} ms")
    println("Time taken to filter unique solutions: ${endingTimeFilter - startingTimeFilter} ms")
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