package com.krm.shinebutton

import android.animation.ValueAnimator
import com.daasuu.ei.Ease
import com.daasuu.ei.EasingInterpolator

internal class ShineAnimator(
    duration: Long,
    max_value: Float,
    delay: Long
) : ValueAnimator() {
    fun startAnim() {
        start()
    }

    init {
        setFloatValues(1f, max_value)
        setDuration(duration)
        startDelay = delay
        interpolator = EasingInterpolator(Ease.QUART_OUT)
    }
}