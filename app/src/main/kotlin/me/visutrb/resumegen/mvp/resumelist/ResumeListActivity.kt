package me.visutrb.resumegen.mvp.resumelist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import me.visutrb.resumegen.databinding.ActivityResumeListBinding
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.mvp.BaseActivity
import org.koin.android.ext.android.inject

class ResumeListActivity : BaseActivity(), ResumeListPresenter.View {

    private lateinit var binding: ActivityResumeListBinding
    private lateinit var recyclerViewAdapter: ResumeListRecyclerViewAdapter

    private val presenter: ResumeListPresenter by inject()

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
            launchResumeFormActivity()
        }

        binding.noResumeTv.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        presenter.loadResumes()
    }

    override fun renderResumes(resumes: List<Resume>) {
        recyclerViewAdapter.addAll(resumes)
    }

    private fun launchResumeFormActivity() {

    }

}