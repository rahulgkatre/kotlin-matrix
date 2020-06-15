import kotlin.math.abs

data class Fraction(val numerator: Complex, val denominator: Complex) {
    constructor(numerator: Int, denominator: Int): this(Complex(numerator), Complex(denominator))
    constructor(numerator: Int): this(Complex(numerator), Complex.ONE)

    companion object {
        val ZERO = Fraction(Complex.ZERO, Complex.ONE)
        val ONE = Fraction(Complex.ONE, Complex.ONE)

        fun gcd(n1: Int, n2: Int): Int {
            if (n2 == 0) {
                return n1
            } else {
                return gcd(n2, n1 % n2)
            }
        }
    }

    fun simplify(): Fraction {
        var f = if (denominator.imaginary != 0) { Fraction(numerator * denominator.conjugate(), denominator * denominator.conjugate()) } else { this }
        val gcd = gcd(gcd(abs(f.numerator.real), abs(f.numerator.imaginary)), abs(f.denominator.real))
        f = Fraction(Complex(f.numerator.real / gcd, f.numerator.imaginary / gcd), Complex(f.denominator.real / gcd))

        if (f.denominator.real < 0) {
            f = Fraction(-f.numerator, -f.denominator)
        }

        if (numerator == Complex.ZERO) {
            f = Fraction(Complex.ZERO, Complex.ONE)
        } else if (numerator == denominator) {
            f = Fraction(Complex.ONE, Complex.ONE)
        }

        return f
    }

    fun invert(): Fraction {
        return Fraction(denominator, numerator)
    }

    fun magnitude(): Double {
        return numerator.magnitude() / denominator.magnitude()
    }

    operator fun unaryMinus(): Fraction {
        return Fraction(-numerator, denominator).simplify()
    }

    operator fun plus(f: Fraction): Fraction {
        val n = f.numerator * denominator + numerator * f.denominator
        val d = denominator * f.denominator
        return Fraction(n, d).simplify()
    }

    operator fun minus(f: Fraction): Fraction {
        val n = f.numerator * denominator - numerator * f.denominator
        val d = denominator * f.denominator
        return Fraction(n, d).simplify()
    }

    operator fun times(f: Fraction): Fraction {
        return Fraction(numerator * f.numerator,  denominator * f.denominator).simplify()
    }

    operator fun div(f: Fraction): Fraction {
        return Fraction(numerator * f.denominator,  denominator * f.numerator).simplify()
    }

    operator fun compareTo(f: Fraction): Int {
        return (this.magnitude() - f.magnitude()).toInt()
    }

    override fun toString(): String {
        if (denominator == Complex.ONE) {
            return " %s ".format(numerator)
        } else {
            return " %3s / %-3s ".format(numerator, denominator)
        }
    }
}