package com.pitchedapps.material.glass

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewPropertyAnimator
import ca.allanwang.kau.animators.KauAnimatorAdd
import ca.allanwang.kau.utils.dpToPx

/**
 * Created by Allan Wang on 2017-07-17.
 */
class MGSlideAdd(override var itemDelayFactor: Float = 0.125f) : KauAnimatorAdd {

    override fun animationPrepare(holder: RecyclerView.ViewHolder): View.() -> Unit = {
        translationY = 500f.dpToPx
        alpha = 0f
    }

    override fun animation(holder: RecyclerView.ViewHolder): ViewPropertyAnimator.() -> Unit = {
        translationY(0f)
        translationX(0f)
        alpha(1f)
    }

    override fun animationCleanup(holder: RecyclerView.ViewHolder): View.() -> Unit = {
        translationY = 0f
        translationX = 0f
        alpha = 1f
    }

    override fun getDelay(remove: Long, move: Long, change: Long): Long = 0L

}