package me.visutrb.resumegen.mvp.resumelist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import me.visutrb.resumegen.databinding.ActivityResumeListBinding
import me.visutrb.resumegen.mvp.BaseActivity

class ResumeListActivity : BaseActivity() {

    private lateinit var binding: ActivityResumeListBinding
    private lateinit var recyclerViewAdapter: ResumeListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewAdapter = ResumeListRecyclerViewAdapter()

        binding.resumeRv.apply {
            adapter = recyclerViewAdapter
            layoutManager = GridLayoutManager(this@ResumeListActivity, 2)
        }

        binding.createResumeBtn.setOnClickListener {
            TODO("Start new resume activity")
        }

        binding.noResumeTv.visibility = View.VISIBLE
    }
}