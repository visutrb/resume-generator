package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.WorkExperience

@Dao
interface WorkExperienceDao {

    @Query("SELECT * FROM WorkExperience")
    fun findAll(): List<WorkExperience>

    @Query("SELECT * FROM WorkExperience WHERE resumeId = :resumeId")
    fun findAllByResumeId(resumeId: Int): List<WorkExperience>

    @Query("SELECT * FROM WorkExperience WHERE id = :id")
    fun findById(id: Int): WorkExperience

    @Insert
    fun insertAll(vararg workExperiences: WorkExperience)

    @Insert
    fun insert(workExperience: WorkExperience)

    @Update
    fun update(workExperience: WorkExperience)

    @Delete
    fun delete(workExperience: WorkExperience)
}