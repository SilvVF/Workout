package io.silv.workout.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.silv.workout.usecase.CalculateBmiUSUseCase
import io.silv.workout.usecase.CalculateBmiUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BmiViewModel @Inject constructor(
    private val bmiUSUseCase: CalculateBmiUSUseCase,
    private val bmiUseCase: CalculateBmiUseCase
): ViewModel() {

    val bmi = MutableStateFlow(0.0)

    private val mutableErrors = Channel<String>()
    val errors = mutableErrors.receiveAsFlow()

    fun calculateBmi(
        height: String,
        weight: String
    ) = viewModelScope.launch {
        kotlin.runCatching {
            bmiUseCase(
                heightCm = height.toDouble(),
                weightMetric = weight.toDouble()
            ).also {
                bmi.emit(it)
            }
        }
            .onFailure {
                mutableErrors.send(it.localizedMessage ?: "error")
            }
    }

    fun calculateBmiUs(
        heightFt: String,
        heightIn: String,
        weight: String
    ) = viewModelScope.launch {
        runCatching {
            bmiUSUseCase(
                heightFt = heightFt.toDouble(),
                heightIn = heightIn.toDouble(),
                weight = weight.toDouble()
            ).also {
                bmi.emit(it)
            }
        }
            .onFailure {
                mutableErrors.send(it.localizedMessage ?: "error")
            }
    }
}