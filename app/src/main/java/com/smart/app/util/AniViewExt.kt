package com.smart.app.util

import android.view.View
import android.view.animation.ScaleAnimation

fun View.slideGenie(duration: Int = 500) {
    visibility = View.VISIBLE

    val animate2 = ScaleAnimation(
        1f,
        1f,
        1f,
        0.001f,
        ScaleAnimation.RELATIVE_TO_PARENT,
        .9f,
        ScaleAnimation.RELATIVE_TO_PARENT,
        .2f
    )

    animate2.duration = duration.toLong()
    animate2.fillAfter = true
    this.startAnimation(animate2)
}

fun View.slideBack(duration: Int = 500) {
    visibility = View.VISIBLE

    val animate2 = ScaleAnimation(
        1f,
        1f,
        0.001f,
        1f,
        ScaleAnimation.RELATIVE_TO_PARENT,
        .9f,
        ScaleAnimation.RELATIVE_TO_PARENT,
        .2f
    )

    animate2.duration = duration.toLong()
    animate2.fillAfter = true
    this.startAnimation(animate2)
}

// Refer
// slidegenie
// val animate = TranslateAnimation(0f, this.width.toFloat(), 0f, 0f)
//    animate.duration = duration.toLong()
//    animate.fillAfter = true
// slideback
// val animate = TranslateAnimation(0f, this.width.toFloat(), 0f, 0f)
//    animate.duration = duration.toLong()
//    animate.fillAfter = true