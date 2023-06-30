package io.silv.workout.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM history")
    fun fetchAll(): Flow<List<ExerciseEntity>>

    @Insert
    suspend fun insert(exerciseEntity: ExerciseEntity)

    @Delete
    suspend fun delete(exerciseEntity: ExerciseEntity)

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deleteById(id: Int)
}