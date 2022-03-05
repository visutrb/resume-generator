package me.visutrb.resumegen.db.dao

import androidx.room.*
import me.visutrb.resumegen.db.dto.ResumeWithRelations
import me.visutrb.resumegen.entity.Resume

@Dao
interface ResumeDao {

    @Query("SELECT * FROM Resume")
    suspend fun findAll(): List<Resume>

    @Query("SELECT * FROM Resume")
    suspend fun findAllWithRelations(): List<ResumeWithRelations>

    @Query("SELECT * FROM Resume WHERE id = :id")
    suspend fun findById(id: Int): Resume

    @Query("SELECT * FROM Resume WHERE id = :id")
    suspend fun findByIdWithRelations(id: Int): ResumeWithRelations

    @Insert
    suspend fun insertAll(vararg resumes: Resume)

    @Insert
    suspend fun insert(resume: Resume)

    @Update
    suspend fun update(resume: Resume)

    @Delete
    suspend fun delete(resume: Resume)
}