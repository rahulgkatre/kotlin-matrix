import kotlin.math.sqrt

data class Complex(val real: Numeric, val imaginary: Numeric): Numeric {
    override fun magnitude(): Double {
        return sqrt((real * real).magnitude() + (imaginary * imaginary).magnitude())
    }

    fun conjugate(): Complex {
        return Complex(real, -imaginary)
    }

    override operator fun unaryMinus(): Numeric {
        return Complex(-real, -imaginary)
    }

    override operator fun plus(n: Numeric): Numeric {
        val z = n.toComplex()
        return Complex(z.real + real, z.imaginary + imaginary)
    }

    operator fun plus(f: Fraction): Fraction {
        val r = real * f.denominator + f.numerator
        val i = imaginary * f.denominator + f.numerator
        return Fraction(Complex(r, i), f.denominator)
    }

    override operator fun minus(n: Numeric): Numeric {
        return this + -n
    }

    override operator fun times(n: Numeric): Numeric {
        val z = n.toComplex()
        val r = real * z.real - imaginary * z.imaginary
        val i = real * z.imaginary + imaginary * z.real
        return if (i == Integer.ZERO) { r } else { Complex(r, i) }
    }

    operator fun times(f: Fraction): Fraction {
        val r = real * f.numerator
        val i = imaginary * f.numerator
        return Fraction(Complex(r, i), f.denominator)
    }

    override operator fun div(n: Numeric): Numeric {
        val z = n.toComplex()
        val conjugate = z.conjugate()
        return Fraction((this * conjugate), (z * conjugate))
    }

    override operator fun compareTo(n: Numeric): Int {
        return (this.magnitude() - n.magnitude()).toInt()
    }

    override fun toComplex(): Complex {
        return this
    }

    override fun toFraction(): Fraction {
        return Fraction(this, Integer.ONE)
    }

    override fun toString(): String {
        var string = "%.3e".format(real)
        if (real >= Integer.ZERO) {
            string = " $string"
        }

        return if (imaginary == Integer.ZERO) {
            string
        } else {
            "$string + %.3e i".format(imaginary)
        }
    }
}