package com.aarush.core.util.extension

import android.R
import android.content.res.Resources
import android.graphics.Color
import java.text.DecimalFormat

val Int.dp : Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun Int.isColorDark(): Boolean {
    if (R.color.transparent == this) return true
    var rtnValue = false
    val rgb = intArrayOf(Color.red(this), Color.green(this), Color.blue(this))
    val brightness = Math.sqrt(
        rgb[0] * rgb[0] * .241 + (rgb[1]
                * rgb[1] * .691) + rgb[2] * rgb[2] * .068
    ).toInt()
    // color is light
    if (brightness >= 200) {
        rtnValue = true
    }
    return !rtnValue
}

fun Double.roundOffDecimal(): Double {
    val df = DecimalFormat("#")

    return df.format(this).toDouble()
}