package com.krm.shinebutton

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.daasuu.ei.Ease
import com.daasuu.ei.EasingInterpolator
import com.krm.shinebutton.listener.SimpleAnimatorListener
import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt

class ShineView : View {
    private var shineAnimator: ShineAnimator? = null
    private lateinit var clickAnimator: ValueAnimator
    private var shineButton: ShineButton? = null
    private var paint: Paint? = null
    private var paint2: Paint? = null
    private var paintSmall: Paint? = null
    private var colorCount = 10
    private var shineCount = 0
    private var smallOffsetAngle = 0f
    private var turnAngle = 0f
    private var animDuration: Long = 0
    private var clickAnimDuration: Long = 0
    private var shineDistanceMultiple = 0f
    private var smallShineColor = colorRandom[0]
    private var bigShineColor = colorRandom[1]
    private var shineSize = 0
    private var allowRandomColor = false
    private var enableFlashing = false
    private var rectF = RectF()
    private var rectFSmall = RectF()
    private var random = Random()
    private var centerAnimX = 0
    private var centerAnimY = 0
    private var btnWidth = 0
    private var btnHeight = 0
    private var thirdLength = 0.0
    private var value = 0f
    var clickValue: Float = 0f
    private var isRun = false
    private val distanceOffset = 0.2f

    constructor(context: Context?) : super(context)
    constructor(
        context: Context?,
        shineButton: ShineButton,
        shineParams: ShineParams
    ) : super(context) {
        initShineParams(shineParams, shineButton)
        shineAnimator = ShineAnimator(animDuration, shineDistanceMultiple, clickAnimDuration)
        val frameRefreshDelay: Long = 25
        ValueAnimator.setFrameDelay(frameRefreshDelay)
        this.shineButton = shineButton
        paint = Paint()
        paint!!.color = bigShineColor
        paint!!.strokeWidth = 20f
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeCap = Paint.Cap.ROUND
        paint2 = Paint()
        paint2!!.color = Color.WHITE
        paint2!!.strokeWidth = 20f
        paint2!!.strokeCap = Paint.Cap.ROUND
        paintSmall = Paint()
        paintSmall!!.color = smallShineColor
        paintSmall!!.strokeWidth = 10f
        paintSmall!!.style = Paint.Style.STROKE
        paintSmall!!.strokeCap = Paint.Cap.ROUND
        clickAnimator = ValueAnimator.ofFloat(0f, 1.1f)
        ValueAnimator.setFrameDelay(frameRefreshDelay)
        clickAnimator.duration = clickAnimDuration
        clickAnimator.interpolator = EasingInterpolator(Ease.QUART_OUT)
        clickAnimator.addUpdateListener { valueAnimator: ValueAnimator ->
            clickValue = valueAnimator.animatedValue as Float
            invalidate()
        }
        clickAnimator.addListener(object : SimpleAnimatorListener() {
            override fun onAnimationEnd(animator: Animator) {
                clickValue = 0f
                invalidate()
            }
        })
        shineAnimator!!.addListener(object : SimpleAnimatorListener() {
            override fun onAnimationEnd(animator: Animator) {
                shineButton.removeView(this@ShineView)
            }
        })
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private fun showAnimation(shineButton: ShineButton?) {
        btnWidth = shineButton!!.width
        btnHeight = shineButton.height
        thirdLength = getThirdLength(btnHeight, btnWidth)
        val location = IntArray(2)
        shineButton.getLocationInWindow(location)
        centerAnimX = location[0] + shineButton.width / 2
        centerAnimY = location[1] + shineButton.height / 2
        if (shineButton.mFixDialog != null && shineButton.mFixDialog.window != null) {
            val decor = shineButton.mFixDialog.window!!.decorView
            centerAnimX -= decor.paddingLeft
            centerAnimY -= decor.paddingTop
        }
        shineAnimator!!.addUpdateListener { valueAnimator: ValueAnimator ->
            value = valueAnimator.animatedValue as Float
            if (shineSize != 0 && shineSize > 0) {
                paint!!.strokeWidth = shineSize * (shineDistanceMultiple - value)
                paintSmall!!.strokeWidth =
                    shineSize.toFloat() / 3 * 2 * (shineDistanceMultiple - value)
            } else {
                paint!!.strokeWidth = btnWidth / 2 * (shineDistanceMultiple - value)
                paintSmall!!.strokeWidth = btnWidth / 3 * (shineDistanceMultiple - value)
            }
            rectF[centerAnimX - btnWidth / (3 - shineDistanceMultiple) * value, centerAnimY - btnHeight / (3 - shineDistanceMultiple) * value, centerAnimX + btnWidth / (3 - shineDistanceMultiple) * value] =
                centerAnimY + btnHeight / (3 - shineDistanceMultiple) * value
            rectFSmall[centerAnimX - btnWidth / (3 - shineDistanceMultiple + distanceOffset) * value, centerAnimY - btnHeight / (3 - shineDistanceMultiple + distanceOffset) * value, centerAnimX + btnWidth / (3 - shineDistanceMultiple + distanceOffset) * value] =
                centerAnimY + btnHeight / (3 - shineDistanceMultiple + distanceOffset) * value
            invalidate()
        }
        shineAnimator!!.startAnim()
        clickAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until shineCount) {
            if (allowRandomColor) {
                paint!!.color =
                    colorRandom[if (abs(colorCount / 2 - i) >= colorCount) colorCount - 1 else abs(
                        colorCount / 2 - i
                    )]
            }
            canvas.drawArc(
                rectF,
                360f / shineCount * i + 1 + (value - 1) * turnAngle,
                0.1f,
                false,
                getConfigPaint(paint)!!
            )
        }
        for (i in 0 until shineCount) {
            if (allowRandomColor) {
                paint!!.color =
                    colorRandom[if (abs(colorCount / 2 - i) >= colorCount) colorCount - 1 else abs(
                        colorCount / 2 - i
                    )]
            }
            canvas.drawArc(
                rectFSmall,
                360f / shineCount * i + 1 - smallOffsetAngle + (value - 1) * turnAngle,
                0.1f,
                false,
                getConfigPaint(paintSmall)!!
            )
        }
        paint!!.strokeWidth = btnWidth * clickValue * (shineDistanceMultiple - distanceOffset)
        if (clickValue != 0f) {
            paint2!!.strokeWidth =
                btnWidth * clickValue * (shineDistanceMultiple - distanceOffset) - 8
        } else {
            paint2!!.strokeWidth = 0f
        }
        canvas.drawPoint(centerAnimX.toFloat(), centerAnimY.toFloat(), paint!!)
        canvas.drawPoint(centerAnimX.toFloat(), centerAnimY.toFloat(), paint2!!)
        if (shineAnimator != null && !isRun) {
            isRun = true
            showAnimation(shineButton)
        }
    }

    private fun getConfigPaint(paint: Paint?): Paint? {
        if (enableFlashing) {
            paint!!.color = colorRandom[random.nextInt(colorCount - 1)]
        }
        return paint
    }

    private fun getThirdLength(btnHeight: Int, btnWidth: Int): Double {
        val all = btnHeight * btnHeight + btnWidth * btnWidth
        return sqrt(all.toDouble())
    }

    class ShineParams {
        @JvmField
        var allowRandomColor = false

        @JvmField
        var animDuration: Long = 1500

        @JvmField
        var bigShineColor = 0

        @JvmField
        var clickAnimDuration: Long = 200

        @JvmField
        var enableFlashing = false

        @JvmField
        var shineCount = 7

        @JvmField
        var shineTurnAngle = 20f

        @JvmField
        var shineDistanceMultiple = 1.5f

        @JvmField
        var smallShineOffsetAngle = 20f

        @JvmField
        var smallShineColor = 0

        @JvmField
        var shineSize = 0

        init {
            colorRandom[0] = Color.parseColor("#FFFF99")
            colorRandom[1] = Color.parseColor("#FFCCCC")
            colorRandom[2] = Color.parseColor("#996699")
            colorRandom[3] = Color.parseColor("#FF6666")
            colorRandom[4] = Color.parseColor("#FFFF66")
            colorRandom[5] = Color.parseColor("#F44336")
            colorRandom[6] = Color.parseColor("#666666")
            colorRandom[7] = Color.parseColor("#CCCC00")
            colorRandom[8] = Color.parseColor("#666666")
            colorRandom[9] = Color.parseColor("#999933")
        }
    }

    private fun initShineParams(shineParams: ShineParams, shineButton: ShineButton) {
        shineCount = shineParams.shineCount
        turnAngle = shineParams.shineTurnAngle
        smallOffsetAngle = shineParams.smallShineOffsetAngle
        enableFlashing = shineParams.enableFlashing
        allowRandomColor = shineParams.allowRandomColor
        shineDistanceMultiple = shineParams.shineDistanceMultiple
        animDuration = shineParams.animDuration
        clickAnimDuration = shineParams.clickAnimDuration
        smallShineColor = shineParams.smallShineColor
        bigShineColor = shineParams.bigShineColor
        shineSize = shineParams.shineSize
        if (smallShineColor == 0) {
            smallShineColor = colorRandom[6]
        }
        if (bigShineColor == 0) {
            bigShineColor = shineButton.color
        }
    }

    companion object {
        var colorRandom = IntArray(10)
    }
}