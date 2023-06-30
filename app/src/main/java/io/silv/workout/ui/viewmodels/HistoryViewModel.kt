package io.silv.workout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.silv.workout.local.ExerciseDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val exerciseDao: ExerciseDao
): ViewModel() {

    val exerciseHistory = exerciseDao.fetchAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun deleteExerciseById(id: Int) = viewModelScope.launch {
        exerciseDao.deleteById(id)
    }
}