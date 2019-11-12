package com.masterschief.battleships.presentation.gameui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.masterschief.battleships.R

abstract class GameDesk : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    protected var gameArray: Array<IntArray>? = null

    protected val size = 10
    protected val strokeSize = 7f
    protected val colorDesk = resources.getColor(R.color.deskBackgroundColor)
    protected val colorLine = resources.getColor(R.color.mainBackgroundColor)
    protected val colorCell = resources.getColor(R.color.deskCellColor)
    protected val colorShip = resources.getColor(R.color.shipColor)
    protected val colorChosenShip = resources.getColor(R.color.shipChosenColor)
    protected val colorBesideBattleShip = resources.getColor(R.color.shipBesideBattleColor)
    protected val colorBesideShip = resources.getColor(R.color.shipBesideColor)

    /*
        DrawMethods
    */

    //draw base node table according to size
    protected fun drawDesk(canvas: Canvas) {
        val cvSize  = (canvas.width).toFloat()
        val p = Paint()
        p.strokeWidth = strokeSize
        p.color = colorLine

        val range = (cvSize / size.toFloat())

        for (i in 0..size) {
            canvas.apply {
                drawLine(
                    range * (i + 1),
                    0F,
                    range * (i + 1),
                    cvSize,
                    p
                )

                drawLine(
                    0F,
                    range * (i + 1),
                    cvSize,
                    range * (i + 1),
                    p
                )
            }
        }
    }


    fun updateDesk() {
        this.invalidate()
    }
}