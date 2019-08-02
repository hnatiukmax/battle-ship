package com.masterschief.battleships.uigame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import com.masterschief.battleships.R
import com.masterschief.battleships.utils.Point

abstract class GameDesk : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    protected lateinit var gameArray: Array<IntArray>

    val size = 10

    protected val strokeSize = 7f
    protected val colorDesk = resources.getColor(R.color.deskBackgroundColor)
    protected val colorLine = resources.getColor(R.color.mainBackgroundColor)
    protected val colorCell = resources.getColor(R.color.deskCellColor)
    protected val colorShip = resources.getColor(R.color.shipColor)
    protected val colorShootedShip = resources.getColor(R.color.shipShootedColor)

    /*
        DrawMethods
    */

    //draw base node table according to size
    protected fun drawDesk(canvas: Canvas) {
        val cvSize = canvas?.width
        val p = Paint()
        p.strokeWidth = strokeSize
        p.color = colorLine

        val range = (cvSize!! / size).toFloat()

        for (i in 0..size) {
            canvas?.apply {
                drawLine(
                    range * (i + 1),
                    0F,
                    range * (i + 1),
                    cvSize.toFloat(),
                    p
                )

                drawLine(
                    0F,
                    range * (i + 1),
                    cvSize.toFloat(),
                    range * (i + 1),
                    p
                )
            }
        }
    }


    fun updateDesk() {
        //Handler().postDelayed( { this.invalidate() }, 3000)
        this.invalidate()
    }
}