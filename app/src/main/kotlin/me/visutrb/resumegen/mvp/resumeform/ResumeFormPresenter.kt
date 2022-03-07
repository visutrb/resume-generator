package me.visutrb.resumegen.mvp.resumeform

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

class ResumeFormPresenter(
    private val resumeDao: ResumeDao,
    private val educationDao: EducationDao,
    private val workExperienceDao: WorkExperienceDao,
    private val projectDao: ProjectDao
) : Presenter<ResumeFormPresenter.View>() {

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
            if (resume.id == 0) {
                insertData(resume, educations, workExperiences, projects)
            } else {
                updateData(resume, educations, workExperiences, projects)
                removeDeletedData()
            }
            view?.onDataSaved()
        } catch (e: Exception) {
            view?.onSaveDataFailed(e)
        }
    }

    private suspend fun insertData(
        resume: Resume,
        educations: List<Education>,
        workExperiences: List<WorkExperience>,
        projects: List<Project>
    ) {
        val resumeId = withContext(Dispatchers.IO) { resumeDao.insert(resume) }.toInt()
        educations.forEach { it.resumeId = resumeId }
        workExperiences.forEach { it.resumeId = resumeId }
        projects.forEach { it.resumeId = resumeId }

        withContext(Dispatchers.IO) {
            educationDao.insertAll(educations)
            workExperienceDao.insertAll(workExperiences)
            projectDao.insertAll(projects)
        }
    }

    private suspend fun updateData(
        resume: Resume,
        educations: List<Education>,
        workExperiences: List<WorkExperience>,
        projects: List<Project>
    ) {
        withContext(Dispatchers.IO) {
            resumeDao.update(resume)
            educationDao.updateAll(educations)
            workExperienceDao.updateAll(workExperiences)
            projectDao.updateAll(projects)
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