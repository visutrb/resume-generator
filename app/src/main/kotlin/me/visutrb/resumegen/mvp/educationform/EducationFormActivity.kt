package me.visutrb.resumegen.mvp.educationform

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ActivityEducationFormBinding
import java.util.*

class EducationFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEducationFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEducationFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDegreeInput()
        setupGraduationYearInput()
    }

    private fun setupDegreeInput() {
        val degrees = resources.getStringArray(R.array.education_form_degrees)
        val adapter = ArrayAdapter(this, R.layout.simple_list_item, degrees)
        binding.degreeTv.setAdapter(adapter)
    }

    private fun setupGraduationYearInput() {
        val currentYear = Calendar.getInstance().apply {
            time = Date()
        }.get(Calendar.YEAR)
        val years = (currentYear downTo 1970).toList()
        val adapter = ArrayAdapter(this, R.layout.simple_list_item, years)
        binding.graduateYearTv.setAdapter(adapter)
    }
}