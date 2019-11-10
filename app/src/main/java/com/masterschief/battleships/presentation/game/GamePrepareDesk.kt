package com.masterschief.battleships.presentation.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import com.masterschief.battleships.domain.entity.BESIDE
import com.masterschief.battleships.domain.entity.CHOSEN
import com.masterschief.battleships.domain.entity.WHOLE

class GamePrepareDesk(cxt: Context, attrs: AttributeSet) : GameDesk(cxt, attrs),
    GameDeskContract.PrepareDesk {

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
        gameArray?.let {
            val pWhole = Paint()
            pWhole.color = colorShip

            val pChosen = Paint()
            pChosen.color = colorChosenShip

            val pBeside = Paint()
            pBeside.color = colorBesideShip

            for ((r, intArray) in it.withIndex()) {
                for ((c, value) in intArray.withIndex()) {

                    if (it[r][c] == WHOLE || it[r][c] == CHOSEN || it[r][c] == BESIDE) {

                        val range = (canvas.width / size).toFloat()
                        val top = r * range
                        val bottom = top + range
                        val left = c * range
                        val right = left + range

                        canvas.drawRect(
                            left, top, right, bottom,
                            when (it[r][c]) {
                                WHOLE -> pWhole
                                CHOSEN -> pChosen
                                BESIDE -> pBeside
                                else -> Paint()
                            }
                        )
                    }
                }
            }
        }
    }

    //when ship changes it's place
    override fun moveShip(event: MotionEvent) {

    }
}