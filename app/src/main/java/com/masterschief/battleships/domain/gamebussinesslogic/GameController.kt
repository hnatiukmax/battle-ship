package com.masterschief.battleships.domain.gamebussinesslogic

import com.masterschief.battleships.domain.entity.*

class GameController(
    private val ownDesk : Array<IntArray>,
    private val enemiesDesk : Array<IntArray>,
    private val onFinishListener : OnFinishGameListener
) {

    fun onDefend(point : Point) : Boolean {
        val result =
            if (ownDesk[point.y][point.x] == WHOLE) {
                ownDesk[point.y][point.x] = SHOOTED
                false
            } else {
                ownDesk[point.y][point.x] = BESIDE
                true
            }
        if (isFinish(ownDesk)) {
            onFinishListener.onFinishGame(false, point)
        }
        return result
    }

    fun onAttack(point : Point) : Boolean? {
        val result = when (enemiesDesk[point.y][point.x]) {
            BESIDE -> {
                null
            }
            WHOLE -> {
                enemiesDesk[point.y][point.x] = SHOOTED
                true
            }
            FREE_CELL -> {
                enemiesDesk[point.y][point.x] = BESIDE
                false
            }
            else -> false
        }
        if (isFinish(enemiesDesk)) {
            onFinishListener.onFinishGame(true, point)
        }

        return result
    }

    private fun isFinish(desk : Array<IntArray>) : Boolean {
        for (array in desk) {
            for (element in array) {
                if (element == WHOLE) return false
            }
        }
        return true

    }

    interface OnFinishGameListener {
        fun onFinishGame(isWin : Boolean, point: Point)
    }
}