import kotlin.math.max

class AugmentedMatrix(val left: Matrix, val right: Matrix) {
    val matrix: Matrix

    init {
        assert(left.rows == right.rows)
        this.matrix = Matrix(Array(left.rows) { i -> Array(left.columns + right.columns) { j -> if (j < left.columns) left.entries[i][j] else right.entries[i][j - left.columns] } } )
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
        val columnWidths = Array(matrix.columns) { 0 }
        for (j in 0 until matrix.columns) {
            var maxLength = 0
            for (i in 0 until matrix.rows) {
                maxLength = max(matrix.entries[i][j].toString().length, maxLength)
            }

            columnWidths[j] = maxLength
        }

        for (i in matrix.entries.indices) {
            output += "\n[  "
            for (j in left.entries[i].indices) {
                val remaining = columnWidths[j]
                val string = left.entries[i][j].toString()
                output += string
                for (k in 0 until remaining - string.length + 2) {
                    output += " "
                }
            }

            output += "|  "
            for (j in right.entries[i].indices) {
                val remaining = columnWidths[j + left.columns]
                val string = right.entries[i][j].toString()
                output += string
                for (k in 0 until remaining - string.length + 2) {
                    output += " "
                }
            }

            output += "]"
        }

        return output + "\n"
    }
}