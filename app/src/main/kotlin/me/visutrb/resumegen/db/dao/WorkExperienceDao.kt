package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.WorkExperience

@Dao
interface WorkExperienceDao {

    @Query("SELECT * FROM WorkExperience")
    suspend fun findAll(): List<WorkExperience>

    @Query("SELECT * FROM WorkExperience WHERE resumeId = :resumeId")
    suspend fun findAllByResumeId(resumeId: Int): List<WorkExperience>

    @Query("SELECT * FROM WorkExperience WHERE id = :id")
    suspend fun findById(id: Int): WorkExperience

    @Insert
    suspend fun insertAll(workExperiences: List<WorkExperience>): List<Long>

    @Insert
    suspend fun insert(workExperience: WorkExperience): Long

    @Update
    suspend fun update(workExperience: WorkExperience)

    @Update
    suspend fun updateAll(workExperiences: List<WorkExperience>)

    @Delete
    suspend fun delete(workExperience: WorkExperience)

    @Delete
    suspend fun deleteAll(workExperiences: List<WorkExperience>)
}