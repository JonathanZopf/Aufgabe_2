package org.hszg.Solvers

import org.hszg.Chessboard
import org.hszg.Solver
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveAction

class ForkJoinRecursiveSolver : Solver {
    private val pool = ForkJoinPool.commonPool()

    override fun solve(size: Int): List<Chessboard> {
        val solutions = ConcurrentLinkedQueue<Chessboard>()

        val tasks = (0 until size).map { col ->
            SolveTask(
                size = size,
                row = 0,
                initialCol = col,
                solutions = solutions
            )
        }

        tasks.forEach { pool.execute(it) }
        tasks.forEach { it.join() }

        return solutions.toList()
    }

    private class SolveTask(
        private val size: Int,
        private val row: Int,
        private val initialCol: Int,
        private val solutions: ConcurrentLinkedQueue<Chessboard>
    ) : RecursiveAction() {
        override fun compute() {
            val placed = mutableListOf(Pair(initialCol, row))
            val cols = mutableSetOf(initialCol)
            val diag1 = mutableSetOf(row + initialCol)
            val diag2 = mutableSetOf(row - initialCol)

            recursiveSolve(row + 1, placed, cols, diag1, diag2)
        }

        private fun recursiveSolve(
            row: Int,
            placed: MutableList<Pair<Int, Int>>,
            cols: MutableSet<Int>,
            diag1: MutableSet<Int>,
            diag2: MutableSet<Int>
        ) {
            if (row == size) {
                val board = Chessboard(size)
                placed.forEach { (x, y) -> board.addQueen(x, y) }
                solutions.add(board)
                return
            }

            val subtasks = mutableListOf<RecursiveAction>()

            for (col in 0 until size) {
                val d1 = row + col
                val d2 = row - col

                if (col in cols || d1 in diag1 || d2 in diag2) continue

                if (row < size / 2) { // Parallelisiere nur die obere Hälfte
                    val task = object : RecursiveAction() {
                        override fun compute() {
                            placed.add(Pair(col, row))
                            cols.add(col)
                            diag1.add(d1)
                            diag2.add(d2)

                            recursiveSolve(row + 1, placed, cols, diag1, diag2)

                            placed.removeLast()
                            cols.remove(col)
                            diag1.remove(d1)
                            diag2.remove(d2)
                        }
                    }
                    subtasks.add(task)
                } else {
                    placed.add(Pair(col, row))
                    cols.add(col)
                    diag1.add(d1)
                    diag2.add(d2)

                    recursiveSolve(row + 1, placed, cols, diag1, diag2)

                    placed.removeLast()
                    cols.remove(col)
                    diag1.remove(d1)
                    diag2.remove(d2)
                }
            }

            if (subtasks.isNotEmpty()) {
                invokeAll(subtasks)
            }
        }
    }
}