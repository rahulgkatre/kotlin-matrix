package matrix

import kotlin.math.sqrt

data class Complex(val real: Double, val imaginary: Double) {
    private var isDecimal: Boolean = true

    constructor(real: Double): this(real, 0.0)

    constructor(real: Int, imaginary: Int): this(real.toDouble(), imaginary.toDouble()) {
        isDecimal = false
    }

    constructor(real: Int): this(real, 0) {
        isDecimal = false
    }

    companion object {
        val ZERO = Complex(0, 0)
        val ONE = Complex(1, 0)
    }

    fun isDecimal(): Boolean {
        return isDecimal
    }

    fun square(): Double {
        return real * real + imaginary * imaginary
    }

    fun magnitude(): Double {
        return sqrt(square())
    }

    fun conjugate(): Complex {
        return Complex(real, -imaginary)
    }

    operator fun unaryMinus(): Complex {
        return if (isDecimal) {
            Complex(-real, -imaginary)
        } else {
            Complex(-real.toInt(), -imaginary.toInt())
        }
    }

    operator fun plus(f: Fraction): Fraction {
        return Fraction(this * f.denominator + f.numerator, f.denominator)
    }

    operator fun plus(x: Int): Complex {
        return if (isDecimal) {
            Complex(real + x, imaginary)
        } else {
            Complex(real.toInt() + x, imaginary.toInt())
        }
    }

    operator fun plus(z: Complex): Complex {
        val r = real + z.real
        val i = imaginary + z.imaginary
        if (isDecimal || z.isDecimal) {
            return Complex(r, i)
        } else {
            return Complex(r.toInt(), i.toInt())
        }
    }

    operator fun minus(f: Fraction): Fraction {
        return this + (-f)
    }

    operator fun minus(x: Int): Complex {
        return this + (-x)
    }

    operator fun minus(z: Complex): Complex {
        return this + (-z)
    }

    operator fun times(f: Fraction): Fraction {
        return Fraction(this * f.numerator, f.denominator)
    }

    operator fun times(x: Int): Complex {
        val r = real * x
        val i = imaginary * x
        return if (isDecimal) {
            Complex(r, i)
        } else {
            Complex(r.toInt(), i.toInt())
        }
    }

    operator fun times(z: Complex): Complex {
        val r = real * z.real - imaginary * z.imaginary
        val i = real * z.imaginary + imaginary * z.real
        return if (isDecimal || z.isDecimal) {
            Complex(r, i)
        } else {
            Complex(r.toInt(), i.toInt())
        }
    }

    operator fun div(f: Fraction): Fraction {
        return Fraction(this * f.denominator, f.numerator)
    }

    operator fun div(x: Int): Fraction {
        return Fraction(this, Complex(x, 0))
    }

    operator fun div(z: Complex): Fraction {
        val conjugate = z.conjugate()
        return Fraction((this * conjugate), (z * conjugate))
    }

    operator fun compareTo(z: Complex): Int {
        return (this.magnitude() - z.magnitude()).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Complex

        if (real != other.real) return false
        if (imaginary != other.imaginary) return false

        return true
    }

    override fun hashCode(): Int {
        var result = real.hashCode()
        result = 31 * result + imaginary.hashCode()
        return result
    }

    override fun toString(): String {
        if (isDecimal) {
            val r = (if (real < 0) "%f" else " %f").format(real)
            val i = if (imaginary == 0.0) "" else if (imaginary < 0) " - %fi".format(-imaginary) else " + %fi".format(imaginary)
            return if (real == 0.0 && imaginary != 0.0) return i else return r + i
        } else {
            val r = (if (real < 0) "%d" else " %d").format(real.toInt())
            val i = if (imaginary.toInt() == 0) "" else if (imaginary.toInt() < 0) " - %di".format(-imaginary.toInt()) else " + %di".format(imaginary.toInt())
            return if (real.toInt() == 0 && imaginary.toInt() != 0) return i else return r + i
        }
    }
}