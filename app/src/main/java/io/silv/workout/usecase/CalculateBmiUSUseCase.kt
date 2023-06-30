package io.silv.workout.usecase

import kotlin.math.pow

class CalculateBmiUSUseCase {

    operator fun invoke(heightFt: Double, heightIn: Double, weight: Double): Double {
            val heightFt: Double = heightFt * 12
            val heightIn: Double = heightIn
            val weight: Double = weight * 703
            return weight / ((heightFt + heightIn).pow(2.0))
    }
}