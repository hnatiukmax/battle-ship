package com.masterschief.battleships.presentation.gameui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import com.masterschief.battleships.R
import com.masterschief.battleships.domain.entity.*

class GameBattleDesk(cxt: Context, attrs: AttributeSet) : GameDesk(cxt, attrs),
    GameDeskContract.BattleDesk {

    private var onAttackListener: OnAttackListener? = null
    private lateinit var currentCell: Point
    private val shootedCell = resources.getDrawable(R.drawable.ic_shooted, null)
    var isOwn = true

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawPrepareShips(canvas: Canvas) {
        gameArray?.let {
            val pWhole = Paint()
            pWhole.color = colorShip

            val pChosen = Paint()
            pChosen.color = colorChosenShip

            val pBeside = Paint()
            pBeside.color = colorBesideBattleShip

            for ((r, intArray) in it.withIndex()) {
                for ((c, value) in intArray.withIndex()) {

                    val range = (canvas.width / size).toFloat()
                    val top = r * range
                    val bottom = top + range
                    val left = c * range
                    val right = left + range

                    when {
                        isOwn && (value == WHOLE) -> canvas.drawRect(left, top, right, bottom, pWhole)
                        value == BESIDE -> canvas.drawRect(left, top, right, bottom, pBeside)
                        value == SHOOTED -> {
                            val padding = 10
                            shootedCell.apply {
                                setBounds(
                                    left.toInt() + padding,
                                    top.toInt() + padding,
                                    right.toInt() - padding,
                                    bottom.toInt() - padding
                                )
                                draw(canvas)
                            }
                        }
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