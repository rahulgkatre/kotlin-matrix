data class Matrix(val entries: Array<Array<Fraction>>) {
    val rows: Int = entries.size
    val columns: Int = entries[0].size

    /*
    constructor(space: Array<Vector>): Matrix {
        for (v in space) {
            assert(v.dimension == space[0].dimension)
        }

        return Matrix((Array(space[0].dimension) { j -> Array(space.size) { i -> space[i].elements[j] } } ) )
    }

     */

    companion object {
        fun identity(size: Int): Matrix {
            return Matrix(Array(size) { i-> Array(size) { j -> if (i == j) Fraction.ONE else Fraction.ZERO } } )
        }
    }

    fun augment(m: Matrix): AugmentedMatrix {
        return AugmentedMatrix(this, m)
    }

    /*
    fun augment(v: Vector): AugmentedMatrix {
        return AugmentedMatrix(this, )
    }

     */

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
            var currentPivotRowIndex = 0
            while (currentColumnIndex < rows) {
                val currentColumn: Array<Fraction> = column(currentColumnIndex).elements
                var pivotRowIndex: Int = rows - 1
                var maxColumnValue: Fraction = currentColumn[0]
                for (i in currentPivotRowIndex until rows) {
                    if (currentColumn[i].magnitude() > maxColumnValue.magnitude()) {
                        pivotRowIndex = i
                        maxColumnValue = currentColumn[i]
                    }

                    if (currentColumn[i].magnitude() != 0.0) {
                        pivotRowIndex = i
                        break
                    }
                }

                if (currentPivotRowIndex != pivotRowIndex) {
                    val swap = copy[currentPivotRowIndex]
                    copy[currentPivotRowIndex] = copy[pivotRowIndex]
                    copy[pivotRowIndex] = swap
                }

                val pivotRow = Vector(copy[currentPivotRowIndex])
                val pivot = pivotRow.elements[currentColumnIndex]
                val pivotScalar = pivot.invert()
                copy[currentPivotRowIndex] = (pivotRow * pivotScalar).elements

                for (i in currentPivotRowIndex + 1 until rows) {
                    if (i != currentPivotRowIndex) {
                        val currentRow = Vector(copy[i])
                        val leadingEntry = currentRow.elements[currentColumnIndex]
                        val scalar = leadingEntry * pivotScalar
                        val scaledPivotRow = pivotRow * scalar
                        val reducedRow = currentRow - scaledPivotRow
                        copy[i] = reducedRow.elements
                    }
                }

                currentColumnIndex++
                currentPivotRowIndex++
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
            var currentPivotRowIndex = 0
            while (currentColumnIndex < rows) {
                val currentColumn: Array<Fraction> = column(currentColumnIndex).elements
                var pivotRowIndex: Int = rows - 1
                var maxColumnValue: Fraction = currentColumn[0]
                for (i in currentPivotRowIndex until rows) {
                    if (currentColumn[i].magnitude() > maxColumnValue.magnitude()) {
                        pivotRowIndex = i
                        maxColumnValue = currentColumn[i]
                    }

                    if (currentColumn[i].magnitude() != 0.0) {
                        pivotRowIndex = i
                        break
                    }
                }

                if (currentPivotRowIndex != pivotRowIndex) {
                    val swap = copy[currentPivotRowIndex]
                    copy[currentPivotRowIndex] = copy[pivotRowIndex]
                    copy[pivotRowIndex] = swap
                }

                val pivotRow = Vector(copy[currentPivotRowIndex])
                val pivot = pivotRow.elements[currentColumnIndex]
                val pivotScalar = pivot.invert()
                copy[currentPivotRowIndex] = (pivotRow * pivotScalar).elements

                for (i in 0 until rows) {
                    if (i != currentPivotRowIndex) {
                        val currentRow = Vector(copy[i])
                        val leadingEntry = currentRow.elements[currentColumnIndex]
                        val scalar = leadingEntry * pivotScalar
                        val scaledPivotRow = pivotRow * scalar
                        val reducedRow = currentRow - scaledPivotRow
                        copy[i] = reducedRow.elements
                    }
                }

                currentColumnIndex++
                currentPivotRowIndex++
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