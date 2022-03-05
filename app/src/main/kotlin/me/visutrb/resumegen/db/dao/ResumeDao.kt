package me.visutrb.resumegen.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.visutrb.resumegen.entity.Resume

@Dao
interface ResumeDao {

    @Query("SELECT * FROM Resume")
    fun findAll(): List<Resume>

    @Query("SELECT * FROM Resume WHERE id = :id")
    fun findById(id: Int): Resume

    @Insert
    fun insertAll(vararg resumes: Resume)

    @Delete
    fun delete(resume: Resume)
}