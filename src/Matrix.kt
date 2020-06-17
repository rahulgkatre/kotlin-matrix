package matrix

import kotlin.math.max

data class Matrix(private val entries: Array<Array<Fraction>>) {
    val rows: Int = entries.size
    val columns: Int = entries[0].size

    constructor(space: Array<Vector>): this(Array(space[0].dimension) { i -> Array(space.size) { j -> space[j].elements[i] } } ) {
        for (v in space) {
            if (v.dimension != space[0].dimension) {
                throw(Exception("All vectors in a vector space must have the same dimension"))
            }
        }
    }

    fun row(i: Int): Vector {
        return Vector(entries[i])
    }

    fun column(j: Int): Vector {
        return Vector(Array(rows) { i -> entries[i][j] } )
    }

    fun get(i: Int, j: Int): Fraction {
        return entries[i][j]
    }

    fun entries(): Array<Array<Fraction>> {
        return entries.copyOf()
    }

    operator fun unaryMinus(): Matrix {
        return Matrix(Array(rows) { i -> Array(columns) { j -> -entries[i][j] } } )
    }

    operator fun plus(m: Matrix): Matrix {
        assert(rows == m.rows && columns == m.columns)
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] + m.entries[i][j] } } )
    }

    operator fun minus(m: Matrix): Matrix {
        assert(rows == m.rows && columns == m.columns)
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] - m.entries[i][j] } } )
    }

    operator fun times(f: Fraction): Matrix {
        assert(f.denominator != Complex.ZERO)
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] * f } } )
    }

    operator fun times(m: Matrix): Matrix {
        assert(columns == m.rows)
        return Matrix(Array(rows) { i -> Array(m.columns) { j -> row(i) * m.column(j) } } )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix

        if (!entries.contentDeepEquals(other.entries)) return false
        if (rows != other.rows) return false
        if (columns != other.columns) return false

        return true
    }

    override fun hashCode(): Int {
        var result = entries.contentDeepHashCode()
        result = 31 * result + rows
        result = 31 * result + columns
        return result
    }

    override fun toString(): String {
        var output = ""
        val columnWidths = Array(columns) { 0 }
        for (j in 0 until columns) {
            var maxLength = 0
            for (i in 0 until rows) {
                maxLength = max(entries[i][j].toString().length, maxLength)
            }

            columnWidths[j] = maxLength
        }

        for (i in 0 until rows) {
            output += "[ "
            for (j in 0 until columns) {
                val remaining = columnWidths[j]
                val string = entries[i][j].toString()
                output += string
                for (k in 0 until remaining - string.length + 2) {
                    output += " "
                }
            }

            output += "]\n"
        }

        return output
    }
}