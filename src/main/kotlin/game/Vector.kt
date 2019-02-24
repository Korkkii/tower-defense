package game

import java.lang.Math.pow
import kotlin.math.sqrt

data class Vector(val x: Double, val y: Double) {
    operator fun plus(other: Vector): Vector {
        return Vector(this.x + other.x, this.y + other.y)
    }

    operator fun minus(other: Vector): Vector {
        return Vector(this.x - other.x, this.y - other.y)
    }

    operator fun minus(scalar: Double): Vector {
        return Vector(this.x - scalar, this.y - scalar)
    }

    operator fun times(scalar: Double): Vector {
        return Vector(scalar * this.x, scalar * this.y)
    }

    operator fun div(scalar: Double): Vector {
        return (1 / scalar) * this
    }

    operator fun unaryMinus(): Vector = Vector(-this.x, -this.y)

    private fun length(): Double = sqrt(pow(this.x, 2.0) + pow(this.y, 2.0))

    fun unitVector(): Vector = this / this.length()
}

operator fun Double.times(vector: Vector) = vector * this