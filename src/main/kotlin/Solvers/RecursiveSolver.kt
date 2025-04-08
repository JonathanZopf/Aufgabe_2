package org.hszg.Solvers

import org.hszg.Chessboard
import org.hszg.Solver

class RecursiveSolver : Solver {
    override fun solve(size: Int): List<Chessboard> {
        val allSolutions = mutableListOf<Chessboard>()

        findAllSolutions(
            size = size,
            row = 0,
            placed = mutableListOf(),
            cols = mutableSetOf(),
            diag1 = mutableSetOf(),
            diag2 = mutableSetOf(),
            result = allSolutions
        )

        return allSolutions
    }

    private fun findAllSolutions(
        size: Int,
        row: Int,
        placed: MutableList<Pair<Int, Int>>,
        cols: MutableSet<Int>,
        diag1: MutableSet<Int>,
        diag2: MutableSet<Int>,
        result: MutableList<Chessboard>
    ) {
        if (row == size) {
            val board = Chessboard(size)
            for ((x, y) in placed) {
                board.addQueen(x, y)
            }
            result.add(board) // <-- Always add, no uniqueness check
            return
        }

        for (col in 0 until size) {
            val d1 = row + col
            val d2 = row - col

            if (col in cols || d1 in diag1 || d2 in diag2) continue

            placed.add(Pair(col, row))
            cols.add(col)
            diag1.add(d1)
            diag2.add(d2)

            findAllSolutions(size, row + 1, placed, cols, diag1, diag2, result)

            placed.removeLast()
            cols.remove(col)
            diag1.remove(d1)
            diag2.remove(d2)
        }
    }
}
