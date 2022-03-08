package me.visutrb.resumegen.mvp.resumeform

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ViewEducationPreviewBinding
import me.visutrb.resumegen.entity.Education
import java.util.*

class EducationPreviewView : FrameLayout {

    private var binding: ViewEducationPreviewBinding

    var education: Education? = null
        set(value) {
            field = value
            updateViews()
        }

    var onEditEducation: ((education: Education) -> Unit)? = null
    var onDeleteEducation: ((education: Education) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleInt: Int) : super(
        context,
        attrs,
        defStyleInt
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewEducationPreviewBinding.inflate(inflater)
        addView(binding.root)
        updateViews()
        setupButtons()
    }

    private fun updateViews() {
        education?.let {
            binding.schoolTv.text = it.school
            binding.fosTv.text =
                resources.getString(R.string.education_preview_fos, it.degree, it.fos, it.graduateYear)
            binding.cgpaTv.text = resources.getString(R.string.education_preview_cgpa, it.cgpa)
        }
    }

    private fun setupButtons() {
        binding.editBtn.setOnClickListener {
            education?.let { ed -> onEditEducation?.invoke(ed) }
        }

        binding.deleteBtn.setOnClickListener {
            education?.let { ed -> onDeleteEducation?.invoke(ed) }
        }
    }
}