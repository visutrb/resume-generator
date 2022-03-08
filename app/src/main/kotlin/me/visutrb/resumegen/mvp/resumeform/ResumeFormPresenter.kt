package me.visutrb.resumegen.mvp.resumeform

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.visutrb.resumegen.db.dao.EducationDao
import me.visutrb.resumegen.db.dao.ProjectDao
import me.visutrb.resumegen.db.dao.ResumeDao
import me.visutrb.resumegen.db.dao.WorkExperienceDao
import me.visutrb.resumegen.entity.Education
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.entity.WorkExperience
import me.visutrb.resumegen.mvp.Presenter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ResumeFormPresenter(
    private val resumeDao: ResumeDao,
    private val educationDao: EducationDao,
    private val workExperienceDao: WorkExperienceDao,
    private val projectDao: ProjectDao
) : Presenter<ResumeFormPresenter.View>() {

    var currentImageFile: File? = null
        private set


    private val deletedEducations = mutableListOf<Education>()
    private val deletedWorkExperiences = mutableListOf<WorkExperience>()
    private val deletedProjects = mutableListOf<Project>()

    fun loadRelatedData(resume: Resume) = launch {
        val resumeId = resume.id
        if (resumeId == 0) {
            return@launch
        }
        try {
            val educations =
                withContext(Dispatchers.IO) { educationDao.findAllByResumeId(resumeId) }
            val workExperiences =
                withContext(Dispatchers.IO) { workExperienceDao.findAllByResumeId(resumeId) }
            val projects =
                withContext(Dispatchers.IO) { projectDao.findAllByResumeId(resumeId) }

            view?.onRelatedDataLoaded(
                educations,
                workExperiences,
                projects
            )
        } catch (e: Exception) {
            view?.onLoadRelatedDataFailed(e)
        }
    }

    fun saveData(
        resume: Resume,
        educations: List<Education>,
        workExperiences: List<WorkExperience>,
        projects: List<Project>
    ) = launch {
        try {
            insertOrUpdateData(resume, educations, workExperiences, projects)
            removeDeletedData()
            view?.onDataSaved()
        } catch (e: Exception) {
            view?.onSaveDataFailed(e)
        }
    }

    private suspend fun insertOrUpdateData(
        resume: Resume,
        educations: List<Education>,
        workExperiences: List<WorkExperience>,
        projects: List<Project>
    ) {
        var resumeId: Int
        if (resume.id == 0) {
            resumeId = withContext(Dispatchers.IO) { resumeDao.insert(resume) }.toInt()
        } else {
            resumeId = resume.id
            withContext(Dispatchers.IO) { resumeDao.update(resume) }
        }

        val newEducations = mutableListOf<Education>()
        val oldEducations = mutableListOf<Education>()
        educations.forEach {
            if (it.resumeId == 0) {
                it.resumeId = resumeId
            }
            if (it.id == 0) {
                newEducations.add(it)
            } else {
                oldEducations.add(it)
            }
        }

        val newWorkExperiences = mutableListOf<WorkExperience>()
        val oldWorkExperiences = mutableListOf<WorkExperience>()
        workExperiences.forEach {
            if (it.resumeId == 0) {
                it.resumeId = resumeId
            }
            if (it.id == 0) {
                newWorkExperiences.add(it)
            } else {
                oldWorkExperiences.add(it)
            }
        }

        val newProjects = mutableListOf<Project>()
        val oldProjects = mutableListOf<Project>()
        projects.forEach {
            if (it.resumeId == 0) {
                it.resumeId = resumeId
            }
            if (it.id == 0) {
                newProjects.add(it)
            } else {
                oldProjects.add(it)
            }
        }

        withContext(Dispatchers.IO) {
            educationDao.updateAll(oldEducations)
            educationDao.insertAll(newEducations)

            println("oldWorkExperiences: $oldWorkExperiences")
            println("newWorkExperiences: $newWorkExperiences")
            workExperienceDao.updateAll(oldWorkExperiences)
            workExperienceDao.insertAll(newWorkExperiences)

            projectDao.updateAll(oldProjects)
            projectDao.insertAll(newProjects)
        }
    }

    fun deleteEducation(education: Education) {
        if (education.id == 0) {
            return
        }
        deletedEducations.add(education)
    }

    fun deleteWorkExperience(workExperience: WorkExperience) {
        if (workExperience.id == 0) {
            return
        }
        deletedWorkExperiences.add(workExperience)
    }

    fun deleteProject(project: Project) {
        if (project.id == 0) {
            return
        }
        deletedProjects.add(project)
    }

    private suspend fun removeDeletedData() {
        withContext(Dispatchers.IO) {
            educationDao.deleteAll(deletedEducations)
            workExperienceDao.deleteAll(deletedWorkExperiences)
            projectDao.deleteAll(deletedProjects)
        }
    }

    fun getCurrentImageFileUri(context: Context): Uri? {
        return currentImageFile?.let {
            FileProvider.getUriForFile(
                context,
                "me.visutrb.resumegen.fileprovider",
                it
            )
        }
    }

    fun createTempImageFile(context: Context) {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        currentImageFile = File.createTempFile("$timestamp", ".jpg", picturesDir)
    }

    fun createLocalImageFile(context: Context, uri: Uri) {
        createTempImageFile(context)

        val inputStream = context.contentResolver.openInputStream(uri) ?: return
        val outputStream = currentImageFile?.outputStream() ?: return

        val buffer = ByteArray(1024)
        var nextByte = 0
        do {
            nextByte = inputStream.read(buffer)
            outputStream.write(buffer)
        } while (nextByte != -1)

        outputStream.close()
        inputStream.close()
    }

    fun removeCurrentTempImageFile() {
        currentImageFile?.delete()
        currentImageFile = null
    }

    interface View : Presenter.View {

        fun onRelatedDataLoaded(
            educations: List<Education>,
            workExperiences: List<WorkExperience>,
            projects: List<Project>
        )

        fun onLoadRelatedDataFailed(e: Exception)

        fun onDataSaved()
        fun onSaveDataFailed(e: Exception)
    }
}