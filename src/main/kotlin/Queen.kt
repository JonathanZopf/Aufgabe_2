package org.hszg

import kotlin.math.abs

class Queen {
    val position: Pair<Int, Int>

    constructor(x: Int, y: Int) {
        position = Pair(x, y)
    }

    fun satisfiesConstraintsWithOther(other: Queen) : Boolean {
        return !intersectsHorizontalVertical(other) && !intersectsDiagonal(other)
    }

    private fun intersectsHorizontalVertical(other: Queen) : Boolean {
        return position.first == other.position.first || position.second == other.position.second
    }

    private fun intersectsDiagonal(other: Queen) : Boolean {
        return abs(position.first - other.position.first) == abs(position.second - other.position.second)
    }
}