package me.visutrb.resumegen.mvp.resumedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ActivityResumeDetailsBinding
import me.visutrb.resumegen.entity.Education
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.entity.WorkExperience
import me.visutrb.resumegen.mvp.Activity
import me.visutrb.resumegen.mvp.resumeform.ResumeFormActivityResultContract
import me.visutrb.resumegen.mvp.resumeform.ResumeFormData
import org.koin.android.ext.android.inject
import java.io.File

class ResumeDetailsActivity : Activity(), ResumeDetailsPresenter.View {

    private lateinit var binding: ActivityResumeDetailsBinding

    private lateinit var resume: Resume

    private var educations = mutableListOf<Education>()
    private var workExperiences = mutableListOf<WorkExperience>()
    private var projects = mutableListOf<Project>()

    private val presenter: ResumeDetailsPresenter by inject()

    private lateinit var resumeFormLauncher: ActivityResultLauncher<ResumeFormData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResumeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupLaunchers()

        resume = intent.getParcelableExtra(EXTRA_RESUME)!!

        renderResume()
        renderSkills()

        presenter.view = this
        presenter.loadRelatedData(resume)
    }

    private fun setupLaunchers() {
        resumeFormLauncher = registerForActivityResult(ResumeFormActivityResultContract()) { data ->
            if (data == null) {
                return@registerForActivityResult
            }

            data.resume?.let { resume = it }
            data.educations.toMutableList().let { educations = it }
            data.workExperiences.toMutableList().let { workExperiences = it }
            data.projects.toMutableList().let { projects = it }

            renderResume()
            renderSkills()
            renderEducations()
            renderWorkExperiences()
            renderProjects()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_resume_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_edit -> {
                launchResumeFormActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        chipGroup.removeAllViews()
        for (skill in resume.skillsHolder.skills) {
            val chip = Chip(this).apply {
                text = skill.name
            }
            chipGroup.addView(chip)
        }
    }

    private fun renderEducations() {
        val container = binding.content.educationsContainerLayout
        container.removeAllViews()
        for (education in educations) {
            val view = EducationView(this).also {
                container.addView(it)
            }
            view.education = education
        }
    }

    private fun renderWorkExperiences() {
        val container = binding.content.workExperiencesContainerLayout
        for (workExperience in workExperiences) {
            val view = WorkExperienceView(this).also {
                container.addView(it)
            }
            view.workExperience = workExperience
        }
    }

    private fun renderProjects() {
        val container = binding.content.projectsContainerLayout
        container.removeAllViews()
        for (project in projects) {
            val view = ProjectView(this).also {
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

    private fun launchResumeFormActivity() {
        val input = ResumeFormData(
            resume,
            educations,
            workExperiences,
            projects
        )
        resumeFormLauncher.launch(input)
    }

    companion object {
        private const val TAG = "ResumeDetailsActivity"

        const val EXTRA_RESUME = "extra_resume"

        fun newIntent(context: Context, resume: Resume) =
            Intent(context, ResumeDetailsActivity::class.java).apply {
                putExtra(EXTRA_RESUME, resume)
            }
    }
}