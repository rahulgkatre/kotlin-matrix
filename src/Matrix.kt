data class Matrix(val entries: Array<Array<Complex>>) {
    val steps = true

    val rows: Int = entries.size
    val columns: Int = entries[0].size

    fun row(i: Int): Vector {
        return Vector(entries[i])
    }

    fun column(j: Int): Vector {
        return Vector(Array(rows) { i -> entries[i][j] } )
    }

    fun augment(m: Matrix): Matrix {
        assert(rows == m.rows)
        val left = entries
        val right = m.entries
        val join = Array(rows) { i -> Array(columns + m.columns) {Complex(0.0)} }

        for (i in left.indices) {
            for (j in left[i].indices) {
                join[i][j] = left[i][j]
            }
        }

        for (i in right.indices) {
            for (j in right[i].indices) {
                join[i][j + columns] = right[i][j]
            }
        }

        return Matrix(join)
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
        return plus(-m)
    }

    operator fun times(z: Complex): Matrix {
        return Matrix(Array(rows) { i -> Array(columns) { j -> entries[i][j] * z } } )
    }

    operator fun times(m: Matrix): Matrix {
        assert(columns == m.rows)
        return Matrix(Array(rows) { i -> Array(m.columns) { j -> row(i).dot(m.column(j)) } } )
    }

    fun ref(): Matrix {
        if (rows > 0 && columns > 0) {
            val copy: Array<Array<Complex>> = entries.copyOf()
            var currentColumnIndex = 0
            var previousPivotIndex = 0
            while (currentColumnIndex < columns) {
                val currentColumn: Array<Complex> = column(currentColumnIndex).elements
                var pivotRowIndex: Int = -1
                for (i in previousPivotIndex until rows) {
                    if (currentColumn[i].real != 0.0 || currentColumn[i].imaginary != 0.0) {
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

                    for (i in previousPivotIndex + 1 until rows) {
                        val currentRow = Vector(copy[i])
                        val leadingEntry = currentRow.elements[currentColumnIndex]
                        val scalar = leadingEntry / pivot
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
                val currentColumn: Array<Complex> = column(currentColumnIndex).elements
                var pivotRowIndex: Int = -1
                for (i in previousPivotIndex until rows) {
                    if (currentColumn[i].real != 0.0 || currentColumn[i].imaginary != 0.0) {
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
                    val pivotScalar = Complex(1.0) / pivot
                    copy[previousPivotIndex] = (pivotRow * pivotScalar).elements

                    for (i in 0 until rows) {
                        if (i != previousPivotIndex) {
                            val currentRow = Vector(copy[i])
                            val leadingEntry = currentRow.elements[currentColumnIndex]
                            val scalar = leadingEntry / pivot
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