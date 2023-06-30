package io.silv.workout.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExerciseEntity::class],
    version = 1,
    exportSchema = true
)
abstract class ExerciseDatabase: RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
}