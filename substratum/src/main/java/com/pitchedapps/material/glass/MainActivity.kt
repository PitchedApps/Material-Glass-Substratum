package com.pitchedapps.material.glass

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import ca.allanwang.kau.adapters.FastItemThemedAdapter
import ca.allanwang.kau.animators.KauAnimator
import ca.allanwang.kau.email.sendEmail
import ca.allanwang.kau.iitems.CardIItem
import ca.allanwang.kau.utils.*
import com.mikepenz.fastadapter.IItem
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import kotlinx.android.synthetic.main.activity_main.*
import projekt.substrate.SubstratumLoader

/**
 * Created by Allan Wang on 2017-07-10.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val SUBSTRATUM_PACKAGE = "projekt.substratum"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fastAdapter = FastItemThemedAdapter<IItem<*, *>>(textColor = color(R.color.text), backgroundColor = color(R.color.card))
        CardIItem.bindClickEvents(fastAdapter)
        with(recycler) {
            adapter = fastAdapter
            itemAnimator = KauAnimator(MGSlideAdd()).apply {
                addDuration = 500
                interpolator = AnimationUtils.loadInterpolator(this@MainActivity, android.R.interpolator.decelerate_quad)
            }
        }
        statusBarColor = 0x30000000
        toolbar.setTitle(R.string.ThemeName)
        fab.apply {
            setIcon(GoogleMaterial.Icon.gmd_format_paint)
            setOnClickListener {
                if (!isAppInstalled(SUBSTRATUM_PACKAGE)) {
                    toast(R.string.toast_substratum)
                    startPlayStoreLink(SUBSTRATUM_PACKAGE)
                } else {
                    if (isAppEnabled(SUBSTRATUM_PACKAGE)) {
                        val intent = SubstratumLoader.launchThemeActivity(applicationContext,
                                intent, getString(R.string.ThemeName), packageName)
                        startActivity(intent)
                        finish()
                    } else {
                        toast(R.string.toast_substratum_frozen)
                    }
                }
            }
        }
        var logoHidden = false
        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percentage = Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            if (percentage > 0.7f && !logoHidden) {
                logoHidden = true
                logo.fadeOut()
            } else if (percentage <= 0.7f && logoHidden) {
                logoHidden = false
                logo.fadeIn()
            }
        }
        postDelayed(500) {
            backdrop.circularReveal {
                fastAdapter.add(listOf(
                        CardIItem {
                            titleRes = R.string.main_title
                            desc = String.format(string(R.string.main_desc), string(R.string.ThemeName))
                        },
                        CardIItem {
                            titleRes = R.string.xda_thread
                            cardClick = { startLink(string(R.string.xda_link)) }
                        },
                        CardIItem {
                            titleRes = R.string.proudly_open_sourced
                            cardClick = { startLink(string(R.string.github_url)) }
                        },
                        CardIItem {
                            titleRes = R.string.contact_dev
                            cardClick = {
                                sendEmail(R.string.email_dev, R.string.email_subject) {
                                    checkPackage(SUBSTRATUM_PACKAGE, "Substratum")
                                }
                            }
                        }
                ))
            }
            postDelayed(1000) {
                fab.show()
            }
        }
    }
}