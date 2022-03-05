package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.Technology

@Dao
interface TechnologyDao {

    @Query("SELECT * FROM Technology")
    suspend fun findAll(): List<Technology>

    @Query("SELECT * FROM Technology WHERE projectId = :projectId")
    suspend fun findAllByProjectId(projectId: Int): List<Technology>

    @Query("SELECT * FROM Technology WHERE id = :id")
    suspend fun findById(id: Int): Technology

    @Insert
    suspend fun insertAll(vararg technologies: Technology)

    @Insert
    suspend fun insert(technology: Technology)

    @Update
    suspend fun update(technology: Technology)

    @Delete
    suspend fun delete(technology: Technology)
}