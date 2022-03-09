package me.visutrb.resumegen.mvp.resumedetails

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ViewWorkExperienceBinding
import me.visutrb.resumegen.entity.WorkExperience

class WorkExperienceView : FrameLayout {

    private lateinit var binding: ViewWorkExperienceBinding

    var workExperience: WorkExperience? = null
        set(value) {
            field = value
            renderWorkExperience()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleInt: Int) : super(
        context,
        attrs,
        defStyleInt
    )

    init {
        setupView()
    }

    private fun setupView() {
        val layoutInflater = LayoutInflater.from(context)
        binding = ViewWorkExperienceBinding.inflate(layoutInflater)
        addView(binding.root)
    }

    private fun renderWorkExperience() {
        val months = resources.getStringArray(R.array.months)
        workExperience?.let {
            binding.companyTv.text = it.companyName
            binding.roleTv.text = it.role
            binding.periodTv.text = it.let {
                if (it.startMonth != -1 && it.startYear != -1) {
                    if (it.isCurrentlyEmployed) {
                        resources.getString(
                            R.string.work_experience_preview_duration_to_present,
                            months[it.startMonth],
                            it.startYear
                        )
                    } else {
                        resources.getString(
                            R.string.work_experience_preview_duration,
                            months[it.startMonth],
                            it.startYear,
                            months[it.endMonth],
                            it.endYear
                        )
                    }
                } else {
                    ""
                }
            }
        }
    }
}