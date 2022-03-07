package me.visutrb.resumegen.mvp.resumeform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import me.visutrb.resumegen.databinding.ActivityResumeFormBinding

class ResumeFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResumeFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profilePictureImv.setOnClickListener {
            selectOrTakePicture()
        }

        binding.mobileNumberEdt.apply {
            addTextChangedListener(PhoneNumberFormattingTextWatcher())
        }

        binding.skillEdt.apply {
            setOnEditorActionListener { view, actionId, event ->
                if (view.text.isNotEmpty() && actionId == EditorInfo.IME_ACTION_NEXT) {
                    addSkill()
                }
                true
            }
            setOnKeyListener { view, keyCode, event ->
                if ((view as EditText).text.isNotEmpty() && keyCode == KeyEvent.KEYCODE_ENTER) {
                    addSkill()
                }
                true
            }
        }

        binding.addEducationBtn.apply {
            setOnClickListener {  }
        }
    }

    private fun selectOrTakePicture() {
        Log.d(TAG, "selectOrTakePicture")
    }

    private fun addSkill() {
        val skill = binding.skillEdt.text.toString()
        if (skill.isBlank()) {
            return
        }
        binding.skillEdt.setText("")
        val chip = Chip(this)
        chip.text = skill
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            binding.skillsChipGroup.removeView(it)
        }
        binding.skillsChipGroup.addView(chip)
    }

    private fun launchEducationFormActivity() {
    }

    companion object {
        private const val TAG = "ResumeFormActivity"
        fun newIntent(context: Context): Intent = Intent(context, ResumeFormActivity::class.java)
    }
}