package org.hszg

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class QueenTests {

    @Test
    fun `test same row - constraint violated`() {
        val q1 = Queen(0, 0)
        val q2 = Queen(0, 5)
        assertFalse(q1.satisfiesConstraintsWithOther(q2), "Queens in same row should violate constraint")
    }

    @Test
    fun `test same column - constraint violated`() {
        val q1 = Queen(3, 2)
        val q2 = Queen(6, 2)
        assertFalse(q1.satisfiesConstraintsWithOther(q2), "Queens in same column should violate constraint")
    }

    @Test
    fun `test same major diagonal - constraint violated`() {
        val q1 = Queen(1, 1)
        val q2 = Queen(3, 3)
        assertFalse(q1.satisfiesConstraintsWithOther(q2), "Queens on same major diagonal should violate constraint")
    }

    @Test
    fun `test same minor diagonal - constraint violated`() {
        val q1 = Queen(2, 5)
        val q2 = Queen(5, 2)
        assertFalse(q1.satisfiesConstraintsWithOther(q2), "Queens on same minor diagonal should violate constraint")
    }

    @Test
    fun `test valid different row column and diagonal`() {
        val q1 = Queen(0, 0)
        val q2 = Queen(1, 2)
        assertTrue(q1.satisfiesConstraintsWithOther(q2), "Queens not attacking each other should satisfy constraints")
    }

    @Test
    fun `test far apart non-threatening`() {
        val q1 = Queen(0, 1)
        val q2 = Queen(5, 3)
        assertTrue(q1.satisfiesConstraintsWithOther(q2), "Non-threatening queens should pass")
    }

    @Test
    fun `test edge of board non-threatening`() {
        val q1 = Queen(0, 7)
        val q2 = Queen(7, 0)
        assertTrue(q1.satisfiesConstraintsWithOther(q2), "Edge positions not attacking should be valid")
    }

    @Test
    fun `test multiple threat types - row and diagonal`() {
        val q1 = Queen(4, 4)
        val q2 = Queen(4, 6) // same row
        val q3 = Queen(6, 6) // same diagonal
        assertFalse(q1.satisfiesConstraintsWithOther(q2))
        assertFalse(q1.satisfiesConstraintsWithOther(q3))
    }
}
