package io.silv.workout.data.types

import io.silv.workout.R


data class Exercise(
    val id: Int,
    val exerciseName: String,
    val image: Int,
    var isSelected: Boolean = false,
    var isCompleted: Boolean = false
) {

    companion object {
        val exerciseList by lazy {
            listOf(
                Exercise(
                    id = 1,
                    exerciseName = "Jumping Jacks",
                    image = R.drawable.jumping_jacks,
                ),
                Exercise(
                    id = 2,
                    exerciseName = "Squats",
                    image = R.drawable.squats,
                ),
                Exercise(
                    id = 3,
                    exerciseName = "Push Ups",
                    image = R.drawable.push_ups,
                ),
                Exercise(
                    id = 4,
                    exerciseName = "Lunges",
                    image = R.drawable.lunges,
                )
            )
        }
    }
}