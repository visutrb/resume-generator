package me.visutrb.resumegen.mvp.resumeform

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.material.chip.Chip
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ViewProjectPreviewBinding
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Technology

class ProjectPreviewView : FrameLayout {

    private var binding: ViewProjectPreviewBinding

    var project: Project? = null
        set(value) {
            field = value
            updateViews()
        }

    var onEditProject: ((project: Project) -> Unit)? = null
    var onDeleteProject: ((project: Project) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleInt: Int) : super(
        context,
        attrs,
        defStyleInt
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewProjectPreviewBinding.inflate(inflater)
        binding.editBtn.setOnClickListener { project?.let { onEditProject?.invoke(it) } }
        binding.deleteBtn.setOnClickListener { project?.let { onDeleteProject?.invoke(it) } }
        addView(binding.root)
    }

    private fun updateViews() {
        project?.let {
            binding.projectNameTv.text = it.name
            binding.roleTv.text = resources.getString(R.string.project_preview_role, it.role)
            binding.teamSizeTv.text =
                resources.getString(R.string.project_preview_team_size, it.teamSize)
            binding.summaryTv.text = it.summary
        }
        binding.technologiesChipGroup.removeAllViews()
        project?.technologiesHolder?.technologies?.forEach { addChip(it) }
    }

    private fun addChip(technology: Technology) {
        val chip = Chip(context).apply {
            text = technology.name
        }
        binding.technologiesChipGroup.addView(chip)
    }
}