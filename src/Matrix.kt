import java.lang.Exception

data class Matrix(val entries: Array<Array<Fraction>>) {
    val rows: Int = entries.size
    val columns: Int = entries[0].size

    companion object {
        fun identity(size: Int): Matrix {
            return Matrix(Array(size) { i-> Array(size) { j -> if (i == j) Fraction.ONE else Fraction.ZERO } } )
        }
    }

    fun augment(m: Matrix): AugmentedMatrix {
        return AugmentedMatrix.augment(this, m)
    }

    fun row(i: Int): Vector {
        return Vector(entries[i])
    }

    fun column(j: Int): Vector {
        return Vector(Array(rows) { i -> entries[i][j] } )
    }

    fun identity(): Matrix {
        return Matrix(Array(rows) { i-> Array(columns) { j -> if (i == j) Fraction.ONE else Fraction.ZERO } } )
    }

    fun transpose(): Matrix {
        return Matrix(Array(columns) { i -> Array(rows) { j -> entries[j][i] } } )
    }

    operator fun unaryMinus(): Matrix {
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] } } )
    }

    operator fun plus(m: Matrix): Matrix {
        assert(rows == m.rows && columns == m.columns)
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] + m.entries[i][j] } } )
    }

    operator fun minus(m: Matrix): Matrix {
        assert(rows == m.rows && columns == m.columns)
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] + m.entries[i][j] } } )
    }

    operator fun times(f: Fraction): Matrix {
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] * f } } )
    }

    operator fun times(m: Matrix): Matrix {
        assert(columns == m.rows)
        return Matrix(Array(rows) { i -> Array(m.columns) { j -> row(i).dot(m.column(j)) } } )
    }

    fun ref(): Matrix {
        if (rows > 0 && columns > 0) {
            val copy: Array<Array<Fraction>> = entries.copyOf()
            var currentColumnIndex = 0
            var previousPivotIndex = 0
            while (currentColumnIndex < columns) {
                val currentColumn: Array<Fraction> = column(currentColumnIndex).elements
                var pivotRowIndex: Int = -1
                for (i in previousPivotIndex until rows) {
                    if (currentColumn[i].magnitude() != 0.0) {
                        pivotRowIndex = i
                        break
                    }
                }

                if (pivotRowIndex == -1) {
                    previousPivotIndex--
                } else {
                    val swap = copy[previousPivotIndex]
                    copy[previousPivotIndex] = copy[pivotRowIndex]
                    copy[pivotRowIndex] = swap

                    val pivotRow = Vector(copy[previousPivotIndex])
                    val pivot = pivotRow.elements[currentColumnIndex]
                    val pivotScalar = pivot.invert()
                    copy[previousPivotIndex] = (pivotRow * pivotScalar).elements

                    for (i in previousPivotIndex + 1 until rows) {
                        val currentRow = Vector(copy[i])
                        val leadingEntry = currentRow.elements[currentColumnIndex]
                        val scalar = leadingEntry * pivotScalar
                        val scaledPivotRow = pivotRow * scalar
                        val reducedRow = currentRow - scaledPivotRow
                        copy[i] = reducedRow.elements
                    }
                }

                currentColumnIndex++
                previousPivotIndex++
            }

            return Matrix(copy)
        } else {
            return this
        }
    }

    fun rref(): Matrix {
        if (rows > 0 && columns > 0) {
            val copy = entries.copyOf()
            var currentColumnIndex = 0
            var previousPivotIndex = 0
            while (currentColumnIndex < columns) {
                val currentColumn: Array<Fraction> = column(currentColumnIndex).elements
                var pivotRowIndex: Int = -1
                for (i in previousPivotIndex until rows) {
                    if (currentColumn[i].magnitude() != 0.0) {
                        pivotRowIndex = i
                        break
                    }
                }

                if (pivotRowIndex == -1) {
                    previousPivotIndex--
                } else {
                    val swap = copy[previousPivotIndex]
                    copy[previousPivotIndex] = copy[pivotRowIndex]
                    copy[pivotRowIndex] = swap

                    val pivotRow = Vector(copy[previousPivotIndex])
                    val pivot = pivotRow.elements[currentColumnIndex]
                    val pivotScalar = pivot.invert()
                    copy[previousPivotIndex] = (pivotRow * pivotScalar).elements

                    for (i in 0 until rows) {
                        if (i != previousPivotIndex) {
                            val currentRow = Vector(copy[i])
                            val leadingEntry = currentRow.elements[currentColumnIndex]
                            val scalar = leadingEntry * pivotScalar
                            val scaledPivotRow = pivotRow * scalar
                            val reducedRow = currentRow - scaledPivotRow
                            copy[i] = reducedRow.elements
                        }
                    }
                }

                currentColumnIndex++
                previousPivotIndex++
            }

            return Matrix(copy)
        } else {
            return this
        }
    }

    fun inverse(): Matrix {
        val augmented = augment(identity()).rref()
        val left = augmented.left
        val right = augmented.right
        if (left == identity()) {
            return right
        } else {
            throw(Exception("$left\nMatrix is singular (inverse does not exist)"))
        }
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
        var output = "\n"
        for (i in entries.indices) {
            output += row(i).toString() + "\n"
        }

        return output
    }
}