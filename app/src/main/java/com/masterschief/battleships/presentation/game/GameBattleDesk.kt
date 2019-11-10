package com.masterschief.battleships.presentation.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import com.masterschief.battleships.domain.entity.BESIDE
import com.masterschief.battleships.domain.entity.CHOSEN
import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.domain.entity.WHOLE
import kotlinx.android.synthetic.main.activity_game.view.*

class GameBattleDesk(cxt: Context, attrs: AttributeSet) : GameDesk(cxt, attrs),
    GameDeskContract.BattleDesk {

    private var onAttackListener: OnAttackListener? = null
    private lateinit var currentCell: Point

    init {
        rootView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = (event.x / (v.width / 10)).toInt()
                    val y = (event.y / (v.width / 10)).toInt()

                    onAttackListener?.onAttack(Point(y, x))
                }
            }

            true
        }
    }

    override fun setOnAttackListener(onAttackListener: OnAttackListener) {
        this.onAttackListener = onAttackListener
    }

    override fun initDesk(
        array: Array<IntArray>
//                          , point: Point
    ) {
        this.gameArray = array
//        this.currentCell = point
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(colorDesk)
        drawPrepareShips(canvas)
        drawDesk(canvas)
    }

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

    //draw current cell
    private fun drawCurrentCell(canvas: Canvas) {
        val p = Paint()
        p.color = colorCell

        val range = (canvas.width / size).toFloat()
        val top = currentCell.x * range
        val bottom = top + range
        val left = currentCell.y * range
        val right = left + range

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

    interface OnAttackListener {
        fun onAttack(point: Point)
    }

}