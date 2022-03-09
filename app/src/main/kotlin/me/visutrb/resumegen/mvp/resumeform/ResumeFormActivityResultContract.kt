package me.visutrb.resumegen.mvp.resumeform

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import me.visutrb.resumegen.entity.Education
import me.visutrb.resumegen.entity.Project
import me.visutrb.resumegen.entity.Resume
import me.visutrb.resumegen.entity.WorkExperience

class ResumeFormActivityResultContract : ActivityResultContract<ResumeFormData, ResumeFormData?>() {

    override fun createIntent(context: Context, input: ResumeFormData?): Intent {
        return ResumeFormActivity.newIntent(
            context,
            input?.resume,
            input?.educations ?: listOf(),
            input?.workExperiences ?: listOf(),
            input?.projects ?: listOf()
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ResumeFormData? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        val resume: Resume? = intent?.getParcelableExtra(ResumeFormActivity.EXTRA_RESUME)
        val educations =
            intent?.getParcelableArrayExtra(ResumeFormActivity.EXTRA_EDUCATIONS)?.toList() as List<Education>?
        val workExperiences =
            intent?.getParcelableArrayExtra(ResumeFormActivity.EXTRA_WORK_EXPERIENCES)?.toList() as List<WorkExperience>?
        val projects =
            intent?.getParcelableArrayExtra(ResumeFormActivity.EXTRA_PROJECTS)?.toList() as List<Project>?

        return ResumeFormData(
            resume,
            educations ?: listOf(),
            workExperiences ?: listOf(),
            projects ?: listOf()
        )
    }
}