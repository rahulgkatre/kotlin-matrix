package matrix

/**
 * Functions for matrices
 */
fun identity(size: Int): Matrix {
    return Matrix(Array(size) { i-> Array(size) { j -> if (i == j) Fraction.ONE else Fraction.ZERO } } )
}

fun identity(m: Matrix): Matrix {
    return Matrix(Array(m.rows) { i-> Array(m.columns) { j -> if (i == j) Fraction.ONE else Fraction.ZERO } } )
}

fun augment(left: Matrix, right: Matrix): AugmentedMatrix {
    return AugmentedMatrix(left, right)
}

fun transpose(m: Matrix): Matrix {
    return Matrix(Array(m.columns) { i -> Array(m.rows) { j -> m.get(j, i) } } )
}

fun ref(m: Matrix): Matrix {
    val copy: Array<Array<Fraction>> = m.entries()
    var currentColumnIndex = 0
    var currentPivotRowIndex = 0
    while (currentColumnIndex < m.rows) {
        val currentColumn: Array<Fraction> = m.column(currentColumnIndex).elements
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
            }

            val pivotRow = Vector(copy[currentPivotRowIndex])
            val pivot = pivotRow.elements[currentColumnIndex]
            val pivotScalar = pivot.invert()
            if (pivotScalar != Fraction.ONE) {
                copy[currentPivotRowIndex] = (pivotRow * pivotScalar).elements
            }

            for (i in currentPivotRowIndex + 1 until m.rows) {
                if (i != currentPivotRowIndex) {
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
        currentPivotRowIndex++
    }

    return Matrix(copy)
}

fun rref(m: Matrix): Matrix {
    val copy = m.entries()
    var currentColumnIndex = 0
    var currentPivotRowIndex = 0
    while (currentColumnIndex < m.rows) {
        val currentColumn: Array<Fraction> = m.column(currentColumnIndex).elements
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
            }

            val pivotRow = Vector(copy[currentPivotRowIndex])
            val pivot = pivotRow.elements[currentColumnIndex]
            val pivotScalar = pivot.invert()
            if (pivotScalar != Fraction.ONE) {
                copy[currentPivotRowIndex] = (pivotRow * pivotScalar).elements
            }

            for (i in 0 until m.rows) {
                if (i != currentPivotRowIndex) {
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
        throw(Exception("Matrix is singular (inverse does not exist)"))
    }

    if (left == identity(m)) {
        return right
    } else {
        throw(Exception("\n$augmented\nMatrix is singular (inverse does not exist)"))
    }
}

/**
 * Functions for augmented matrices
 */
fun ref(am: AugmentedMatrix): AugmentedMatrix {
    val ref = ref(am.matrix)
    val l = Matrix(Array(am.left.rows) { i -> Array(am.left.columns) { j -> ref.get(i, j) } } )
    val r = Matrix(Array(am.right.rows) { i -> Array(am.right.columns) { j -> ref.get(i, j + am.left.columns) } } )
    return AugmentedMatrix(l, r)
}

fun rref(am: AugmentedMatrix): AugmentedMatrix {
    val rref = rref(am.matrix)
    val l = Matrix(Array(am.left.rows) { i -> Array(am.left.columns) { j -> rref.get(i, j) } } )
    val r = Matrix(Array(am.right.rows) { i -> Array(am.right.columns) { j -> rref.get(i, j + am.left.columns) } } )
    return AugmentedMatrix(l, r)
}