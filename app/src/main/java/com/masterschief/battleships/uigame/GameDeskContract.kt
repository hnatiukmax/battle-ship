package com.masterschief.battleships.uigame

import android.view.MotionEvent
import com.masterschief.battleships.utils.Point

interface GameDeskContract {

    interface PrepareDesk {

        fun moveShip(event : MotionEvent)

        fun initDesk(array : Array<IntArray>)

        fun updateDesk()

    }

    interface BattleDesk {

        fun showAttack()

        fun initDesk(array: Array<IntArray>, point: Point)

        fun updateDesk()
    }

}
