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

    fun fieldsWithNoCollision() : List<Pair<Int, Int>> {
        val fields = mutableListOf<Pair<Int, Int>>()
        for (x in 0 until size) {
            for (y in 0 until size) {
                val hasQueen = queens.none { it.position == Pair(x, y) }
                if (hasQueen) break
                val hasCollision = queens.any { it.satisfiesConstraintsWithOther(Queen(x, y)) }
                if (hasCollision) break
                fields.add(Pair(x, y))
            }
        }
        return fields
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Chessboard) return false
        if (size != other.size) return false

        val thisNormalized = normalizeQueenPositions()
        val otherRotations = other.allRotatedPositions()

        if (otherRotations.any { rotation -> thisNormalized == rotation }) {
            return true
        }

        val otherSymmetries = other.allSymmetries()
        return otherSymmetries.any { symmetry -> thisNormalized == symmetry }
    }

    private fun normalizeQueenPositions(): List<Pair<Int, Int>> {
        return queens.map { it.position }.sortedBy { it.first }
    }

    private fun allRotatedPositions(): List<List<Pair<Int, Int>>> {
        val rotations = mutableListOf<List<Pair<Int, Int>>>()
        for (i in 0 until 4) {
            val rotated = queens.map { it.position }.map { rotate(it, i) }
            rotations.add(rotated.sortedBy { it.first })
        }
        return rotations
    }

    private fun rotate(position: Pair<Int, Int>, rotation: Int): Pair<Int, Int> {
        return when (rotation) {
            0 -> position // No rotation
            1 -> Pair(size - 1 - position.second, position.first) // 90 degrees
            2 -> Pair(size - 1 - position.first, size - 1 - position.second) // 180 degrees
            3 -> Pair(position.second, size - 1 - position.first) // 270 degrees
            else -> throw IllegalArgumentException("Invalid rotation")
        }
    }

    private fun allSymmetries(): List<List<Pair<Int, Int>>> {
        val symmetries = mutableListOf<List<Pair<Int, Int>>>()
        val original = queens.map { it.position }

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
            symmetries.add(original.map(transform).sortedBy { it.first })
        }

        return symmetries
    }
}