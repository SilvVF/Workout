package io.silv.workout.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.silv.workout.local.ExerciseDao


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelExerciseModule {

    @Provides
    @ViewModelScoped
    fun provideExerciseViewModel(handle: SavedStateHandle) = ExerciseViewModel(handle)
}



@Module
@InstallIn(ViewModelComponent::class)
object ViewModelFinishModule {

    @Provides
    @ViewModelScoped
    fun provideExerciseViewModel(
        exerciseDao: ExerciseDao
    ) = FinishViewModel(exerciseDao)
}