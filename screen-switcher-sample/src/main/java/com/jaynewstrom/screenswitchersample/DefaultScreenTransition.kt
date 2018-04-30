package com.jaynewstrom.screenswitchersample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

import com.jaynewstrom.screenswitcher.ScreenTransition

object DefaultScreenTransition : ScreenTransition {
    override fun transitionIn(foregroundView: View, backgroundView: View, onTransitionCompleted: Runnable) {
        foregroundView.x = (foregroundView.parent as View).measuredWidth.toFloat()
        foregroundView.animate()
            .x(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onTransitionCompleted.run()
                }
            })
    }

    override fun transitionOut(foregroundView: View, backgroundView: View, onTransitionCompleted: Runnable) {
        foregroundView.animate()
            .x((foregroundView.parent as View).measuredWidth.toFloat())
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onTransitionCompleted.run()
                }
            })
    }
}
