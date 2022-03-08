package me.visutrb.resumegen.mvp.resumeform

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ViewWorkExperiencePreviewBinding
import me.visutrb.resumegen.entity.WorkExperience

class WorkExperiencePreviewView : FrameLayout {

    private var binding: ViewWorkExperiencePreviewBinding

    private var months = arrayOf<String>()

    var workExperience: WorkExperience? = null
        set(value) {
            field = value
            updateViews()
        }

    var onEditWorkExperience: ((workExperience: WorkExperience) -> Unit)? = null
    var onDeleteWorkExperience: ((workExperience: WorkExperience) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleInt: Int) : super(
        context,
        attrs,
        defStyleInt
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewWorkExperiencePreviewBinding.inflate(inflater)
        addView(binding.root)

        updateViews()
        setupButtons()
    }

    private fun updateViews() {
        if (months.isEmpty()) {
            months = context.resources.getStringArray(R.array.months)
        }

        workExperience?.let {
            binding.companyTv.text = workExperience?.companyName
            binding.roleTv.text = workExperience?.role
            binding.periodTv.text = workExperience?.let {
                if (it.startMonth != -1 && it.startYear != -1) {
                    if (it.isCurrentlyEmployed) {
                        "${months[it.startMonth]} ${it.startYear} - Present"
                    } else {
                        "${months[it.startMonth]} ${it.startYear} - ${months[it.endMonth]} ${it.endYear}"
                    }
                } else {
                    ""
                }
            }
        }
    }

    private fun setupButtons() {
        binding.editBtn.setOnClickListener {
            workExperience?.let { wx -> onEditWorkExperience?.invoke(wx) }
        }

        binding.deleteBtn.setOnClickListener {
            workExperience?.let { wx -> onDeleteWorkExperience?.invoke(wx) }
        }
    }
}