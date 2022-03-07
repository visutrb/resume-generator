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
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ActivityResumeFormBinding
import me.visutrb.resumegen.entity.Education
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.entity.WorkExperience
import me.visutrb.resumegen.mvp.Activity
import me.visutrb.resumegen.mvp.educationform.EducationFormResultContract
import me.visutrb.resumegen.mvp.projectform.ProjectFormResultContract
import me.visutrb.resumegen.mvp.workexpform.WorkExperienceFormResultContract

class ResumeFormActivity : Activity() {

    private lateinit var binding: ActivityResumeFormBinding

    private lateinit var educationFormResultLauncher: ActivityResultLauncher<Education?>
    private lateinit var projectFormResultLauncher: ActivityResultLauncher<Project?>
    private lateinit var workExperienceFormResultLauncher: ActivityResultLauncher<WorkExperience?>

    private var initialResume: Resume? = null

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
                }
                true
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
        val skill = binding.skillEdt.text.toString()
        if (skill.isBlank()) {
            return
        }
        binding.skillEdt.setText("")
        val chip = Chip(this)
        chip.text = skill
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            binding.skillsChipGroup.removeView(it)
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

    companion object {
        private const val TAG = "ResumeFormActivity"
        fun newIntent(context: Context): Intent = Intent(context, ResumeFormActivity::class.java)
    }
}