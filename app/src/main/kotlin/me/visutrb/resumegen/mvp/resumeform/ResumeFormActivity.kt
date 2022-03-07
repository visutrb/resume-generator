package me.visutrb.resumegen.mvp.resumeform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.children
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ActivityResumeFormBinding
import me.visutrb.resumegen.db.dao.ResumeDao
import me.visutrb.resumegen.entity.*
import me.visutrb.resumegen.mvp.Activity
import me.visutrb.resumegen.mvp.educationform.EducationFormResultContract
import me.visutrb.resumegen.mvp.projectform.ProjectFormResultContract
import me.visutrb.resumegen.mvp.workexpform.WorkExperienceFormResultContract
import org.koin.android.ext.android.inject

class ResumeFormActivity : Activity() {

    private lateinit var binding: ActivityResumeFormBinding

    private lateinit var educationFormResultLauncher: ActivityResultLauncher<Education?>
    private lateinit var projectFormResultLauncher: ActivityResultLauncher<Project?>
    private lateinit var workExperienceFormResultLauncher: ActivityResultLauncher<WorkExperience?>

    private lateinit var resume: Resume

    private var educations = mutableListOf<Education>()
    private var workExperiences = mutableListOf<WorkExperience>()
    private var projects = mutableListOf<Project>()

    private val resumeDao: ResumeDao by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupLaunchers()
        setupInitialData()
        setupViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_form_save -> {
                validateAndSaveResult()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupActionBar() {
        supportActionBar?.let {
            it.title = getString(R.string.resume_form_title)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_close_filled)
        }
    }

    private fun setupLaunchers() {
        educationFormResultLauncher =
            registerForActivityResult(EducationFormResultContract()) { education ->
                education?.let { addOrUpdateEducation(it) }
            }

        projectFormResultLauncher =
            registerForActivityResult(ProjectFormResultContract()) { project ->
                project?.let { addOrUpdateProject(project) }
            }

        workExperienceFormResultLauncher =
            registerForActivityResult(WorkExperienceFormResultContract()) { workExperience ->
                workExperience?.let { addOrUpdateWorkExperience(it) }
            }
    }

    private fun setupInitialData() {
        resume = intent.getParcelableExtra(EXTRA_RESUME) ?: Resume()
        resume.let {
            binding.firstNameEdt.setText(it.firstName)
            binding.lastNameEdt.setText(it.lastName)
            binding.mobileNumberEdt.setText(it.phoneNumber)
            binding.emailEdt.setText(it.emailAddress)
            binding.addressEdt.setText(it.address.street)
            binding.cityEdt.setText(it.address.city)
            binding.stateEdt.setText(it.address.state)
            binding.countryEdt.setText(it.address.country)
            binding.postalCodeEdt.setText(it.address.postalCode)
            binding.careerObjectiveEdt.setText(it.careerObjective)
        }

        resume.skillsHolder.skills.forEach { addSkillChip(it) }
    }

    private fun setupViews() {
        binding.profilePictureImv.setOnClickListener { selectOrTakePicture() }

        binding.mobileNumberEdt.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.skillEdt.apply {
            setOnEditorActionListener { view, actionId, event ->
                if (view.text.isNotEmpty() && actionId == EditorInfo.IME_ACTION_NEXT) {
                    addSkill()
                }
                true
            }
            setOnKeyListener { view, keyCode, event ->
                if ((view as EditText).text.isNotEmpty() && keyCode == KeyEvent.KEYCODE_ENTER) {
                    addSkill()
                    true
                } else {
                    false
                }
            }
        }

        binding.addEducationBtn.setOnClickListener { launchEducationFormActivity() }
        binding.addProjectBtn.setOnClickListener { launchProjectFormActivity() }
        binding.addWorkExperienceBtn.setOnClickListener { launchWorkExperienceFormActivity() }
    }

    private fun selectOrTakePicture() {
        Log.d(TAG, "selectOrTakePicture")
    }

    private fun addSkill() {
        val skillName = binding.skillEdt.text.toString()
        if (skillName.isBlank()) {
            return
        }
        binding.skillEdt.setText("")
        val skill = Skill(name = skillName)
        resume.skillsHolder.skills.add(skill)
        addSkillChip(skill)
    }

    private fun addSkillChip(skill: Skill) {
        val chip = Chip(this).apply {
            text = skill.name
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                resume.skillsHolder.skills.remove(skill)
                binding.skillsChipGroup.removeView(it)
            }
        }
        binding.skillsChipGroup.addView(chip)
    }

    private fun addOrUpdateEducation(education: Education) {
        val idx = educations.indexOfFirst { it.uuid == education.uuid }
        if (idx != -1) {
            educations[idx] = education
            binding.educationsContainerLayout.children.find {
                (it as EducationPreviewView).education?.uuid == education.uuid
            }?.let {
                (it as EducationPreviewView).education = education
            }
            return
        }

        educations.add(education)
        val preview = EducationPreviewView(this).apply {
            this.education = education
            onEditEducation = { launchEducationFormActivity(it) }
            onDeleteEducation = {
                educations.remove(education)
                binding.educationsContainerLayout.removeView(this)
            }
        }
        binding.educationsContainerLayout.addView(preview)
    }

    private fun addOrUpdateWorkExperience(workExperience: WorkExperience) {
        val idx = workExperiences.indexOfFirst { it.uuid == workExperience.uuid }
        if (idx != -1) {
            workExperiences[idx] = workExperience
            binding.workExperiencesContainerLayout.children.find {
                (it as WorkExperiencePreviewView).workExperience?.uuid == workExperience.uuid
            }?.let {
                (it as WorkExperiencePreviewView).workExperience = workExperience
            }
            return
        }

        workExperiences.add(workExperience)
        val preview = WorkExperiencePreviewView(this).apply {
            this.workExperience = workExperience
            onEditWorkExperience = { launchWorkExperienceFormActivity(it) }
            onDeleteWorkExperience = {
                workExperiences.remove(workExperience)
                binding.workExperiencesContainerLayout.removeView(this)
            }
        }
        binding.workExperiencesContainerLayout.addView(preview)
    }

    private fun addOrUpdateProject(project: Project) {
        val idx = projects.indexOfFirst { it.uuid == project.uuid }
        if (idx != -1) {
            projects[idx] = project
            binding.projectsContainerLayout.children.find {
                (it as ProjectPreviewView).project?.uuid == project.uuid
            }?.let {
                (it as ProjectPreviewView).project = project
            }
            return
        }

        projects.add(project)
        val preview = ProjectPreviewView(this).apply {
            this.project = project
            onEditProject = { launchProjectFormActivity(it) }
            onDeleteProject = {
                projects.remove(project)
                binding.projectsContainerLayout.removeView(this)
            }
        }
        binding.projectsContainerLayout.addView(preview)
    }

    private fun launchEducationFormActivity(education: Education? = null) {
        educationFormResultLauncher.launch(education)
    }

    private fun launchProjectFormActivity(project: Project? = null) {
        projectFormResultLauncher.launch(project)
    }

    private fun launchWorkExperienceFormActivity(workExperience: WorkExperience? = null) {
        workExperienceFormResultLauncher.launch(workExperience)
    }

    private fun validateAndSaveResult() {
        var isValid = true

        val firstName = binding.firstNameEdt.text.toString()
        val lastName = binding.lastNameEdt.text.toString()
        val mobile = binding.mobileNumberEdt.text.toString()
        val street = binding.addressEdt.text.toString()
        val city = binding.cityEdt.text.toString()
        val obj = binding.careerObjectiveEdt.text.toString()

        if (firstName.isBlank() || firstName.isEmpty()) {
            binding.firstNameEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (lastName.isBlank() || lastName.isEmpty()) {
            binding.lastNameEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (mobile.isBlank() || mobile.isEmpty()) {
            binding.mobileNumberEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (street.isBlank() || street.isEmpty()) {
            binding.addressEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (city.isBlank() || city.isEmpty()) {
            binding.cityEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (obj.isBlank() || obj.isEmpty()) {
            binding.careerObjectiveEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (!isValid) {
            return
        }

        launch {
            withContext(Dispatchers.IO) { resumeDao.insert(resume) }
        }
    }

    companion object {
        private const val TAG = "ResumeFormActivity"

        const val EXTRA_RESUME = "extra_resume"

        fun newIntent(context: Context): Intent = Intent(context, ResumeFormActivity::class.java)
    }
}