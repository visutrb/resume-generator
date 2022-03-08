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
    suspend fun insertAll(educations: List<Education>): List<Long>

    @Insert
    suspend fun insert(education: Education): Long

    @Update
    suspend fun update(education: Education)

    @Update
    suspend fun updateAll(educations: List<Education>)

    @Delete
    suspend fun delete(education: Education)

    @Delete
    suspend fun deleteAll(educations: List<Education>)
}