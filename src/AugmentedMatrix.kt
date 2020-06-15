class AugmentedMatrix private constructor(val left: Matrix, val right: Matrix) {
    val matrix = Matrix(Array(left.rows) { i -> Array(left.columns + right.columns) { j -> if (j < left.columns) left.entries[i][j] else right.entries[i][j - left.columns] } } )

    companion object {
        fun augment(left: Matrix, right: Matrix): AugmentedMatrix {
            assert(left.rows == right.rows)
            return AugmentedMatrix(left, right)
        }
    }

    fun ref(): AugmentedMatrix {
        val ref = matrix.ref()
        val l = Matrix(Array(left.rows) { i -> Array(left.columns) { j -> ref.entries[i][j] } } )
        val r = Matrix(Array(right.rows) { i -> Array(right.columns) { j -> ref.entries[i][j + left.columns] } } )
        return AugmentedMatrix(l, r)
    }

    fun rref(): AugmentedMatrix {
        val rref = matrix.rref()
        val l = Matrix(Array(left.rows) { i -> Array(left.columns) { j -> rref.entries[i][j] } } )
        val r = Matrix(Array(right.rows) { i -> Array(right.columns) { j -> rref.entries[i][j + left.columns] } } )
        return AugmentedMatrix(l, r)
    }

    override fun toString(): String {
        var output = ""
        for (i in matrix.entries.indices) {
            output += "\n[ "
            for (j in left.entries[i].indices) {
                output += left.entries[i][j].toString() + " "
            }

            output += "| "
            for (j in right.entries[i].indices) {
                output += right.entries[i][j].toString() + " "
            }

            output += "]"
        }

        return output
    }
}