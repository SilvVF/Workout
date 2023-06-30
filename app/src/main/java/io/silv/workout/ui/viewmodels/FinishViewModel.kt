package io.silv.workout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.silv.workout.local.ExerciseDao
import io.silv.workout.local.ExerciseEntity
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class FinishViewModel @Inject constructor(
    private val exerciseDao: ExerciseDao
): ViewModel() {

    fun saveToExerciseSession() {
        viewModelScope.launch {
            exerciseDao.insert(
                ExerciseEntity(
                    date = Calendar.getInstance().time.toString()
                )
            )
        }
    }
}