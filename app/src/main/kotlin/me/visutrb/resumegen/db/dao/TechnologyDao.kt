package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.Technology

@Dao
interface TechnologyDao {

    @Query("SELECT * FROM Technology")
    fun findAll(): List<Technology>

    @Query("SELECT * FROM Technology WHERE projectId = :projectId")
    fun findAllByProjectId(projectId: Int): List<Technology>

    @Query("SELECT * FROM Technology WHERE id = :id")
    fun findById(id: Int): Technology

    @Insert
    fun insertAll(vararg technologies: Technology)

    @Insert
    fun insert(technology: Technology)

    @Update
    fun update(technology: Technology)

    @Delete
    fun delete(technology: Technology)
}