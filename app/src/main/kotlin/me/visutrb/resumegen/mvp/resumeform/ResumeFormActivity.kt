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
                Log.d(TAG, "Received education: $education")
            }

        projectFormResultLauncher =
            registerForActivityResult(ProjectFormResultContract()) { project ->
                Log.d(TAG, "Received project: $project")
            }

        workExperienceFormResultLauncher =
            registerForActivityResult(WorkExperienceFormResultContract()) { workExperience ->
                Log.d(TAG, "Received workExperience: $workExperience")
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

        resume.skillsHolder.skills.forEach { }
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

    private fun launchEducationFormActivity() {
        educationFormResultLauncher.launch(null)
    }

    private fun launchProjectFormActivity() {
        projectFormResultLauncher.launch(null)
    }

    private fun launchWorkExperienceFormActivity() {
        workExperienceFormResultLauncher.launch(null)
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