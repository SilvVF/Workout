package io.silv.workout.usecase

import kotlin.math.pow

class CalculateBmiUseCase {

    operator fun invoke(heightCm: Double, weightMetric: Double): Double {
        //bmi = kg / height in meters ** 2
        val height: Double = heightCm * 0.01
        val weight: Double = weightMetric
        return weight / (height.pow(2.0))
    }
}