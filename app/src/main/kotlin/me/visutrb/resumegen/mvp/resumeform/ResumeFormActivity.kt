package me.visutrb.resumegen.mvp.resumeform

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ActivityResumeFormBinding
import me.visutrb.resumegen.entity.*
import me.visutrb.resumegen.mvp.Activity
import me.visutrb.resumegen.mvp.educationform.EducationFormResultContract
import me.visutrb.resumegen.mvp.projectform.ProjectFormResultContract
import me.visutrb.resumegen.mvp.workexpform.WorkExperienceFormResultContract
import org.koin.android.ext.android.inject
import java.io.File

class ResumeFormActivity : Activity(), ResumeFormPresenter.View {

    private lateinit var binding: ActivityResumeFormBinding

    private lateinit var educationFormResultLauncher: ActivityResultLauncher<Education?>
    private lateinit var projectFormResultLauncher: ActivityResultLauncher<Project?>
    private lateinit var workExperienceFormResultLauncher: ActivityResultLauncher<WorkExperience?>

    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestReadExternalStoragePermissionLauncher: ActivityResultLauncher<String>

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var selectImageLauncher: ActivityResultLauncher<String>

    private lateinit var resume: Resume

    private var educations = mutableListOf<Education>()
    private var workExperiences = mutableListOf<WorkExperience>()
    private var projects = mutableListOf<Project>()

    private val presenter: ResumeFormPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.view = this

        setupActionBar()
        setupLaunchers()
        setupInitialData()
        setupViews()
        renderResumeData()
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
                project?.let { addOrUpdateProject(it) }
            }

        workExperienceFormResultLauncher =
            registerForActivityResult(WorkExperienceFormResultContract()) { workExperience ->
                workExperience?.let { addOrUpdateWorkExperience(it) }
            }

        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted) {
                    return@registerForActivityResult
                }
                launchCamera()
            }

        requestReadExternalStoragePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted) {
                    return@registerForActivityResult
                }
                launchGallery()
            }

        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    Log.d(TAG, "Photo saved successfully")
                    resume.profilePicturePath = presenter.currentImageFile?.absolutePath ?: ""
                    renderProfileImage()
                } else {
                    presenter.removeCurrentTempImageFile()
                }
            }

        selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it == null) {
                Log.d(TAG, "selection is cancelled")
                return@registerForActivityResult
            }
            presenter.createLocalImageFile(this@ResumeFormActivity, it)
            resume.profilePicturePath = presenter.currentImageFile?.absolutePath ?: ""
            renderProfileImage()
        }
    }

    private fun setupInitialData() {
        resume = intent.getParcelableExtra(EXTRA_RESUME) ?: Resume()
        presenter.loadRelatedData(resume)
    }

    override fun onRelatedDataLoaded(
        educations: List<Education>,
        workExperiences: List<WorkExperience>,
        projects: List<Project>
    ) {
        this.educations.addAll(educations)
        this.workExperiences.addAll(workExperiences)
        this.projects.addAll(projects)
        renderRelatedData()
    }

    override fun onLoadRelatedDataFailed(e: Exception) {
        Log.e(TAG, "Cannot load related data", e)
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

    private fun renderResumeData() {
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
            binding.roleEdt.setText(it.role)
            if (it.totalYearsOfExp != -1) {
                binding.yearsOfExpEdt.setText(it.totalYearsOfExp.toString())
            }
            binding.careerObjectiveEdt.setText(it.careerObjective)
        }

        resume.skillsHolder.skills.forEach { addSkillChip(it) }

        renderProfileImage()
    }

    private fun renderProfileImage() {
        resume.profilePicturePath.let {
            if (it.isEmpty() || it.isBlank()) {
                return@let
            }
            Glide.with(this)
                .load(File(it))
                .into(binding.profilePictureImv)
        }
    }

    private fun renderRelatedData() {
        educations.forEach { addEducationPreview(it) }
        workExperiences.forEach { addWorkExperiencePreview(it) }
        projects.forEach { addProjectPreview(it) }
    }

    private fun selectOrTakePicture() {
        val bottomSheet = SelectImageSourceBottomSheet()
        bottomSheet.onOptionSelected = { dialog, option ->
            when (option) {
                SelectImageSourceBottomSheet.OPTION_CAMERA -> launchCamera()
                SelectImageSourceBottomSheet.OPTION_GALLERY -> launchGallery()
            }
            dialog.dismiss()
        }
        bottomSheet.show(supportFragmentManager, SelectImageSourceBottomSheet.TAG)
    }

    private fun launchCamera() {
        val permissionGranted = presenter.isPermissionGranted(this, Manifest.permission.CAMERA)
        if (!permissionGranted) {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            return
        }

        presenter.createTempImageFile(this)
        val uri = presenter.getCurrentImageFileUri(this)
        takePictureLauncher.launch(uri)
    }

    private fun launchGallery() {
        val permissionGranted =
            presenter.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!permissionGranted) {
            requestReadExternalStoragePermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            return
        }
        selectImageLauncher.launch("image/*")
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
        addEducationPreview(education)
    }

    private fun addEducationPreview(education: Education) {
        val preview = EducationPreviewView(this).apply {
            this.education = education
            onEditEducation = { launchEducationFormActivity(it) }
            onDeleteEducation = {
                presenter.deleteEducation(education)
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
        addWorkExperiencePreview(workExperience)
    }

    private fun addWorkExperiencePreview(workExperience: WorkExperience) {
        val preview = WorkExperiencePreviewView(this).apply {
            this.workExperience = workExperience
            onEditWorkExperience = { launchWorkExperienceFormActivity(it) }
            onDeleteWorkExperience = {
                presenter.deleteWorkExperience(workExperience)
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
        addProjectPreview(project)
    }

    private fun addProjectPreview(project: Project) {
        val preview = ProjectPreviewView(this).apply {
            this.project = project
            onEditProject = { launchProjectFormActivity(it) }
            onDeleteProject = {
                presenter.deleteProject(project)
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
        val email = binding.emailEdt.text.toString()
        val street = binding.addressEdt.text.toString()
        val city = binding.cityEdt.text.toString()
        val state = binding.stateEdt.text.toString()
        val country = binding.countryEdt.text.toString()
        val postalCode = binding.postalCodeEdt.text.toString()
        val role = binding.roleEdt.text.toString()
        val yearOfExpStr = binding.yearsOfExpEdt.text.toString()
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

        if (email.isBlank() || email.isEmpty()) {
            binding.emailEdt.error = getString(R.string.error_required)
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

        if (state.isBlank() || state.isEmpty()) {
            binding.stateEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (country.isBlank() || country.isEmpty()) {
            binding.countryEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (postalCode.isBlank() || postalCode.isEmpty()) {
            binding.postalCodeEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (role.isBlank() || role.isEmpty()) {
            binding.roleEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (yearOfExpStr.isBlank() || yearOfExpStr.isEmpty()) {
            binding.yearsOfExpEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (obj.isBlank() || obj.isEmpty()) {
            binding.careerObjectiveEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (!isValid) {
            return
        }


        resume.let {
            it.firstName = firstName
            it.lastName = lastName
            it.phoneNumber = mobile
            it.emailAddress = email
            it.address = Address(
                street = street,
                city = city,
                state = state,
                country = country,
                postalCode = postalCode
            )
            it.role = role
            it.totalYearsOfExp = yearOfExpStr.toInt()
            it.careerObjective = obj
        }

        presenter.saveData(resume, educations, workExperiences, projects)
    }

    override fun onDataSaved() {
        finish()
    }

    override fun onSaveDataFailed(e: Exception) {
        Log.e(TAG, "Cannot save data", e)
    }

    companion object {
        private const val TAG = "ResumeFormActivity"
        const val EXTRA_RESUME = "extra_resume"

        fun newIntent(context: Context, resume: Resume? = null): Intent =
            Intent(context, ResumeFormActivity::class.java).apply {
                resume?.let { putExtra(EXTRA_RESUME, it) }
            }
    }
}