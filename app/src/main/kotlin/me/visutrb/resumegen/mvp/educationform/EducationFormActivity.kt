package me.visutrb.resumegen.mvp.educationform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ActivityEducationFormBinding
import me.visutrb.resumegen.entity.Education
import java.util.*

class EducationFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEducationFormBinding

    private var initialEducation: Education? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEducationFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupInitialData()
        setupDegreeInput()
        setupGraduationYearInput()
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
        val actionBar = supportActionBar ?: return
        actionBar.title = getString(R.string.education_form_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_filled)
    }

    private fun setupInitialData() {
        if (!intent.hasExtra(EXTRA_EDUCATION)) {
            return
        }

        initialEducation = intent.getParcelableExtra(EXTRA_EDUCATION)
        initialEducation?.let {
            binding.schoolEdt.setText(it.school)
            binding.degreeTv.setText(it.degree)
            binding.fosEdt.setText(it.fos)
            binding.gradeEdt.setText(it.cgpa.toString())
            binding.graduateYearTv.setText(it.graduateYear.toString())
        }
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

    private fun validateAndSendResult() {
        val school = binding.schoolEdt.text.toString()
        val degree = binding.degreeTv.text.toString()
        val fos = binding.fosEdt.text.toString()
        val cgpaStr = binding.gradeEdt.text.toString()
        val graduateYearStr = binding.graduateYearTv.text.toString()

        var isInputValid = true
        if (school.isEmpty()) {
            isInputValid = false
            binding.schoolEdt.error = getString(R.string.error_required)
        }

        if (degree.isEmpty()) {
            isInputValid = false
            binding.degreeTv.error = getString(R.string.error_required)
        }

        if (fos.isEmpty()) {
            isInputValid = false
            binding.degreeTv.error = getString(R.string.error_required)
        }

        if (cgpaStr.isEmpty()) {
            isInputValid = false
            binding.gradeEdt.error = getString(R.string.error_required)
        }

        if (graduateYearStr.isEmpty()) {
            isInputValid = false
            binding.graduateYearTv.error = getString(R.string.error_required)
        }

        if (!isInputValid) {
            return
        }

        val cgpa = cgpaStr.toFloat()
        val graduateYear = graduateYearStr.toInt()

        val education = initialEducation?.copy(
            school = school,
            degree = degree,
            fos = fos,
            cgpa = cgpa,
            graduateYear = graduateYear
        ) ?: Education(
            school = school,
            degree = degree,
            fos = fos,
            cgpa = cgpa,
            graduateYear = graduateYear
        )
        val intent = Intent().apply {
            putExtra(EXTRA_EDUCATION, education)
        }
        Log.d(TAG, "Sending result: $education")
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun cancelAndFinish() {
        setResult(RESULT_CANCELED)
        finish()
    }

    companion object {
        private const val TAG = "EducationFormActivity"

        const val EXTRA_EDUCATION = "extra_education"

        fun newIntent(context: Context, education: Education?): Intent {
            return Intent(context, EducationFormActivity::class.java).apply {
                putExtra(EXTRA_EDUCATION, education)
            }
        }
    }
}