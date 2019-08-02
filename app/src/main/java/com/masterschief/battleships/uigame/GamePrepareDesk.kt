package com.masterschief.battleships.uigame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import com.masterschief.battleships.gamelogic.*

import com.masterschief.battleships.utils.Point

class GamePrepareDesk(cxt : Context, attrs : AttributeSet) : GameDesk(cxt, attrs), GameDeskContract.PrepareDesk {

    override fun initDesk(array: Array<IntArray>) {
        this.gameArray = array
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(colorDesk)
        drawPrepareShips(canvas)
        drawDesk(canvas)

    }

    //draw ships
    private fun drawPrepareShips(canvas: Canvas) {
        val pWhole = Paint()
        pWhole.color = colorShip

        val pShooted = Paint()
        pShooted.color = colorShootedShip


        for ((r, intArray) in gameArray.withIndex()) {
            for ((c, value) in intArray.withIndex()) {

                if (gameArray[r][c] == WHOLE || gameArray[r][c] == CHOSEN) {

                    val range = (canvas.width / size).toFloat()
                    val top = r * range
                    val bottom = top + range
                    val left = c * range
                    val right = left + range

                    canvas.drawRect(left, top, right, bottom,
                        when(gameArray[r][c]) {
                            WHOLE -> pWhole
                            CHOSEN -> pShooted
                            else -> Paint()
                        }
                        )
                }
            }
        }

    }

    //when ship changes it's place
    override fun moveShip(event: MotionEvent) {

    }
}