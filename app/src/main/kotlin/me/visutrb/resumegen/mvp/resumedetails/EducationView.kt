package me.visutrb.resumegen.mvp.resumedetails

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ViewEducationBinding
import me.visutrb.resumegen.entity.Education

class EducationView : FrameLayout {

    private lateinit var binding: ViewEducationBinding

    var education: Education? = null
        set(value) {
            field = value
            renderEducation()
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
        binding = ViewEducationBinding.inflate(layoutInflater)
        addView(binding.root)
    }

    private fun renderEducation() {
        education?.let {
            binding.schoolTv.text = it.school
            binding.fosTv.text =
                resources.getString(R.string.education_fos, it.degree, it.fos, it.graduateYear)
            binding.cgpaTv.text = resources.getString(R.string.education_cgpa, it.cgpa)
        }
    }
}