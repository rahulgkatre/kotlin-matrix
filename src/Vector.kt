import kotlin.math.sqrt

data class Vector(val elements: Array<Complex>) {
    val dimension = elements.size

    fun magnitude(): Complex {
        var sum = 0.0
        for (element in elements) {
            sum += (element * element).real
        }

        return Complex(sqrt(sum))
    }

    fun normalize(): Vector {
        return this / magnitude()
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

    operator fun times(z: Complex): Vector {
        return Vector(Array(dimension) { i -> elements[i] * z } )
    }

    operator fun div(z: Complex): Vector {
        return Vector(Array(dimension) { i -> elements[i] / z } )
    }

    fun dot(v: Vector): Complex {
        assert(dimension == v.dimension)
        var sum = Complex(0.0, 0.0)
        for (i in elements.indices) {
            sum += elements[i] * v.elements[i]
        }

        return sum
    }

    override fun toString(): String {
        var output = "[ "
        for (element in elements) {
            output += "$element  "
        }

        return "$output ]"
    }
}