package vhumeniuk.floatinglikes

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import kotlin.random.Random

class LikeView(
    private val tapView: View
) {

    companion object {
        private const val TOUCH_INITIAL_DELAY = 400L //ms

        private const val DEFAULT_SMALL_DELAY = 100L //ms
        private const val TRANSLATE_DURATION = 1200L //ms
        private const val FADE_DURATION = 500L //ms
        private const val FLOAT_ALPHA = 0.7f
        private const val SMALL_EMOJI_SIZE = 16f
        private const val DEFAULT_EMOJI = "\u2764"
        private const val DEFAULT_FLOAT_HEIGHT = 1200//TODO
    }

    private var floatHeight = DEFAULT_FLOAT_HEIGHT
    private var smallDelay = DEFAULT_SMALL_DELAY
    private val handlerSmall = Handler()
    private var emoji = DEFAULT_EMOJI
    private var randomize: Boolean = true
    private var size = SMALL_EMOJI_SIZE
    private var translateDuration = TRANSLATE_DURATION
    private val emojiViewPool = mutableListOf<TextView>()

    @SuppressLint("ClickableViewAccessibility")
    fun setup(emoji: String = DEFAULT_EMOJI,
              delay: Long = DEFAULT_SMALL_DELAY,
              randomize: Boolean = true,
              size: Float = SMALL_EMOJI_SIZE,
              translateDuration: Long = TRANSLATE_DURATION,
              floatHeight: Int = DEFAULT_FLOAT_HEIGHT) {
        this.emoji = emoji
        this.smallDelay = delay
        this.randomize = randomize
        this.size = size
        this.translateDuration = translateDuration
        this.floatHeight = floatHeight
        tapView.setOnTouchListener { _, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    handlerSmall.postDelayed(emitSmallRunnable, TOUCH_INITIAL_DELAY)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_HOVER_EXIT -> {
                    handlerSmall.removeCallbacks(emitSmallRunnable)
                }
                else -> {}
            }
            true
        }
    }

    private fun emitSmall() {
        emit()
        handlerSmall.postDelayed(emitSmallRunnable, smallDelay)
    }

    private fun emit() {
        val floatingView = getEmojiView()
        floatingView.textSize = size

        val parent = tapView.parent as ViewGroup

        val sizeDim = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size * 1.2f, tapView.resources.displayMetrics)
        val randomX = try { if (randomize) Random.nextInt(-(tapView.width - sizeDim).toInt().div(2), tapView.width / 2) else 0 } catch (e: Exception) { 0 }
        val randomY = try { if (randomize) Random.nextInt(-tapView.height, tapView.height) else 0 } catch (e: Exception) { 0 }
        val startX = tapView.width / 2 - sizeDim / 2 + tapView.x
        val startY = tapView.y - parent.paddingTop + tapView.height / 2
        val destinationY = startY - floatHeight + 2 * tapView.height + randomY
        val destinationX = startX + randomX

        val translateAnimation = TranslateAnimation(startX, destinationX,
            startY, destinationY).apply {
            duration = translateDuration
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    emojiViewPool.add(floatingView)
                }
                override fun onAnimationStart(animation: Animation?) {
                    floatingView.visibility = View.VISIBLE
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        floatingView.alpha = 1f
        floatingView.startAnimation(translateAnimation)
        runFade(floatingView, translateDuration - FADE_DURATION)
    }

    private fun getEmojiView(): TextView {
        return if (emojiViewPool.isNotEmpty()) {
            emojiViewPool.removeAt(0)
        } else {
            val view = TextView(tapView.context).apply {
                text = emoji
                setTextColor(Color.BLACK)
                alpha = FLOAT_ALPHA
            }
            val parent = tapView.parent as ViewGroup
            parent.addView(view)
            tapView.bringToFront()
            view
        }
    }

    private fun runFade(view: View, delay: Long) {
        view.animate()
            .setStartDelay(delay)
            .alpha(0.0f)
            .setUpdateListener { animator ->
                if (view.alpha == 0.0f) {
                    animator.cancel()
                }
            }.duration = FADE_DURATION
    }

    private val emitSmallRunnable = Runnable {
        emitSmall()
    }
}