data class Complex(val real: Double, val imaginary: Double) {
    constructor(real: Double): this(real, 0.0)

    fun conjugate(): Complex {
        return Complex(real, -imaginary)
    }

    operator fun unaryMinus(): Complex {
        return Complex(-real, -imaginary)
    }

    operator fun plus(z: Complex): Complex {
        val a = real + z.real
        val b = imaginary + z.imaginary
        return Complex(a, b)
    }

    operator fun minus(z: Complex): Complex {
        return plus(-z)
    }

    operator fun times(z: Complex): Complex {
        val a = real * z.real - imaginary * z.imaginary;
        val b = real * z.imaginary + imaginary * z.real;
        return Complex(a, b);
    }

    operator fun div(z: Complex): Complex {
        val conjugate = z.conjugate()
        val product = this * conjugate
        val denominator = z * conjugate
        return Complex(product.real / denominator.real, product.imaginary / denominator.real)
    }

    override fun toString(): String {
        var string = "%.3e".format(real)
        if (real >= 0) {
            string = " $string"
        }

        return if (imaginary == 0.0) {
            string
        } else {
            "$string + %.3e i".format(imaginary)
        }
    }
}