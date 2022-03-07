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

class ProjectFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectFormBinding

    private var initialProject: Project? = null

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
                }
                true
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
        if (!intent.hasExtra(EXTRA_PROJECT)) {
            return
        }
        initialProject = intent.getParcelableExtra(EXTRA_PROJECT)
        initialProject?.let {
            binding.projectNameEdt.setText(it.name)
            binding.roleEdt.setText(it.role)
            binding.teamSizeEdt.setText(it.teamSize.toString())
            binding.summaryEdt.setText(it.summary)
        }
    }

    private fun addTechnology() {
        val technology = binding.technologyEdt.text.toString()
        if (technology.isBlank()) {
            return
        }

        binding.technologyEdt.setText("")
        val chip = Chip(this).apply {
            text = technology
            isCloseIconVisible = true
            setOnCloseIconClickListener {
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

        val project = initialProject?.copy(
            name = projectName,
            role = role,
            teamSize = teamSize,
            summary = summary

        ) ?: Project(
            name = projectName,
            role = role,
            teamSize = teamSize,
            summary = summary
        )

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