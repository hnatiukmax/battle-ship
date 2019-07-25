package com.masterschief.battleships.uigame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.masterschief.battleships.R

class GameDesk : View, GameDeskContract {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var gameArray : Array<IntArray>

    private val size = 10
    private val colorDesk = resources.getColor(R.color.deskBackgroundColor)
    private val colorLine = resources.getColor(R.color.mainBackgroundColor)

    fun setGameArray(array : Array<IntArray>) {
        this.gameArray = array
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cvSize = canvas?.width
        val p = Paint()
        p.strokeWidth = 7F
        p.color = colorLine

        canvas?.drawColor(colorDesk)

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

    override fun updateDesk() {
        
    }

    override fun showAttack() {

    }
}