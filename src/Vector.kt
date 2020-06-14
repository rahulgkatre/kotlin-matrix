data class Vector(val elements: Array<Numeric>) {
    val dimension = elements.size

    operator fun unaryMinus(): Vector {
        return Vector(Array(dimension) { i -> -elements[i] } )
    }

    operator fun plus(v: Vector): Vector {
        return Vector(Array(dimension) { i -> elements[i] + v.elements[i] } )
    }

    operator fun minus(v: Vector): Vector {
        return plus(-v)
    }

    operator fun times(n: Numeric): Vector {
        return Vector(Array(dimension) { i -> elements[i] * n } )
    }

    operator fun div(z: Complex): Vector {
        return Vector(Array(dimension) { i -> elements[i] / z } )
    }

    fun dot(v: Vector): Numeric {
        assert(dimension == v.dimension)
        var sum: Numeric = Integer.ZERO
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

        return "$output]"
    }
}