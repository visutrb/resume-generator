package me.visutrb.resumegen.mvp.workexpform

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import me.visutrb.resumegen.entity.WorkExperience

class WorkExperienceFormResultContract :
    ActivityResultContract<WorkExperience?, WorkExperience?>() {

    override fun createIntent(context: Context, input: WorkExperience?): Intent {
        return WorkExperienceFormActivity.newIntent(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): WorkExperience? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.getParcelableExtra(WorkExperienceFormActivity.EXTRA_WORK_EXPERIENCE)
    }
}