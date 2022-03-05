package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume

@Dao
interface ProjectDao {

    @Query("SELECT * FROM Project")
    suspend fun findAll(): List<Project>

    @Query("SELECT * FROM Project WHERE resumeId = :resumeId")
    suspend fun findAllByResumeId(resumeId: Int): List<Project>

    @Query("SELECT * FROM Project WHERE id = :id")
    suspend fun findById(id: Int): Project

    @Insert
    suspend fun insertAll(vararg resumes: Resume)

    @Insert
    suspend fun insert(resume: Resume)

    @Update
    suspend fun update(resume: Resume)

    @Delete
    suspend fun delete(resume: Resume)
}