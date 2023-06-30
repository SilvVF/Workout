package io.silv.workout.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.silv.workout.data.types.Exercise
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val mutableEventChannel = Channel<ExerciseEvent>()
    val eventChannel = mutableEventChannel.receiveAsFlow()

    val exercises = MutableStateFlow(Exercise.exerciseList)

    private var exercisePos: Int = savedStateHandle[exercisePosKey] ?: 0
        set(value) {
            savedStateHandle[exercisePosKey] = value
            field = value
        }


    val exerciseTimer = flow {
        exercises.getAndUpdate {
            it.toMutableList().apply {
                set(
                    exercisePos,
                    exercises.value[exercisePos].copy(isSelected = true)
                )
            }
        }
        var time = initialExerciseTime
        while (time >= 0) {
            delay(1000)
            emit(time--)
        }
        exercises.getAndUpdate {
            it.toMutableList().apply {
                set(
                    exercisePos,
                    exercises.value[exercisePos].copy(isSelected = false, isCompleted = true)
                )
            }
        }
        exercisePos += 1
        return@flow
    }

    val restTimer = flow {
        var time = initialRestTime
        while (time >= 0) {
            delay(1000)
            emit(time--)
        }
        return@flow
    }

    fun startExerciseTimer() = viewModelScope.launch {
        mutableEventChannel.send(
            ExerciseEvent.ExerciseStart(
                exercise = exercises.value[exercisePos],
                isLast = exercisePos == exercises.value.lastIndex
            )
        )
    }

    fun startRestTimer() = viewModelScope.launch {
        mutableEventChannel.send(
            ExerciseEvent.RestStart(
                nextExercise = exercises.value[exercisePos]
            )
        )
    }

    companion object {
        const val initialRestTime = 2
        const val initialExerciseTime = 3
        const val exercisePosKey = "exercise_pos_key"
    }
}

sealed interface ExerciseEvent {

    data class RestStart(val nextExercise: Exercise): ExerciseEvent

    data class ExerciseStart(val exercise: Exercise, val isLast: Boolean): ExerciseEvent
}