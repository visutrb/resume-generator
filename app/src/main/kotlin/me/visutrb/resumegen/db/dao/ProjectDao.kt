package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.db.dto.ProjectWithTechnologies
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

    @Query("SELECT * FROM Project WHERE id = :id")
    suspend fun findByIdWithTechnologies(id: Int): ProjectWithTechnologies

    @Insert
    suspend fun insertAll(projects: List<Project>): List<Long>

    @Insert
    suspend fun insert(project: Project): Long

    @Update
    suspend fun update(project: Project)

    @Update
    suspend fun updateAll(projects: List<Project>)

    @Delete
    suspend fun delete(project: Project)

    @Delete
    suspend fun deleteAll(projects: List<Project>)
}