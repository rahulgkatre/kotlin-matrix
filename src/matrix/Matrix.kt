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
        return Vector(Array(rows) { i -> entries[i][j] })
    }

    fun get(i: Int, j: Int): Fraction {
        return entries[i][j]
    }

    fun entries(): Array<Array<Fraction>> {
        return entries.copyOf()
    }

    operator fun unaryMinus(): Matrix {
        return Matrix(Array(rows) { i -> Array(columns) { j -> -entries[i][j] } })
    }

    operator fun plus(m: Matrix): Matrix {
        assert(rows == m.rows && columns == m.columns)
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] + m.entries[i][j] } })
    }

    operator fun minus(m: Matrix): Matrix {
        assert(rows == m.rows && columns == m.columns)
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] - m.entries[i][j] } })
    }

    operator fun times(f: Fraction): Matrix {
        assert(f.denominator != Complex.ZERO)
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] * f } })
    }

    operator fun times(m: Matrix): Matrix {
        assert(columns == m.rows)
        return Matrix(Array(rows) { i -> Array(m.columns) { j -> row(i) * m.column(j) } })
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

fun identity(size: Int): Matrix {
    return Matrix(Array(size) { i -> Array(size) { j -> if (i == j) Fraction.ONE else Fraction.ZERO } })
}

fun identity(m: Matrix): Matrix {
    return Matrix(Array(m.rows) { i -> Array(m.columns) { j -> if (i == j) Fraction.ONE else Fraction.ZERO } })
}

fun augment(left: Matrix, right: Matrix): AugmentedMatrix {
    return AugmentedMatrix(left, right)
}

fun augment(m: Matrix, v: Vector): AugmentedMatrix {
    return AugmentedMatrix(m, Matrix(arrayOf(v)))
}

fun transpose(m: Matrix): Matrix {
    return Matrix(Array(m.columns) { i -> Array(m.rows) { j -> m.get(j, i) } })
}

fun ref(m: Matrix): Matrix {
    val copy: Array<Array<Fraction>> = m.entries()
    var currentColumnIndex = 0
    var currentPivotRowIndex = 0
    while (currentColumnIndex < m.rows) {
        val currentColumn: Array<Fraction> = Matrix(copy).column(currentColumnIndex).elements
        var pivotRowIndex: Int = currentPivotRowIndex
        var largestRelativeMax = 0.0
        for (i in currentPivotRowIndex until m.rows) {
            val relativeMax = currentColumn[i].magnitude() / m.row(i).max().magnitude()
            if (relativeMax > largestRelativeMax) {
                pivotRowIndex = i
                largestRelativeMax = relativeMax
            }
        }

        if (largestRelativeMax > 0.0) {
            if (currentPivotRowIndex != pivotRowIndex) {
                val swap = copy[currentPivotRowIndex]
                copy[currentPivotRowIndex] = copy[pivotRowIndex]
                copy[pivotRowIndex] = swap

                // println("R" + (currentPivotRowIndex + 1) + " <-> R" + (pivotRowIndex + 1))
            }

            val pivotRow = Vector(copy[currentPivotRowIndex])
            val pivot = pivotRow.elements[currentColumnIndex]
            val pivotScalar = pivot.invert()
            if (pivotScalar != Fraction.ONE) {
                copy[currentPivotRowIndex] = (pivotRow * pivotScalar).elements

                // println("R" + (currentPivotRowIndex + 1) + " = $pivotScalar * R" + (currentPivotRowIndex + 1))
            }

            for (i in currentPivotRowIndex + 1 until m.rows) {
                if (i != currentPivotRowIndex) {
                    val currentRow = Vector(copy[i])
                    val leadingEntry = currentRow.elements[currentColumnIndex]
                    val scalar = leadingEntry * pivotScalar
                    val scaledPivotRow = pivotRow * scalar
                    val reducedRow = currentRow - scaledPivotRow
                    copy[i] = reducedRow.elements

                    // println("R" + (i + 1) + " = R" + (i + 1) + " + $scalar * R" + (currentPivotRowIndex + 1))
                }
            }
        }

        currentColumnIndex++
        currentPivotRowIndex++
    }

    return Matrix(copy)
}

fun rref(m: Matrix): Matrix {
    val copy = m.entries()
    var currentColumnIndex = 0
    var currentPivotRowIndex = 0
    while (currentColumnIndex < m.rows) {
        val currentColumn: Array<Fraction> = Matrix(copy).column(currentColumnIndex).elements
        var pivotRowIndex: Int = currentPivotRowIndex
        var largestRelativeMax = 0.0
        for (i in currentPivotRowIndex until m.rows) {
            val relativeMax = currentColumn[i].magnitude() / m.row(i).max().magnitude()
            if (relativeMax > largestRelativeMax) {
                pivotRowIndex = i
                largestRelativeMax = relativeMax
            }
        }

        if (largestRelativeMax > 0.0) {
            if (currentPivotRowIndex != pivotRowIndex) {
                val swap = copy[currentPivotRowIndex]
                copy[currentPivotRowIndex] = copy[pivotRowIndex]
                copy[pivotRowIndex] = swap

                // println("R" + (currentPivotRowIndex + 1) + " <-> R" + (pivotRowIndex + 1))
            }

            val pivotRow = Vector(copy[currentPivotRowIndex])
            val pivot = pivotRow.elements[currentColumnIndex]
            val pivotScalar = pivot.invert()
            if (pivotScalar != Fraction.ONE) {
                copy[currentPivotRowIndex] = (pivotRow * pivotScalar).elements

                // println("R" + (currentPivotRowIndex + 1) + " = $pivotScalar * R" + (currentPivotRowIndex + 1))
            }

            for (i in 0 until m.rows) {
                if (i != currentPivotRowIndex) {
                    val currentRow = Vector(copy[i])
                    val leadingEntry = currentRow.elements[currentColumnIndex]
                    val scalar = leadingEntry * pivotScalar
                    val scaledPivotRow = pivotRow * scalar
                    val reducedRow = currentRow - scaledPivotRow
                    copy[i] = reducedRow.elements

                    // println("R" + (i + 1) + " = R" + (i + 1) + " + $scalar * R" + (currentPivotRowIndex + 1))
                }
            }
        }

        currentColumnIndex++
        currentPivotRowIndex++
    }

    return Matrix(copy)
}

fun inverse(m: Matrix): Matrix {
    val augmented: AugmentedMatrix
    val left: Matrix
    val right: Matrix
    try {
        augmented = rref(augment(m, identity(m)))
        left = augmented.left
        right = augmented.right
    } catch(e: Exception) {
        throw(Exception("matrix.Matrix is singular (matrix.inverse does not exist)"))
    }

    if (left == identity(m)) {
        return right
    } else {
        throw(Exception("\n$augmented\nmatrix.Matrix is singular (matrix.inverse does not exist)"))
    }
}

fun lup(m: Matrix): Array<Matrix> {
    assert(m.rows == m.columns)
    val permutation = identity(m).entries()
    val lower = identity(m).entries()
    val upper = m.entries()
    var currentColumnIndex = 0
    var currentPivotRowIndex = 0
    while (currentColumnIndex < m.columns) {
        val currentColumn: Array<Fraction> = Matrix(upper).column(currentColumnIndex).elements
        var pivotRowIndex: Int = currentPivotRowIndex
        var largestRelativeMax = 0.0
        for (i in currentPivotRowIndex until m.rows) {
            val relativeMax = currentColumn[i].magnitude() / m.row(i).max().magnitude()
            if (relativeMax > largestRelativeMax) {
                pivotRowIndex = i
                largestRelativeMax = relativeMax
            }
        }

        if (largestRelativeMax > 0.0) {
            if (currentPivotRowIndex != pivotRowIndex) {
                val swap = upper[currentPivotRowIndex]
                upper[currentPivotRowIndex] = upper[pivotRowIndex]
                upper[pivotRowIndex] = swap

                // println("R" + (currentPivotRowIndex + 1) + " <-> R" + (pivotRowIndex + 1))
            }

            val pivotRow = Vector(upper[currentPivotRowIndex])
            val pivot = pivotRow.elements[currentColumnIndex]
            val pivotScalar = pivot.invert()
            for (i in currentPivotRowIndex + 1 until m.rows) {
                val currentRow = Vector(upper[i])
                val leadingEntry = currentRow.elements[currentColumnIndex]
                val scalar = leadingEntry * pivotScalar
                val scaledPivotRow = pivotRow * scalar
                val reducedRow = currentRow - scaledPivotRow
                upper[i] = reducedRow.elements
                lower[i][currentColumnIndex] = scalar

                // println("R" + (i + 1) + " = R" + (i + 1) + " + $scalar * R" + (currentPivotRowIndex + 1))
            }

            if (currentPivotRowIndex != pivotRowIndex) {
                val swap = permutation[currentPivotRowIndex]
                permutation[currentPivotRowIndex] = permutation[pivotRowIndex]
                permutation[pivotRowIndex] = swap
            }
        }

        currentColumnIndex++
        currentPivotRowIndex++
    }

    return arrayOf(Matrix(lower), Matrix(upper), Matrix(permutation))
}

fun det(m: Matrix): Fraction {
    assert(m.rows == m.columns)
    val lu = lup(m)
    val lower = lu[0].entries()
    val upper = lu[1].entries()
    var determinant = Fraction.ONE
    for (i in 0 until m.rows) {
        determinant *= lower[i][i] * upper[i][i]
    }

    return determinant
}
