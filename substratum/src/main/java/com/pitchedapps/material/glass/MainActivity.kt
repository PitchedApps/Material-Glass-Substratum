package com.pitchedapps.material.glass

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import ca.allanwang.kau.adapters.FastItemThemedAdapter
import ca.allanwang.kau.animators.KauAnimator
import ca.allanwang.kau.animators.SlideAnimatorAdd
import ca.allanwang.kau.iitems.CardIItem
import ca.allanwang.kau.utils.*
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Allan Wang on 2017-07-10.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fastAdapter = FastItemThemedAdapter<CardIItem>(textColor = color(R.color.text), backgroundColor = color(R.color.card))
        with(recycler) {
            adapter = fastAdapter
            itemAnimator = KauAnimator(SlideAnimatorAdd(KAU_BOTTOM, slideFactor = 3f)).apply {
                addDuration = 500
                interpolator = AnimationUtils.loadInterpolator(this@MainActivity, android.R.interpolator.decelerate_quad)
            }
        }
        toolbar.setTitle(R.string.ThemeName)
        fab.apply {
            setIcon(GoogleMaterial.Icon.gmd_format_paint)
            setBackgroundColor(R.color.fab)
        }
        var logoHidden = false
        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percentage = Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            if (percentage > 0.75f && !logoHidden) {
                logoHidden = true
                logo.fadeOut()
            } else if (percentage <= 0.75f && logoHidden) {
                logoHidden = false
                logo.fadeIn()
            }
        }
        postDelayed(500) {
            backdrop.circularReveal {
                fastAdapter.add(listOf(
                        CardIItem { title = "TEST TEST TEST" },
                        CardIItem { title = "TEST TEST TEST" },
                        CardIItem { title = "TEST TEST TEST" },
                        CardIItem { title = "TEST TEST TEST" },
                        CardIItem { title = "TEST TEST TEST" },
                        CardIItem { title = "TEST TEST TEST" },
                        CardIItem { title = "TEST TEST TEST" },
                        CardIItem { title = "TEST TEST TEST" },
                        CardIItem { title = "TEST TEST TEST" }
                ))
            }
            postDelayed(1000) {
                fab.show()
            }
        }
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