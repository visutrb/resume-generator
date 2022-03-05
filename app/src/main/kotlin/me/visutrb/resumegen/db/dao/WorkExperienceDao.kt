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
    suspend fun insertAll(vararg workExperiences: WorkExperience)

    @Insert
    suspend fun insert(workExperience: WorkExperience)

    @Update
    suspend fun update(workExperience: WorkExperience)

    @Delete
    suspend fun delete(workExperience: WorkExperience)
}