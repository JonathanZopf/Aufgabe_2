package org.hszg.Solvers

import org.hszg.Chessboard
import org.hszg.Queen
import org.hszg.Solver

class IterativeRepairSolver : Solver {
    private val maxIterations = 10000
    private val random = java.util.Random()

    override fun solve(size: Int): List<Chessboard> {
        val solutions = mutableSetOf<Chessboard>()

        // Mehrere Versuche, um verschiedene Lösungen zu finden
        repeat(size.times(2) * 1000) {
            val board = createInitialBoard(size)
            var iterations = 0

            while (iterations < maxIterations) {
                val conflicts = countAllConflicts(board)
                if (conflicts == 0) {
                    // Lösung gefunden
                    if (!solutions.any { it == board }) {
                        solutions.add(board.copy())
                    }
                    break
                }

                // Dame mit den meisten Konflikten finden
                val queenWithMostConflicts = findQueenWithMostConflicts(board)
                val column = queenWithMostConflicts.position.first

                // Beste neue Position in dieser Spalte finden
                val bestRow = findBestRowForColumn(board, column)

                // Dame bewegen
                board.removeQueen(column, queenWithMostConflicts.position.second)
                board.addQueen(column, bestRow)

                iterations++
            }
        }

        return solutions.toList()
    }

    private fun createInitialBoard(size: Int): Chessboard {
        val board = Chessboard(size)
        // Jede Dame in eine zufällige Zeile ihrer Spalte setzen
        for (x in 0 until size) {
            val y = random.nextInt(size)
            board.addQueen(x, y)
        }
        return board
    }

    private fun countAllConflicts(board: Chessboard): Int {
        var totalConflicts = 0
        for (i in 0 until board.queens.size) {
            for (j in i + 1 until board.queens.size) {
                if (!board.queens[i].satisfiesConstraintsWithOther(board.queens[j])) {
                    totalConflicts++
                }
            }
        }
        return totalConflicts
    }

    private fun findQueenWithMostConflicts(board: Chessboard): Queen {
        val conflictCounts = mutableMapOf<Queen, Int>()

        // Konflikte für jede Dame zählen
        for (i in 0 until board.queens.size) {
            var conflicts = 0
            for (j in 0 until board.queens.size) {
                if (i != j && !board.queens[i].satisfiesConstraintsWithOther(board.queens[j])) {
                    conflicts++
                }
            }
            conflictCounts[board.queens[i]] = conflicts
        }

        // Dame mit den meisten Konflikten zurückgeben
        return conflictCounts.maxByOrNull { it.value }!!.key
    }

    private fun findBestRowForColumn(board: Chessboard, column: Int): Int {
        val currentRow = board.queens.first { it.position.first == column }.position.second
        var minConflicts = Int.MAX_VALUE
        val bestRows = mutableListOf<Int>()

        // Konflikte für jede mögliche Zeile in dieser Spalte berechnen
        for (row in 0 until board.size) {
            if (row == currentRow) continue

            var conflicts = 0
            // Temporäre Dame für diese Position erstellen
            val tempQueen = Queen(column, row)

            // Konflikte mit allen anderen Damen zählen
            for (queen in board.queens) {
                if (queen.position.first != column && !tempQueen.satisfiesConstraintsWithOther(queen)) {
                    conflicts++
                }
            }

            // Beste Zeilen verfolgen
            when {
                conflicts < minConflicts -> {
                    minConflicts = conflicts
                    bestRows.clear()
                    bestRows.add(row)
                }
                conflicts == minConflicts -> {
                    bestRows.add(row)
                }
            }
        }

        // Zufällige beste Zeile auswählen (falls mehrere gleich gut sind)
        return bestRows.random()
    }

    private fun Chessboard.copy(): Chessboard {
        val newBoard = Chessboard(size)
        queens.forEach { newBoard.addQueen(it.position.first, it.position.second) }
        return newBoard
    }
}