package org.hszg

class Chessboard(val size: Int) {
    val queens: MutableList<Queen> = mutableListOf()

    fun addQueen(x: Int, y: Int) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw IllegalArgumentException("Position out of bounds")
        }
        queens.add(Queen(x, y))
    }

    fun removeQueen(x: Int, y: Int) {
        queens.removeIf { it.position == Pair(x, y) }
    }

    fun printBoard() {
        // Print column headers (A, B, C, ...)
        print("    ") // Padding for row numbers
        for (x in 0 until size) {
            print(" ${('A' + x)} ") // Column letters with spacing
        }
        println()

        for (y in 0 until size) {
            // Print row number with padding
            print("${(y).toString().padStart(2)}  ") // Right-aligned row numbers

            // Print board row
            for (x in 0 until size) {
                val hasQueen = queens.any { it.position == Pair(x, y) }
                print(if (hasQueen) " ♛ " else " ☐ ")
            }
            println()
        }
    }

    /**
     * Returns a normalized version of the chessboard.
     * This is useful for comparing different chessboards
     * The normalized version is defined as the rotation and reflection where the most
     */
    fun getNormalized(): Chessboard {
        val allForms = mutableListOf<Chessboard>()
        val transforms = listOf<(Pair<Int, Int>) -> Pair<Int, Int>>(
            { it }, // identity (original)
            { (x, y) -> Pair(size - 1 - y, x) }, // rotate 90°
            { (x, y) -> Pair(size - 1 - x, size - 1 - y) }, // rotate 180°
            { (x, y) -> Pair(y, size - 1 - x) }, // rotate 270°
            { (x, y) -> Pair(size - 1 - x, y) }, // reflect over vertical axis
            { (x, y) -> Pair(x, size - 1 - y) }, // reflect over horizontal axis
            { (x, y) -> Pair(y, x) }, // reflect over top-left to bottom-right diagonal
            { (x, y) -> Pair(size - 1 - y, size - 1 - x) } // reflect over top-right to bottom-left diagonal
        )

        for (transform in transforms) {
            val transformedBoard = Chessboard(size)
            for (queen in queens) {
                val newPos = transform(queen.position)
                transformedBoard.addQueen(newPos.first, newPos.second)
            }
            allForms.add(transformedBoard)
        }

        allForms.sortBy { board ->
            board.ranking()
        }

        return allForms.first()
    }

    private fun ranking(): Int {
        // Flatten queen positions into a single integer for deterministic ranking.
        // Example: For queens at (0,1), (1,3), (2,0), return 013020 (concatenated coordinates).
        return queens.sortedWith(compareBy({ it.position.first }, { it.position.second }))
            .fold(0) { acc, queen ->
                (acc * 100) + (queen.position.first * 10) + queen.position.second
            }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Chessboard) return false
        if (size != other.size) return false

        // Compare queens' positions
        if (queens.size != other.queens.size) return false
        queens.sortBy { it.position.first + it.position.second }
        other.queens.sortBy { it.position.first + it.position.second }
        for (i in queens.indices) {
            if (queens[i].position != other.queens[i].position) return false
        }
        return true
    }

    override fun hashCode(): Int {
        return queens.fold(0) { acc, queen ->
            acc * 31 + queen.position.hashCode()
        }
    }
}