package com.pitchedapps.material.glass

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Allan Wang on 2017-07-10.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        animate()
        val scene1 = Scene.getSceneForLayout(scrollView, R.layout.activity_main_start, this)
        val scene2 = Scene.getSceneForLayout(scrollView, R.layout.activity_main_end, this)
//        scene1.enter()
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.main_transition)

        Handler().postDelayed({
            TransitionManager.go(scene2, transition.setDuration(1000L))
        }, 1000)
    }



//    fun animate() {
//        linear.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//            override fun onPreDraw(): Boolean {
//                linear.viewTreeObserver.removeOnPreDrawListener(this)
//                val logoOffset = (activityHeight.toFloat() / 2) - logo.y
//                logo.translationY = logoOffset
//                text.translationY = 300f
//                linear.invalidate()
//                logo.animate().translationY(0f).translationX(-200f).setDuration(1000L).setStartDelay(500).setListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationEnd(animation: Animator?) {
//                        bindScrollView()
//                    }
//                }).start()
//                return true
//            }
//        })
//
//    }

//    fun bindScrollView() {
//        val scrollView = ScrollView(this)
//        scrollView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
//        val container = linear.parent as ViewGroup
//        container.removeView(linear)
//        scrollView.addView(linear)
//        linear.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
//        container.addView(scrollView)
//        log("done")
//    }
}