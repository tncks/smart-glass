package com.smart.app.util

import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun TextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

fun TextView.rightDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0)
}


@Suppress("unused")
fun TextView.topDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0)
}

@Suppress("unused")
fun TextView.bottomDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, id)
}


fun TextView.bottomDrawable(@DrawableRes id: Int = 0, @DimenRes sizeRes: Int) {
    val drawable = ContextCompat.getDrawable(context, id)
    val size = resources.getDimensionPixelSize(sizeRes)
    drawable?.setBounds(5, 5, size * 2, size)
    this.setCompoundDrawables(null, null, null, drawable)
}
