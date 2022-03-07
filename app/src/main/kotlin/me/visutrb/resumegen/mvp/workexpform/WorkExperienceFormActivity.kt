package me.visutrb.resumegen.mvp.workexpform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import me.visutrb.resumegen.R
import me.visutrb.resumegen.databinding.ActivityWorkExperienceFormBinding
import me.visutrb.resumegen.entity.WorkExperience
import java.util.*

class WorkExperienceFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkExperienceFormBinding

    private var initialWorkExperience: WorkExperience? = null

    private lateinit var months: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkExperienceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupInitialData()
        setupMonthInputData()
        setupYearInputData()
        setupViews()
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
        months = resources.getStringArray(R.array.months)

        if (!intent.hasExtra(EXTRA_WORK_EXPERIENCE)) {
            return
        }

        initialWorkExperience = intent.getParcelableExtra(EXTRA_WORK_EXPERIENCE)
        initialWorkExperience?.let {
            binding.companyEdt.setText(it.companyName)
            binding.roleEdt.setText(it.role)
            binding.startMonthEdt.setText(intToMonth(it.startMonth))
            binding.startYearEdt.setText(it.startYear.toString())

            if (it.endMonth < 0 && it.endYear < 0) {
                binding.endYearEdt.isEnabled = false
                binding.endMonthEdt.isEnabled = false
                binding.isWorkingCb.isChecked = true
            } else {
                binding.endYearEdt.setText(it.endYear.toString())
                binding.endMonthEdt.setText(intToMonth(it.startMonth))
                binding.isWorkingCb.isChecked = false
            }
        }
    }

    private fun setupMonthInputData() {
        binding.startMonthEdt.setAdapter(ArrayAdapter(this, R.layout.simple_list_item, months))
        binding.endMonthEdt.setAdapter(ArrayAdapter(this, R.layout.simple_list_item, months))
    }

    private fun setupYearInputData() {
        val currentYear = Calendar.getInstance().apply {
            time = Date()
        }.get(Calendar.YEAR)
        val years = currentYear.downTo(1970).toList()
        binding.startYearEdt.setAdapter(ArrayAdapter(this, R.layout.simple_list_item, years))
        binding.endYearEdt.setAdapter(ArrayAdapter(this, R.layout.simple_list_item, years))
    }

    private fun setupViews() {
        binding.isWorkingCb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.endYearEdt.isEnabled = false
                binding.endMonthEdt.isEnabled = false
            } else {
                binding.endYearEdt.isEnabled = true
                binding.endMonthEdt.isEnabled = true
            }
        }
    }

    private fun intToMonth(int: Int): String {
        return months[int]
    }

    private fun monthToInt(month: String): Int {
        return months.indexOf(month)
    }

    private fun validateAndSendResult() {
        var isValid = true

        val company = binding.companyEdt.text.toString()
        val role = binding.roleEdt.text.toString()
        val startMonthStr = binding.startMonthEdt.text.toString()
        val startYearStr = binding.startYearEdt.text.toString()

        val endMonthStr = binding.endMonthEdt.text.toString()
        val endYearStr = binding.endYearEdt.text.toString()

        val isCurrentlyWorking = binding.isWorkingCb.isChecked

        if (company.isEmpty() || company.isBlank()) {
            binding.companyEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (role.isEmpty() || role.isBlank()) {
            binding.roleEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (startMonthStr.isEmpty() || startMonthStr.isBlank()) {
            binding.startMonthEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (startYearStr.isEmpty() || startYearStr.isBlank()) {
            binding.startYearEdt.error = getString(R.string.error_required)
            isValid = false
        }

        if (!isCurrentlyWorking) {
            if (endMonthStr.isEmpty() || endMonthStr.isBlank()) {
                binding.endMonthEdt.error = getString(R.string.error_required)
                isValid = false
            }

            if (endYearStr.isEmpty() || endYearStr.isBlank()) {
                binding.endYearEdt.error = getString(R.string.error_required)
                isValid = false
            }
        }

        val startMonth = monthToInt(startMonthStr)
        if (startMonth !in 0..11) {
            isValid = false
            binding.startMonthEdt.error = getString(R.string.error_invalid)
        }
        val endMonth = monthToInt(endMonthStr)
        if (endMonth !in 0..11 && !isCurrentlyWorking) {
            isValid = false
            binding.endMonthEdt.error = getString(R.string.error_invalid)
        }

        val startYear = startYearStr.toInt()
        val endYear = if (isCurrentlyWorking) -1 else endYearStr.toInt()

        if (!isValid) {
            return
        }

        val workExperience = initialWorkExperience?.copy(
            companyName = company,
            role = role,
            startMonth = startMonth,
            startYear = startYear,
            endMonth = if (isCurrentlyWorking) -1 else endMonth,
            endYear = endYear,
        ) ?: WorkExperience(
            companyName = company,
            role = role,
            startMonth = startMonth,
            startYear = startYear,
            endMonth = if (isCurrentlyWorking) -1 else endMonth,
            endYear = endYear
        )
        val intent = Intent().apply {
            putExtra(EXTRA_WORK_EXPERIENCE, workExperience)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun cancelAndFinish() {
        setResult(RESULT_CANCELED)
        finish()
    }

    companion object {

        const val EXTRA_WORK_EXPERIENCE = "extra_work_experience"

        fun newIntent(context: Context, workExperience: WorkExperience?) =
            Intent(context, WorkExperienceFormActivity::class.java).apply {
                putExtra(EXTRA_WORK_EXPERIENCE, workExperience)
            }
    }
}