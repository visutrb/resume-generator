package me.visutrb.resumegen.mvp.projectform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ActivityProjectFormBinding
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.TechnologiesHolder
import me.visutrb.resumegen.entity.Technology

class ProjectFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectFormBinding

    private lateinit var project: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupInitialData()

        binding.technologyEdt.apply {
            setOnEditorActionListener { view, actionId, event ->
                if (view.text.isNotEmpty() && actionId == EditorInfo.IME_ACTION_NEXT) {
                    addTechnology()
                }
                true
            }
            setOnKeyListener { view, keyCode, event ->
                if ((view as EditText).text.isNotEmpty() && keyCode == KeyEvent.KEYCODE_ENTER) {
                    addTechnology()
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_form_save -> {
                validateAndSendResult()
                true
            }
            android.R.id.home -> {
                cancelAndFinish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_close_filled)
            it.title = getString(R.string.project_form_title)
        }
    }

    private fun setupInitialData() {
        project = intent.getParcelableExtra(EXTRA_PROJECT) ?: Project()
        project.let {
            binding.projectNameEdt.setText(it.name)
            binding.roleEdt.setText(it.role)
            if (it.teamSize != 0) {
                binding.teamSizeEdt.setText(it.teamSize.toString())
            }
            binding.summaryEdt.setText(it.summary)
        }
        project.technologiesHolder.technologies.forEach { addChip(it) }
    }

    private fun addTechnology() {
        val technologyName = binding.technologyEdt.text.toString()
        if (technologyName.isBlank()) {
            return
        }

        binding.technologyEdt.setText("")

        val technology = Technology(technologyName)
        project.technologiesHolder.technologies.add(technology)

        addChip(technology)
    }

    private fun addChip(technology: Technology) {
        val chip = Chip(this).apply {
            text = technology.name
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                project.technologiesHolder.technologies.remove(technology)
                binding.technologiesChipGroup.removeView(it)
            }
        }
        binding.technologiesChipGroup.addView(chip)
    }

    private fun validateAndSendResult() {
        val projectName = binding.projectNameEdt.text.toString()
        val role = binding.roleEdt.text.toString()
        val teamSizeStr = binding.teamSizeEdt.text.toString()
        val summary = binding.summaryEdt.text.toString()

        var isValid = true
        if (projectName.isEmpty() || projectName.isBlank()) {
            isValid = false
            binding.projectNameEdt.error = getString(R.string.error_required)
        }

        if (role.isEmpty() || role.isBlank()) {
            isValid = false
            binding.roleEdt.error = getString(R.string.error_required)
        }

        if (teamSizeStr.isEmpty() || teamSizeStr.isBlank()) {
            isValid = false
            binding.teamSizeEdt.error = getString(R.string.error_required)
        }

        if (summary.isEmpty() || projectName.isBlank()) {
            isValid = false
            binding.summaryEdt.error = getString(R.string.error_required)
        }

        if (!isValid) {
            return
        }

        val teamSize = teamSizeStr.toInt()

        project.let {
            it.name = projectName
            it.role = role
            it.teamSize = teamSize
            it.summary = summary
        }

        val intent = Intent().apply {
            putExtra(EXTRA_PROJECT, project)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun cancelAndFinish() {
        setResult(RESULT_CANCELED)
        finish()
    }

    companion object {
        const val EXTRA_PROJECT = "extra_project"

        fun newIntent(context: Context, project: Project?): Intent {
            return Intent(context, ProjectFormActivity::class.java).apply {
                putExtra(EXTRA_PROJECT, project)
            }
        }
    }
}