package com.masterschief.battleships.domain.gamebussinesslogic

import com.masterschief.battleships.domain.entity.BESIDE
import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.domain.entity.SHOOTED
import com.masterschief.battleships.domain.entity.WHOLE

class GameController(
    private val ownDesk : Array<IntArray>,
    private val enemiesDesk : Array<IntArray>,
    private val onFinishListener : OnFinishGameListener
) {

    fun onDefend(point : Point) {
        ownDesk[point.y][point.x] =
            if (ownDesk[point.y][point.x] == WHOLE) {
                SHOOTED
            } else {
                BESIDE
            }
        if (isFinish(ownDesk)) {
            onFinishListener.onFinishGame(false)
        }
    }

    fun onAttack(point : Point) : Boolean {
        return if (enemiesDesk[point.y][point.x] == BESIDE) {
            false
        } else {
            enemiesDesk[point.y][point.x] = SHOOTED
            if (isFinish(enemiesDesk)) {
                onFinishListener.onFinishGame(true)
            }
            true
        }
    }

    private fun isFinish(desk : Array<IntArray>) : Boolean {
        for (array in desk) {
            for (element in array) {
                if (element == WHOLE) return true
            }
        }
        return true
    }

    interface OnFinishGameListener {
        fun onFinishGame(isWin : Boolean)
    }
}