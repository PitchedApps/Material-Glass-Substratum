package com.pitchedapps.material.glass

import ca.allanwang.kau.kpref.KPref
import ca.allanwang.kau.kpref.kpref

/**
 * Created by Allan Wang on 2017-07-18.
 */
object Prefs : KPref() {

    var versionCode: Int by kpref("version_code", -1)

    var installDate: Long by kpref("install_date", -1L)

    var identifier: Int by kpref("identifier", -1)

    val randomId:String
    get() = "$identifier-$installDate"

}