package org.hszg.Solvers

import org.hszg.Chessboard
import org.hszg.Solver

/**
 * A recursive backtracking solver for the N-Queens problem.
 * Generates all unique solutions by exploring valid queen placements row by row,
 * avoiding conflicts using sets for columns and diagonals.
 */
class RecursiveSolver : Solver {

    /**
     * Entry point for solving the N-Queens problem.
     * @param size The size of the chessboard (e.g., 8 for standard 8-Queens).
     * @return List of unique solutions, each represented as a normalized Chessboard.
     */
    override fun solve(size: Int): List<Chessboard> {
        val allSolutions = mutableListOf<Chessboard>()

        findAllSolutions(
            size = size,
            row = 0, // Start placing queens from the first row (0-indexed)
            placed = mutableListOf(), // Tracks queen positions (col, row)
            cols = mutableSetOf(), // Tracks occupied columns
            diag1 = mutableSetOf(), // Tracks occupied "top-left to bottom-right" diagonals (row + col)
            diag2 = mutableSetOf(), // Tracks occupied "top-right to bottom-left" diagonals (row - col)
            result = allSolutions // Stores all unique solutions
        )

        return allSolutions
    }

    /**
     * Recursive helper to find all valid N-Queens solutions.
     * @param size Chessboard size.
     * @param row Current row being processed.
     * @param placed List of already placed queens (col, row).
     * @param cols Set of occupied columns.
     * @param diag1 Set of occupied diagonals (row + col).
     * @param diag2 Set of occupied diagonals (row - col).
     * @param result Mutable list to store unique solutions.
     */
    private fun findAllSolutions(
        size: Int,
        row: Int,
        placed: MutableList<Pair<Int, Int>>,
        cols: MutableSet<Int>,
        diag1: MutableSet<Int>,
        diag2: MutableSet<Int>,
        result: MutableList<Chessboard>,
    ) {
        // Base case: All queens placed successfully
        if (row == size) {
            val board = Chessboard(size)
            for ((x, y) in placed) {
                board.addQueen(x, y) // Reconstruct the board from placed queens
            }

            // Normalize the board to handle symmetries (rotations/reflections)
            val normalized = board.getNormalized()

            // Add to results only if it's a new unique solution
            if (!result.any { it == normalized }) {
                result.add(normalized)
            }
            return
        }

        // Try placing a queen in each column of the current row
        for (col in 0 until size) {
            val d1 = row + col // Diagonal 1 identifier
            val d2 = row - col  // Diagonal 2 identifier

            // Skip if column or diagonals are already occupied
            if (col in cols || d1 in diag1 || d2 in diag2) continue

            // Place the queen and mark conflicts
            placed.add(Pair(col, row))
            cols.add(col)
            diag1.add(d1)
            diag2.add(d2)

            // Recurse to the next row
            findAllSolutions(size, row + 1, placed, cols, diag1, diag2, result)

            // Backtrack: Remove the queen and unmark conflicts
            placed.removeLast()
            cols.remove(col)
            diag1.remove(d1)
            diag2.remove(d2)
        }
    }
}