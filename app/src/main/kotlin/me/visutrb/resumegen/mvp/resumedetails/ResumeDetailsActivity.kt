package me.visutrb.resumegen.mvp.resumedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ActivityResumeDetailsBinding
import me.visutrb.resumegen.entity.Education
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.entity.WorkExperience
import me.visutrb.resumegen.mvp.Activity
import org.koin.android.ext.android.inject
import java.io.File

class ResumeDetailsActivity : Activity(), ResumeDetailsPresenter.View {

    private lateinit var binding: ActivityResumeDetailsBinding

    private lateinit var resume: Resume

    private var educations = mutableListOf<Education>()
    private var workExperiences = mutableListOf<WorkExperience>()
    private var projects = mutableListOf<Project>()

    private val presenter: ResumeDetailsPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResumeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        resume = intent.getParcelableExtra(EXTRA_RESUME)!!

        renderResume()
        renderSkills()

        presenter.view = this
        presenter.loadRelatedData(resume)
    }

    private fun renderResume() {
        val yearsExp = resume.totalYearsOfExp
        binding.content.fullNameTv.text = resume.fullName
        binding.content.jobTitleTv.text = resume.role
        binding.content.addressLine1Tv.text = resume.address.addressLine1
        binding.content.addressLine2Tv.text = resume.address.addressLine2
        binding.content.phoneTv.text = resume.phoneNumber
        binding.content.emailTv.text = resume.emailAddress
        binding.content.objTv.text = resume.careerObjective
        binding.content.yearsOfExpTv.text = resources.getQuantityString(
            R.plurals.resume_details_years_of_exp,
            yearsExp,
            yearsExp
        )
        if (resume.profilePicturePath.isNotEmpty()) {
            Glide.with(binding.root)
                .load(File(resume.profilePicturePath))
                .into(binding.profilePictureImv)
        }
    }

    private fun renderSkills() {
        val chipGroup = binding.content.skillsChipGroup
        for (skill in resume.skillsHolder.skills) {
            val chip = Chip(this).apply {
                text = skill.name
            }
            chipGroup.addView(chip)
        }
    }

    private fun renderEducations() {
        val container = binding.content.educationsContainerLayout
        for (education in educations) {
            val view = container.findViewWithTag(education.uuid)
                ?: EducationView(this).also {
                    it.tag = education.uuid
                    container.addView(it)
                }
            view.education = education
        }
    }

    private fun renderWorkExperiences() {
        val container = binding.content.workExperiencesContainerLayout
        for (workExperience in workExperiences) {
            val view = container.findViewWithTag(workExperience.uuid)
                ?: WorkExperienceView(this).also {
                    it.tag = workExperience.uuid
                    container.addView(it)
                }
            view.workExperience = workExperience
        }
    }

    private fun renderProjects() {
        val container = binding.content.projectsContainerLayout
        for (project in projects) {
            val view = container.findViewWithTag(project.uuid)
                ?: ProjectView(this).also {
                    it.tag = project.uuid
                    container.addView(it)
                }
            view.project = project
        }
    }

    override fun onRelatedDataLoaded(
        educations: List<Education>,
        workExperiences: List<WorkExperience>,
        projects: List<Project>
    ) {
        this.educations = educations.toMutableList()
        this.workExperiences = workExperiences.toMutableList()
        this.projects = projects.toMutableList()

        renderEducations()
        renderWorkExperiences()
        renderProjects()
    }

    companion object {
        const val EXTRA_RESUME = "extra_resume"

        fun newIntent(context: Context, resume: Resume) =
            Intent(context, ResumeDetailsActivity::class.java).apply {
                putExtra(EXTRA_RESUME, resume)
            }
    }
}