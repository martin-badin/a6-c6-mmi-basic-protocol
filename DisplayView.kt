package com.mbadin.mmibasic

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.LinearLayout
import com.mbadin.mmibasic.commands.Command04
import com.mbadin.mmibasic.commands.Command31
import com.mbadin.mmibasic.commands.Command39
import com.mbadin.mmibasic.utils.AppLog

@ExperimentalUnsignedTypes
class DisplayView(
    private val context: Context,
    size: Pair<Int, Int> = Command04().getDisplaySize()
) {

    val imageView: ImageView = ImageView(context)

    private var height: Int = size.first
    private var width: Int = size.second

    private val bitmap: Bitmap
    private val density: Int

    private enum class Errors {
        UNKNOWN,
        WRONG_POSITION_X,
        WRONG_POSITION_Y,
        NO_ERROR
    }

    init {
        density = computeDensity()

        this.height += density
        this.width += density

        bitmap = Bitmap.createBitmap(
            (this.width * density) + density,
            (this.height * density) + density,
            Bitmap.Config.ARGB_8888
        )

        redraw()

        imageView.layoutParams = LinearLayout.LayoutParams(bitmap.width, bitmap.height)
    }

    fun redraw() {
        imageView.setImageBitmap(bitmap)
    }

    private fun computeDensity(): Int {
        val metrics = context.resources.displayMetrics
        val heightPixels = metrics.heightPixels
        val widthPixels = metrics.widthPixels

        val density = heightPixels / height

        return if ((density * width) + density > widthPixels) {
            widthPixels / width
        } else {
            density
        }
    }

    private fun setPixel(x: Int, y: Int, color: Int): Errors {

        val xPos = x * density
        val yPos = y * density

        if (xPos + density > bitmap.width) return Errors.WRONG_POSITION_X
        if (yPos + density > bitmap.height) return Errors.WRONG_POSITION_Y

        for (row in 0 until density) {
            for (column in 0 until density) {
                bitmap.setPixel(xPos + column, yPos + row, color)
            }
        }

        return Errors.NO_ERROR
    }

    fun render(model: CommandModel) {

        val error = when (model) {

            is Command31 -> {
                model.render { x, y, color ->
                    setPixel(x, y, color)
                }
                Errors.NO_ERROR
            }

            is Command39 -> {
                model.render { x, y, color ->
                    setPixel(x, y, color)
                }
                Errors.NO_ERROR
            }

            else -> Errors.NO_ERROR
        }

        if (error != Errors.NO_ERROR) {
            AppLog.debug(TAG, "Start ----------------")
            AppLog.debug(TAG, "Command: $model")

            when (error) {
                Errors.UNKNOWN -> AppLog.debug(TAG, "Unknown")
                Errors.WRONG_POSITION_X -> AppLog.debug(TAG, "Wrong x position")
                Errors.WRONG_POSITION_Y -> AppLog.debug(TAG, "Wrong y position")
                else -> {}
            }

            AppLog.debug(TAG, "End ----------------")
        }
    }

    companion object {
        private val TAG = DisplayView::class.simpleName ?: ""
    }
}