package ru.lostgirl.rfidinventory.utils.searchablespinner

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.graphics.Rect
import android.view.ViewAnimationUtils
import kotlin.math.hypot

internal object CircularReveal {

    fun startReveal(
        toReveal: Boolean,
        dialog: Dialog,
        onAnimationEnd: OnAnimationEnd,
        animationDuration: Int
    ) {
        val rootView = dialog.window!!.decorView
        val circularRevealAnimator: Animator
        val clickedViewRect = Rect()
        rootView.getGlobalVisibleRect(clickedViewRect)
        val width = rootView.measuredWidth
        val height = rootView.measuredHeight
        val cx = clickedViewRect.exactCenterX().toInt()
        val cy = clickedViewRect.exactCenterY().toInt()
        val minRadiusForReveal =
            if (toReveal) {
                0F
            } else {
                hypot(width.toFloat() / 2, height.toFloat() / 2) + HORIZONTAL_INSET
            }
        val maxRadiusForReveal =
            if (toReveal) {
                hypot(
                    width.toFloat() / 2,
                    height.toFloat() / 2
                ) + HORIZONTAL_INSET
            } else {
                0F
            }
        circularRevealAnimator =
            ViewAnimationUtils.createCircularReveal(
                rootView,
                cx,
                cy,
                minRadiusForReveal,
                maxRadiusForReveal
            )
        circularRevealAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (!toReveal) dialog.dismiss()
                onAnimationEnd.onAnimationEndListener(toReveal)
            }
        })
        circularRevealAnimator.duration = animationDuration.toLong()
        circularRevealAnimator.start()
    }
}
