package com.example.canva.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.canva.R

class ViewFinderOverlay(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val boxPaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.barcode_reticle_stroke)
        style = Paint.Style.STROKE
        strokeWidth =
            context.resources.getDimensionPixelOffset(R.dimen.barcode_reticle_stroke_width)
                .toFloat()
    }

    private val boxLinePaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.colorStartLine)
        foreground = GradientDrawable().apply {
            colors = intArrayOf(
                R.color.colorStartLine,
                R.color.colorStartLine,
                R.color.colorStartLine,
                R.color.colorStartLine,
            )
            orientation = GradientDrawable.Orientation.TOP_BOTTOM
            gradientType = GradientDrawable.LINEAR_GRADIENT
            shape = GradientDrawable.RECTANGLE
        }
    }

    // method to generate radial gradient drawable
    private fun radialGradientDrawable(): GradientDrawable {
        return GradientDrawable().apply {
            GradientDrawable().apply {
                colors = intArrayOf(
                    R.color.colorStartLine,
                    R.color.colorEndLine,
                )
                orientation = GradientDrawable.Orientation.TOP_BOTTOM
                gradientType = GradientDrawable.LINEAR_GRADIENT
                // shape = GradientDrawable.RECTANGLE
            }
        }
    }


    private val scrimPaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.barcode_reticle_background)
    }

    private val eraserPaint: Paint = Paint().apply {
        strokeWidth = boxPaint.strokeWidth
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val boxCornerRadius: Float =
        context.resources.getDimensionPixelOffset(R.dimen.barcode_reticle_corner_radius).toFloat()

     var boxRect: RectF? = null
     var boxRectTop: Int = 0
     var boxRectBottom: Int = 0
     var boxRectLeft: Int = 0
     var boxRectRight: Int = 0


    fun getBoxWidth(): Int {
        val overlayWidth = width.toFloat()
        val boxWidth = overlayWidth * 80 / 100
        return overlayWidth.toInt()
    }

    fun getBoxHeight(): Int {
        val overlayHeight = height.toFloat()
        return (overlayHeight * 36 / 100).toInt()
    }

    fun setViewFinder() {
        val overlayWidth = width.toFloat()
        val overlayHeight = height.toFloat()
        val boxWidth = overlayWidth * 80 / 100
        val boxHeight = overlayHeight * 36 / 100
        val cx = overlayWidth / 2
        val cy = overlayHeight / 2
        boxRect =
            RectF(cx - boxWidth / 2, cy - boxHeight  , cx + boxWidth / 2, cy + boxHeight)

        boxRectTop = (cy - boxHeight / 2).toInt()
        boxRectBottom = (cy + boxHeight / 2).toInt()
        boxRectLeft = (cx - boxWidth / 2).toInt()
        boxRectRight = (cx + boxWidth / 2).toInt()
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        boxRect?.let {
            // Draws the dark background scrim and leaves the box area clear.
            canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), scrimPaint)
            // As the stroke is always centered, so erase twice with FILL and STROKE respectively to clear
            // all area that the box rect would occupy.
            eraserPaint.style = Paint.Style.FILL
            canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, eraserPaint)
            eraserPaint.style = Paint.Style.STROKE
            canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, eraserPaint)
            // Draws the box.
            canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, boxPaint)

            //   canvas.drawLine("", "", "", "")
          //  canvas.drawLine(it.left, it.top, it.right, it.bottom, boxLinePaint)

            var lg = LinearGradient(0f, 0f,45f,45f,Color.BLUE,Color.RED,Shader.TileMode.CLAMP)
            boxLinePaint.shader =lg
        //    canvas.drawLine(it.left, it.top, it.right, it.top+20, boxLinePaint)


            Log.d("TAG", "onDraw:left " + it.left)
            Log.d("TAG", "onDraw:top " + it.top)
            Log.d("TAG", "onDraw:right " + it.right)
            Log.d("TAG", "onDraw:bottom " + it.bottom)


        }
    }

}