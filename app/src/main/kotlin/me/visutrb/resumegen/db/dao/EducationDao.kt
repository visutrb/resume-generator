package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.entity.Education

@Dao
interface EducationDao {

    @Query("SELECT * FROM Education")
    suspend fun findAll(): List<Education>

    @Query("SELECT * FROM Education WHERE resumeId = :resumeId")
    suspend fun findAllByResumeId(resumeId: Int): List<Education>

    @Query("SELECT * FROM Education WHERE id = :id")
    suspend fun findById(id: Int): Education

    @Insert
    suspend fun insertAll(vararg educations: Education)

    @Insert
    suspend fun insert(education: Education)

    @Update
    suspend fun update(education: Education)

    @Delete
    suspend fun delete(education: Education)
}