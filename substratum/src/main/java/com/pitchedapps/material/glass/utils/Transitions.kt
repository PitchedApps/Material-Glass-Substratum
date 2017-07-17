package com.pitchedapps.material.glass.utils

import android.transition.Transition

/**
 * Created by Allan Wang on 2017-07-10.
 */
class TransitionEndListener(val onEnd: (transition: Transition) -> Unit) : Transition.TransitionListener {
    override fun onTransitionEnd(transition: Transition) = onEnd(transition)
    override fun onTransitionResume(transition: Transition) {}
    override fun onTransitionPause(transition: Transition) {}
    override fun onTransitionCancel(transition: Transition) {}
    override fun onTransitionStart(transition: Transition) {}
}

fun Transition.addEndListener(onEnd: (transition: Transition) -> Unit) {
    addListener(TransitionEndListener(onEnd))
}