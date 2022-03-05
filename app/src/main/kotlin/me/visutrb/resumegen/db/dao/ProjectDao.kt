package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume

@Dao
interface ProjectDao {

    @Query("SELECT * FROM Project")
    fun findAll(): List<Project>

    @Query("SELECT * FROM Project WHERE resumeId = :resumeId")
    fun findAllByResumeId(resumeId: Int): List<Project>

    @Query("SELECT * FROM Project WHERE id = :id")
    fun findById(id: Int): Project

    @Insert
    fun insertAll(vararg resumes: Resume)

    @Insert
    fun insert(resume: Resume)

    @Update
    fun update(resume: Resume)

    @Delete
    fun delete(resume: Resume)
}