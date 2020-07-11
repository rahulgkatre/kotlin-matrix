package matrix

import kotlin.math.max

data class AugmentedMatrix(val left: Matrix, val right: Matrix) {
    val matrix: Matrix

    init {
        assert(left.rows == right.rows)
        this.matrix = Matrix(Array(left.rows) { i -> Array(left.columns + right.columns) { j -> if (j < left.columns) left.get(i, j) else right.get(i, j - left.columns) } })
    }

    override fun toString(): String {
        var output = ""
        val columnWidths = Array(matrix.columns) { 0 }
        for (j in 0 until matrix.columns) {
            var maxLength = 0
            for (i in 0 until matrix.rows) {
                maxLength = max(matrix.get(i, j).toString().length, maxLength)
            }

            columnWidths[j] = maxLength
        }

        for (i in 0 until matrix.rows) {
            output += "[ "
            for (j in 0 until left.columns) {
                val remaining = columnWidths[j]
                val string = left.get(i, j).toString()
                output += string
                for (k in 0 until remaining - string.length + 2) {
                    output += " "
                }
            }

            output += "| "
            for (j in 0 until right.columns) {
                val remaining = columnWidths[j + left.columns]
                val string = right.get(i, j).toString()
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

fun ref(am: AugmentedMatrix): AugmentedMatrix {
    val ref = ref(am.matrix)
    val l = Matrix(Array(am.left.rows) { i -> Array(am.left.columns) { j -> ref.get(i, j) } })
    val r = Matrix(Array(am.right.rows) { i -> Array(am.right.columns) { j -> ref.get(i, j + am.left.columns) } })
    return AugmentedMatrix(l, r)
}

fun rref(am: AugmentedMatrix): AugmentedMatrix {
    val rref = rref(am.matrix)
    val l = Matrix(Array(am.left.rows) { i -> Array(am.left.columns) { j -> rref.get(i, j) } })
    val r = Matrix(Array(am.right.rows) { i -> Array(am.right.columns) { j -> rref.get(i, j + am.left.columns) } })
    return AugmentedMatrix(l, r)
}