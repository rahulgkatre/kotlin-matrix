package matrix

import kotlin.math.sqrt

data class Complex(val real: Int, val imaginary: Int) {
    constructor(real: Int): this(real, 0)

    companion object {
        val ZERO = Complex(0, 0)
        val ONE = Complex(1, 0)
    }

    fun magnitude(): Double {
        return sqrt(((real * real) + (imaginary * imaginary).toDouble()))
    }

    fun conjugate(): Complex {
        return Complex(real, -imaginary)
    }

    operator fun unaryMinus(): Complex {
        return Complex(-real, -imaginary)
    }

    operator fun plus(f: Fraction): Fraction {
        return Fraction(this * f.denominator + f.numerator, f.denominator)
    }

    operator fun plus(x: Int): Complex {
        return Complex(real + x, imaginary)
    }

    operator fun plus(z: Complex): Complex {
        val r = real + z.real
        val i = imaginary + z.imaginary
        return Complex(r, i)
    }

    operator fun minus(f: Fraction): Fraction {
        return Fraction(this * f.denominator - f.numerator, f.denominator)
    }

    operator fun minus(x: Int): Complex {
        return Complex(real - x, imaginary)
    }

    operator fun minus(z: Complex): Complex {
        val r = real - z.real
        val i = imaginary - z.imaginary
        return Complex(r, i)
    }

    operator fun times(f: Fraction): Fraction {
        return Fraction(this * f.numerator, f.denominator)
    }

    operator fun times(x: Int): Complex {
        val r = real * x
        val i = imaginary * x
        return Complex(r, i)
    }

    operator fun times(z: Complex): Complex {
        val r = real * z.real - imaginary * z.imaginary
        val i = real * z.imaginary + imaginary * z.real
        return Complex(r, i)
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
        val r = "$real"
        val i: String = if (imaginary == 0) "" else "$imaginary" + "i"
        return r + i
    }
}