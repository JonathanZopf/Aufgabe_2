package org.hszg

import org.hszg.Solvers.RecursiveSolver

fun main() {
    val queensToPlace = 14
    val solver = RecursiveSolver()
    val startingTimeSolve = System.currentTimeMillis()
    val uniqueSolutions = solver.solve(queensToPlace)
    val endingTimeSolve = System.currentTimeMillis()

   /* for ((i, board) in uniqueSolutions.withIndex()) {
        println("Unique Solution ${i + 1}:")
        board.printBoard()
        println()
    }*/

    println("Found ${uniqueSolutions.size} unique solutions.")
    println("Time taken to solve: ${endingTimeSolve - startingTimeSolve} ms")
}