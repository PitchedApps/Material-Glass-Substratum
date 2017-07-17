package com.pitchedapps.material.glass.utils

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.View

/**
 * Created by Allan Wang on 2017-07-10.
 */
val View.y: Int
    get() {
        val r = Rect()
        getLocalVisibleRect(r)
        log(r.flattenToString())
        return r.centerY()
    }

fun log(text: String) = Log.d("MGS", text)

val Activity.activityHeight: Int
    get() {
        val p = Point()
        windowManager.defaultDisplay.getSize(p)
        return p.y
    }