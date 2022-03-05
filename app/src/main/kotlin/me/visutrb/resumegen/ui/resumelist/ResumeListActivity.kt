package me.visutrb.resumegen.ui.resumelist

import android.os.Bundle
import me.visutrb.resumegen.databinding.ActivityResumeListBinding
import me.visutrb.resumegen.ui.BaseActivity

class ResumeListActivity : BaseActivity() {

    private lateinit var binding: ActivityResumeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}