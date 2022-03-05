package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.Skill

@Dao
interface SkillDao {

    @Query("SELECT * FROM Skill")
    suspend fun findAll(): List<Skill>

    @Query("SELECT * FROM Skill WHERE resumeId = :resumeId")
    suspend fun findByResumeId(resumeId: Int): List<Skill>

    @Query("SELECT * FROM Skill WHERE id = :id")
    suspend fun findById(id: Int): Skill

    @Insert
    suspend fun insertAll(vararg skills: Skill)

    @Insert
    suspend fun insert(skill: Skill)

    @Update
    suspend fun update(skill: Skill)

    @Delete
    suspend fun delete(skill: Skill)
}