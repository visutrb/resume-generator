package me.visutrb.resumegen.mvp.resumelist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import me.visutrb.resumegen.databinding.ActivityResumeListBinding
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.mvp.Activity
import me.visutrb.resumegen.mvp.resumeform.ResumeFormActivity
import org.koin.android.ext.android.inject

class ResumeListActivity : Activity(), ResumeListPresenter.View {

    private lateinit var binding: ActivityResumeListBinding
    private lateinit var recyclerViewAdapter: ResumeListRecyclerViewAdapter

    private val presenter: ResumeListPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.view = this

        recyclerViewAdapter = ResumeListRecyclerViewAdapter().apply {
            onResumeSelected = { launchResumeFormActivity(it) }
        }

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
        binding.noResumeTv.visibility = when (resumes.isEmpty()) {
            true -> View.VISIBLE
            false -> View.GONE
        }
        recyclerViewAdapter.replaceAll(resumes)
    }

    private fun launchResumeFormActivity(resume: Resume? = null) {
        val intent = ResumeFormActivity.newIntent(this, resume)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "ResumeListActivity"
    }
}