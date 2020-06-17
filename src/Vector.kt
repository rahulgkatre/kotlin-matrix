package matrix

import kotlin.math.sqrt

data class Vector(val elements: Array<Fraction>) {
    val dimension = elements.size
    //val ZERO = Vector(Array(dimension) { i -> Fraction.ZERO } )

    fun square(): Fraction {
        var sum = Fraction.ZERO
        for (e in elements) {
            sum += e * e.conjugate()
        }

        return sum
    }

    fun magnitude(): Double {
        return sqrt(square().magnitude())
    }

    operator fun unaryMinus(): Vector {
        return Vector(Array(dimension) { i -> -elements[i] } )
    }

    operator fun plus(v: Vector): Vector {
        return Vector(Array(dimension) { i -> elements[i] + v.elements[i] } )
    }

    operator fun minus(v: Vector): Vector {
        return plus(-v)
    }

    operator fun times(f: Fraction): Vector {
        return Vector(Array(dimension) { i -> elements[i] * f } )
    }

    operator fun div(f: Fraction): Vector {
        return Vector(Array(dimension) { i -> elements[i] / f } )
    }

    fun max(): Fraction {
        return elements.maxBy { f -> f.magnitude() } !!
    }

    operator fun times(v: Vector): Fraction {
        assert(dimension == v.dimension)
        var sum: Fraction = Fraction.ZERO
        for (i in elements.indices) {
            sum += elements[i].conjugate() * v.elements[i]
        }

        return sum
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector

        if (!elements.contentEquals(other.elements)) return false
        if (dimension != other.dimension) return false

        return true
    }

    override fun hashCode(): Int {
        var result = elements.contentHashCode()
        result = 31 * result + dimension
        return result
    }

    override fun toString(): String {
        var output = "[ "
        for (element in elements) {
            output += "$element "
        }

        return "$output]"
    }
}