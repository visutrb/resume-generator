package me.visutrb.resumegen.mvp.workexpform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.visutrb.resumegen.databinding.ActivityWorkExperienceFormBinding

class WorkExperienceFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkExperienceFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkExperienceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}