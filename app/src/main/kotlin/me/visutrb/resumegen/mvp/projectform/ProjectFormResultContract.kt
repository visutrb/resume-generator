package me.visutrb.resumegen.mvp.projectform

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import me.visutrb.resumegen.entity.Project

class ProjectFormResultContract : ActivityResultContract<Project?, Project?>() {

    override fun createIntent(context: Context, input: Project?): Intent {
        return ProjectFormActivity.newIntent(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Project? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.getParcelableExtra(ProjectFormActivity.EXTRA_PROJECT)
    }
}