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

    // New method to get canonical form for faster comparison
    fun getCanonicalForm(): String {
        // Find the smallest representation among all transformations
        var minRepresentation = queens.map { it.position }.toSet().toString()

        val transforms = listOf<(Pair<Int, Int>) -> Pair<Int, Int>>(
            { it }, // identity
            { (x, y) -> Pair(size - 1 - y, x) }, // rotate 90°
            { (x, y) -> Pair(size - 1 - x, size - 1 - y) }, // rotate 180°
            { (x, y) -> Pair(y, size - 1 - x) }, // rotate 270°
            { (x, y) -> Pair(size - 1 - x, y) }, // horizontal reflection
            { (x, y) -> Pair(x, size - 1 - y) }, // vertical reflection
            { (x, y) -> Pair(y, x) }, // main diagonal
            { (x, y) -> Pair(size - 1 - y, size - 1 - x) } // anti-diagonal
        )

        for (transform in transforms) {
            val transformed = queens.map { transform(it.position) }.toSet().toString()
            if (transformed < minRepresentation) {
                minRepresentation = transformed
            }
        }

        return minRepresentation
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Chessboard) return false
        if (size != other.size) return false

        return this.getCanonicalForm() == other.getCanonicalForm()
    }

    override fun hashCode(): Int {
        return getCanonicalForm().hashCode()
    }
}