package io.silv.workout.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): RoomDatabase {
        return Room.databaseBuilder(
            context,
            ExerciseDatabase::class.java,
            "exercise.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }


}