interface Numeric {
    fun magnitude(): Double

    operator fun unaryMinus(): Numeric
    operator fun plus(n: Numeric): Numeric
    operator fun minus(n: Numeric): Numeric
    operator fun times(n: Numeric): Numeric
    operator fun div(n: Numeric): Numeric

    operator fun compareTo(n: Numeric): Int

    fun toComplex(): Complex
    fun toFraction(): Fraction
}

