package com.pitchedapps.material.glass

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import ca.allanwang.kau.adapters.FastItemThemedAdapter
import ca.allanwang.kau.animators.KauAnimator
import ca.allanwang.kau.changelog.showChangelog
import ca.allanwang.kau.email.sendEmail
import ca.allanwang.kau.iitems.CardIItem
import ca.allanwang.kau.utils.*
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fastadapter.IItem
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import kotlinx.android.synthetic.main.activity_main.*
import projekt.substrate.SubstratumLoader


/**
 * Created by Allan Wang on 2017-07-10.
 *
 * The main ui
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
        recycler.apply {
            adapter = fastAdapter
            itemAnimator = KauAnimator(MGSlideAdd()).apply {
                addDuration = 500
                interpolator = AnimationUtils.loadInterpolator(this@MainActivity, android.R.interpolator.decelerate_quad)
            }
            val divider = DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            divider.setDrawable(drawable(R.drawable.recycler_divider))
            addItemDecoration(divider)
        }
        statusBarColor = 0x30000000
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.ThemeName)
        fab.apply {
            setIcon(GoogleMaterial.Icon.gmd_format_paint)
            setOnClickListener {
                if (!isAppInstalled(SUBSTRATUM_PACKAGE)) {
                    toast(R.string.substratum_not_installed)
                    startPlayStoreLink(SUBSTRATUM_PACKAGE)
                } else {
                    if (isAppEnabled(SUBSTRATUM_PACKAGE)) {
                        val intent = SubstratumLoader.launchThemeActivity(applicationContext,
                                intent, string(R.string.ThemeName), packageName)
                        startActivity(intent)
                        finish()
                    } else {
                        materialDialog {
                            title(R.string.kau_error)
                            content(R.string.substratum_frozen)
                            positiveText(R.string.kau_ok)
                        }
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
                val items = mutableListOf(
                        CardIItem {
                            titleRes = R.string.main_title
                            desc = String.format(string(R.string.main_desc), string(R.string.ThemeName))
                        },
                        CardIItem {
                            titleRes = R.string.join_beta
                            descRes = R.string.join_beta_desc
                            cardClick = { startLink(string(R.string.beta_url)) }
                            imageIIcon = CommunityMaterial.Icon.cmd_beta
                        },
                        CardIItem {
                            titleRes = R.string.xda_thread
                            descRes = R.string.xda_thread_desc
                            cardClick = { startLink(string(R.string.xda_url)) }
                            imageIIcon = CommunityMaterial.Icon.cmd_xda
                        },
                        CardIItem {
                            titleRes = R.string.open_sourced
                            descRes = R.string.open_sourced_desc
                            cardClick = { startLink(string(R.string.github_url)) }
                            imageIIcon = CommunityMaterial.Icon.cmd_github_circle
                        },
                        CardIItem {
                            titleRes = R.string.contact_dev
                            descRes = R.string.contact_dev_desc
                            cardClick = {
                                sendEmail(string(R.string.email_dev), string(R.string.ThemeName) + " Support") {
                                    checkPackage(SUBSTRATUM_PACKAGE, "Substratum")
                                    addItem("Random ID", Prefs.randomId)
                                }
                            }
                            imageIIcon = CommunityMaterial.Icon.cmd_email
                        },
                        CardIItem {
                            titleRes = R.string.mg_legacy
                            descRes = R.string.mg_legacy_desc
                            cardClick = { startLink(string(R.string.mg_legacy_url)) }
                        }
                )
                fastAdapter.add(items)
            }
            postDelayed(1000) { fab.show() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        setMenuIcons(menu, Color.WHITE, R.id.action_changelog to GoogleMaterial.Icon.gmd_info)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_changelog -> showChangelog()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    fun showChangelog() {
        showChangelog(R.xml.theme_changelog) {
            titleColorRes(R.color.text)
            contentColorRes(R.color.text)
            positiveColorRes(R.color.text)
            backgroundColorRes(R.color.dialog_background)
        }
    }
}