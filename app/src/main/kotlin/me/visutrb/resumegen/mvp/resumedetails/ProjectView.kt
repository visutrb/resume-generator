package me.visutrb.resumegen.mvp.resumedetails

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.material.chip.Chip
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ViewProjectBinding
import me.visutrb.resumegen.entity.Project

class ProjectView : FrameLayout {

    private lateinit var binding: ViewProjectBinding

    var project: Project? = null
        set(value) {
            field = value
            renderProject()
            renderTechnologies()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleInt: Int) : super(
        context,
        attrs,
        defStyleInt
    )

    init {
        setupViews()
    }

    private fun setupViews() {
        val layoutInflater = LayoutInflater.from(context)
        binding = ViewProjectBinding.inflate(layoutInflater)
        addView(binding.root)
    }

    private fun renderProject() {
        project?.let {
            binding.projectNameTv.text = it.name
            binding.roleTv.text = it.role
            binding.summaryTv.text = it.summary
            binding.teamSizeTv.text = resources.getString(
                R.string.project_preview_team_size,
                it.teamSize
            )
        }
    }

    private fun renderTechnologies() {
        val technologies = project?.technologiesHolder?.technologies ?: return
        val chipGroup = binding.technologiesChipGroup
        chipGroup.removeAllViews()
        for (technology in technologies) {
            val chip = Chip(context).apply {
                text = technology.name
            }
            chipGroup.addView(chip)
        }
    }
}