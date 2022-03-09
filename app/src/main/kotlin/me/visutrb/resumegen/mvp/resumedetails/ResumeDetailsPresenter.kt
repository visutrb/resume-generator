package me.visutrb.resumegen.mvp.resumedetails

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.visutrb.resumegen.db.dao.EducationDao
import me.visutrb.resumegen.db.dao.ProjectDao
import me.visutrb.resumegen.db.dao.WorkExperienceDao
import me.visutrb.resumegen.entity.Education
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.entity.WorkExperience
import me.visutrb.resumegen.mvp.Presenter

class ResumeDetailsPresenter(
    private val educationDao: EducationDao,
    private val workExperienceDao: WorkExperienceDao,
    private val projectDao: ProjectDao,
) : Presenter<ResumeDetailsPresenter.View>() {

    fun loadRelatedData(resume: Resume) {
        launch {
            try {
                var educations: List<Education>
                var workExperiences: List<WorkExperience>
                var projects: List<Project>
                withContext(Dispatchers.IO) {
                    educations = educationDao.findAllByResumeId(resume.id)
                    workExperiences = workExperienceDao.findAllByResumeId(resume.id)
                    projects = projectDao.findAllByResumeId(resume.id)
                }
                view?.onRelatedDataLoaded(
                    educations,
                    workExperiences,
                    projects
                )
            } catch (e: Exception) {
                view?.onLoadRelatedDataFailed(e)
            }
        }
    }

    interface View : Presenter.View {
        fun onRelatedDataLoaded(
            educations: List<Education>,
            workExperiences: List<WorkExperience>,
            projects: List<Project>
        )

        fun onLoadRelatedDataFailed(e: Exception) {}
    }
}