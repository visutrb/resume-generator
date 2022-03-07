package me.visutrb.resumegen.mvp.projectform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import me.visutrb.resumegen.databinding.ActivityProjectFormBinding
import me.visutrb.resumegen.entity.Project

class ProjectFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    companion object {
        const val EXTRA_PROJECT = "extra_project"

        fun newIntent(context: Context, project: Project?): Intent {
            return Intent(context, ProjectFormActivity::class.java).apply {
                putExtra(EXTRA_PROJECT, project)
            }
        }
    }
}