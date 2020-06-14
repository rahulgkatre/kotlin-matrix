data class Fraction(val numerator: Numeric, val denominator: Numeric): Numeric {
    companion object {
        fun gcd(n1: Integer, n2: Integer): Integer {
            return if (n2 == Integer.ZERO) {
                n1
            } else {
                gcd(n2, n1 % n2)
            }
        }
    }

    fun simplify(): Numeric {
        /*
        if (numerator == denominator) {
            return Integer.ONE
        } else if (numerator == Integer.ZERO) {
            return Integer.ZERO
        } else if (denominator == Integer.ONE) {
            return numerator
        } else {
            val n = numerator.toFraction().simplify()
            val d = denominator.toFraction().simplify()
            return Fraction(n, d)
        }

         */

        val f: Fraction
        if (numerator is Integer && denominator is Integer) {
            val gcd = gcd(numerator, denominator)
            var n = numerator.x / gcd.x
            var d = denominator.x / gcd.x

            if (d < 0) {
                n = -n
                d = -d
            }

            f = Fraction(Integer(n), Integer(d))
        } else if (numerator is Fraction && denominator is Fraction) {
            f = Fraction(numerator / denominator, Integer.ONE).toFraction()
        } else if (numerator is Integer && denominator is Fraction) {
            f = Fraction(numerator * denominator.denominator, denominator.numerator).toFraction()
        } else {
            f = this
        }

        if (f.numerator == f.denominator) {
            return Integer.ONE
        } else if (f.numerator == Integer.ZERO) {
            return Integer.ZERO
        } else if (f.denominator == Integer.ONE) {
            return f.numerator
        } else {
            return f
        }


        //TODO: Turn real fractions into Integer, turn complex fractions into Complex, turn everything else into
        return this
    }

    fun invert(): Fraction {
        return Fraction(denominator, numerator)
    }

    override fun magnitude(): Double {
        return numerator.magnitude() / denominator.magnitude()
    }

    override fun unaryMinus(): Numeric {
        return Fraction(-numerator, denominator).simplify()
    }

    override fun plus(n: Numeric): Numeric {
        val f = n.toFraction()
        val n = f.numerator * denominator + numerator * f.denominator
        val d = denominator * f.denominator
        return Fraction(n, d).simplify()
    }

    override fun minus(n: Numeric): Numeric {
        val f = n.toFraction()
        return -f + this
    }

    override fun times(n: Numeric): Numeric {
        val f = n.toFraction()
        return Fraction(f.numerator * numerator, f.denominator * denominator).simplify()
    }

    override fun div(n: Numeric): Numeric {
        return this * n.toFraction().invert()
    }

    override operator fun compareTo(n: Numeric): Int {
        return (this.magnitude() - n.magnitude()).toInt()
    }

    override fun toComplex(): Complex {
        // Not preferable as this makes it harder to simplify, so Complex handles fraction operations instead
        // Complex will also convert itself to a proper complex fraction if any of its terms are fractions
        return Complex(this, Integer.ZERO)
    }

    override fun toFraction(): Fraction {
        return this
    }

    override fun toString(): String {
        return if (magnitude() >= 0) {
            "( $numerator / $denominator)"
        } else {
            "($numerator / $denominator)"
        }
    }
}