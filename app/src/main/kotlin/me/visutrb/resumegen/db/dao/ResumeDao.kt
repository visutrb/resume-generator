package me.visutrb.resumegen.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.visutrb.resumegen.entity.Resume

interface ResumeDao {

    @Query("SELECT * FROM resume")
    fun findAll(): List<Resume>

    @Query("SELECT * FROM resume WHERE id = :id")
    fun findById(id: Int): Resume

    @Insert
    fun insertAll(vararg resumes: Resume)

    @Delete
    fun delete(resume: Resume)
}