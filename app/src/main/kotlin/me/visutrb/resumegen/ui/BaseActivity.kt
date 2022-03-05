package me.visutrb.resumegen.ui

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), CoroutineScope, AndroidScopeComponent {
    protected val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    override val scope: Scope by activityScope()
}