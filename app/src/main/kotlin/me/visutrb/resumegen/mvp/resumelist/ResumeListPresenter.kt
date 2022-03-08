package me.visutrb.resumegen.mvp.resumelist

import kotlinx.coroutines.launch
import me.visutrb.resumegen.db.dao.ResumeDao
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.mvp.Presenter

class ResumeListPresenter(
    private val resumeDao: ResumeDao
) : Presenter<ResumeListPresenter.View>() {

    fun loadResumes() = launch {
        val resumes = resumeDao.findAll()
        view?.renderResumes(resumes)
    }

    interface View : Presenter.View {
        fun renderResumes(resumes: List<Resume>)
    }
}