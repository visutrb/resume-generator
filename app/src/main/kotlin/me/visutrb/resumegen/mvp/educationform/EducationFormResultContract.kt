package me.visutrb.resumegen.mvp.educationform

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import me.visutrb.resumegen.entity.Education

class EducationFormResultContract : ActivityResultContract<Education?, Education?>() {

    override fun createIntent(context: Context, input: Education?): Intent {
        return EducationFormActivity.newIntent(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Education? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.let { it.getParcelableExtra(EducationFormActivity.EXTRA_EDUCATION) as Education? }
    }
}