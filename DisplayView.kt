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
    var width: Int = Command04().getDisplaySize()[1].toInt(),
    var height: Int = Command04().getDisplaySize()[0].toInt()
) {
    val imageView: ImageView

    private val bitmap: Bitmap
    private var density: Int
    private val redColor = context.getColor(R.color.pixel)
    private val blackColor = context.getColor(R.color.black)

    private enum class Errors {
        UNKNOWN,
        WRONG_POSITION_X,
        WRONG_POSITION_Y,
        NO_ERROR
    }

    init {
        density = computeDensity()

        height += density
        width += density

        imageView = ImageView(context)

        bitmap = Bitmap.createBitmap(
            (width * density) + density,
            (height * density) + density,
            Bitmap.Config.ARGB_8888
        )

        redraw()
        imageView.layoutParams = LinearLayout.LayoutParams(bitmap.width, bitmap.height)
    }

    fun redraw() {
        imageView.setImageBitmap(bitmap)
    }

    private fun computeDensity(): Int {
        val heightPixels = context.resources.displayMetrics.heightPixels
        val widthPixels = context.resources.displayMetrics.widthPixels

        val density = heightPixels / height

        if ((density * width) + density > widthPixels) {
            return widthPixels / width
        }

        return density
    }

    private fun setPixel(x: Int, y: Int, color: Int): Errors {
        val xPos = x * density
        val yPos = y * density

        if (xPos + density > bitmap.width) {
            return Errors.WRONG_POSITION_X
        }

        if (yPos + density > bitmap.height) {
            return Errors.WRONG_POSITION_Y
        }

        for (rowIndex in 0 until density) {
            for (columnIndex in 0 until density) {
                bitmap.setPixel(xPos + rowIndex, yPos + columnIndex, color)
            }
        }

        return Errors.NO_ERROR
    }

    private fun renderCommand31(command: Command31): Errors {
        val x = command.getCoords()[0]
        val y = command.getCoords()[1]
        val bytes = command.getBytes()
        val type = command.getColorType().toInt()

        var hasError = Errors.NO_ERROR

        bytes.forEachIndexed { column, byte ->
            byte.forEachIndexed { row, char ->
                val xPos = x.toInt() + column
                val yPos = y.toInt() + row

                val err = if (type == 0x00) {
                    setPixel(xPos, yPos, if (char == '1') redColor else blackColor)
                } else if (type == 0x01) {
                    if (char == '1') {
                        setPixel(xPos, yPos, redColor)
                    } else {
                        Errors.NO_ERROR
                    }
                } else if (type == 0x02) {
                    if (char == '1') {
                        setPixel(xPos, yPos, blackColor)
                    } else {
                        Errors.NO_ERROR
                    }
                } else {
                    Errors.UNKNOWN
                }

                hasError = if (err == Errors.NO_ERROR) hasError else err
            }
        }

        return hasError
    }

    private fun renderCommand39(command: Command39): Errors {
        val x = command.getCoords()[0]
        val y = command.getCoords()[1]
        val height = command.getSize()[0]
        val width = command.getSize()[1]
        val color = command.getColorType().toInt()

        var hasError = Errors.NO_ERROR

        Array(height.toInt()) { it }.forEach { row ->
            Array(width.toInt()) { it }.forEach { column ->
                val xPos = x.toInt() + column
                val yPos = y.toInt() + row

                val err = if (color == 0x00) {
                    setPixel(xPos, yPos, redColor)
                } else if (color == 0x02) {
                    setPixel(xPos, yPos, blackColor)
                } else {
                    Errors.UNKNOWN
                }

                hasError = if (err == Errors.NO_ERROR) hasError else err
            }
        }

        return hasError
    }

    fun render(model: CommandModel) {
        val error =
            if (model is Command31) {
                renderCommand31(model)
            } else if (model is Command39) {
                renderCommand39(model)
            } else {
                Errors.NO_ERROR
            }

        if (error != Errors.NO_ERROR) {
            AppLog.debug(TAG, "Start ----------------")
            AppLog.debug(TAG, "Command: $model")

            if (error == Errors.UNKNOWN) {
                AppLog.debug(TAG, "Unknown")
            } else if (error == Errors.WRONG_POSITION_X) {
                AppLog.debug(TAG, "Wrong x position")
            } else if (error == Errors.WRONG_POSITION_Y) {
                AppLog.debug(TAG, "Wrong y position")
            }

            AppLog.debug(TAG, "End ----------------")
        }
    }

    companion object {
        private var TAG = DisplayView::class.simpleName ?: ""
    }
}