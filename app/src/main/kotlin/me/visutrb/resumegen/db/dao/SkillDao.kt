package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.Skill

@Dao
interface SkillDao {

    @Query("SELECT * FROM Skill")
    fun findAll(): List<Skill>

    @Query("SELECT * FROM Skill WHERE resumeId = :resumeId")
    fun findByResumeId(resumeId: Int): List<Skill>

    @Query("SELECT * FROM Skill WHERE id = :id")
    fun findById(id: Int): Skill

    @Insert
    fun insertAll(vararg skills: Skill)

    @Insert
    fun insert(skill: Skill)

    @Update
    fun update(skill: Skill)

    @Delete
    fun delete(skill: Skill)
}