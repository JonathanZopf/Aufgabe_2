package org.hszg.Solvers

import org.hszg.Chessboard
import org.hszg.Solver

class JonathanRecursiveSolver : Solver {
    override fun solve(size: Int): List<Chessboard> {
        val allSolutions = mutableListOf<Chessboard>()
        val board = Chessboard(size)
        return allSolutions
    }
}