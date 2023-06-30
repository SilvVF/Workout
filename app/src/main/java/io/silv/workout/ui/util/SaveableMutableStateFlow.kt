package io.silv.workout.ui.util

import androidx.lifecycle.SavedStateHandle

class SaveableMutableStateFlow<T>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String,
    initialValue: T,
) {
    private val state = savedStateHandle.getStateFlow(key, initialValue)
    var value: T
        get() = state.value
        set(value) {
            savedStateHandle[key] = value
        }

    fun asStateFlow() = state
}

fun <T> SavedStateHandle.getMutableStateFlow(key: String, initialValue: T) =
    SaveableMutableStateFlow(this, key, initialValue)