data class Integer(val x: Int): Numeric {
    companion object {
        val ZERO = Integer(0)
        val ONE = Integer(1)
    }

    override fun magnitude(): Double {
        return x.toDouble()
    }

    override fun unaryMinus(): Numeric {
        return Integer(-1 * x)
    }

    override operator fun plus(n: Numeric): Numeric {
        return if (n is Integer) { Integer(x + n.x) } else { n + this }
    }

    override operator fun minus(n: Numeric): Numeric {
        return -n + this
    }

    override operator fun times(n: Numeric): Numeric {
        return if (n is Integer) { Integer(x * n.x) } else { n * this }
    }

    override operator fun div(n: Numeric): Numeric {
        return Fraction(this, n)
    }

    operator fun rem(i: Integer): Integer {
        return Integer(x - i.x * (x / i.x))
    }

    override fun toComplex(): Complex {
        return Complex(this, ZERO)
    }

    override fun toFraction(): Fraction {
        return Fraction(this, ONE)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Integer

        if (x != other.x) return false

        return true
    }

    override fun hashCode(): Int {
        return x
    }

    override operator fun compareTo(n: Numeric): Int {
        return (this.magnitude() - n.magnitude()).toInt()
    }

    override fun toString(): String {
        return if (x >= 0) {
            " $x"
        } else {
            "$x"
        }
    }
}

