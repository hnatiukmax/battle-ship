package com.masterschief.battleships.uigame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import com.masterschief.battleships.utils.Point

class GameBattleDesk(cxt : Context, attrs : AttributeSet) : GameDesk(cxt, attrs), GameDeskContract.BattleDesk {

    protected lateinit var currentCell: Point

    override fun initDesk(array: Array<IntArray>, point: Point) {
        this.gameArray = array
        this.currentCell = point
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(colorDesk)
        //other methods
        //drawCurrentCell(canvas)
        drawDesk(canvas)
    }

    //draw current cell
    private fun drawCurrentCell(canvas: Canvas) {
        val p = Paint()
        p.color = colorCell

        val range = (canvas.width / size).toFloat()
        var top = currentCell.x * range
        var bottom = top + range
        var left = currentCell.y * range
        var right = left + range

        if (currentCell.x != -1 && currentCell.y != -1) {
            for (i in 0..size) {
                canvas.apply {
                    drawRect(
                        i * range, top, range * (i + 1), bottom, p
                    )

                    drawRect(
                        left, i * range, right, range * (i + 1), p
                    )
                }
            }
        }
    }

    //show attack
    override fun showAttack() {

    }

}