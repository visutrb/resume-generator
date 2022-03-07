package me.visutrb.resumegen.mvp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class Presenter<View : Presenter.View> : CoroutineScope {

    protected val job = Job()
    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    var view: View? = null

    interface View
}