package io.silv.workout.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.silv.workout.local.ExerciseDao
import io.silv.workout.usecase.CalculateBmiUSUseCase
import io.silv.workout.usecase.CalculateBmiUseCase


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelExerciseModule {

    @Provides
    @ViewModelScoped
    fun provideExerciseViewModel(handle: SavedStateHandle) = ExerciseViewModel(handle)
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelBmiModule {

    @Provides
    @ViewModelScoped
    fun provideBmiViewModel() = BmiViewModel(
        bmiUseCase = CalculateBmiUseCase(),
        bmiUSUseCase = CalculateBmiUSUseCase()
    )
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelHistoryModule {

    @Provides
    @ViewModelScoped
    fun provideHistoryViewModel(
        exerciseDao: ExerciseDao
    ) = HistoryViewModel(
        exerciseDao = exerciseDao
    )
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