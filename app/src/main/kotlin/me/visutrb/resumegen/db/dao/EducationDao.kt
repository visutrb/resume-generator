package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.Education

@Dao
interface EducationDao {

    @Query("SELECT * FROM Education")
    fun findAll(): List<Education>

    @Query("SELECT * FROM Education WHERE resumeId = :resumeId")
    fun findAllByResumeId(resumeId: Int): List<Education>

    @Query("SELECT * FROM Education WHERE id = :id")
    fun findById(id: Int): Education

    @Insert
    fun insertAll(vararg educations: Education)

    @Insert
    fun insert(education: Education)

    @Update
    fun update(education: Education)

    @Delete
    fun delete(education: Education)
}