package com.example.utils

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

object AnimationHelper {

    fun fadeIn(view: View, duration: Long = 500) {
        view.visibility = View.VISIBLE
        val anim = AlphaAnimation(0f, 1f).apply {
            this.duration = duration
            fillAfter = true
        }
        view.startAnimation(anim)
    }

    fun slideUp(view: View, duration: Long = 500) {
        view.visibility = View.VISIBLE
        val anim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f
        ).apply {
            this.duration = duration
            fillAfter = true
        }
        view.startAnimation(anim)
    }

    fun popIn(view: View, duration: Long = 300) {
        view.visibility = View.VISIBLE
        val anim = ScaleAnimation(
            0.7f, 1.0f,
            0.7f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            this.duration = duration
            fillAfter = true
        }
        view.startAnimation(anim)
    }
}
