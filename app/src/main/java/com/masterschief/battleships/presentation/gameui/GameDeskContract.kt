package com.masterschief.battleships.presentation.gameui

import android.view.MotionEvent

interface GameDeskContract {

    interface PrepareDesk {

        fun moveShip(event : MotionEvent)

        fun initDesk(array : Array<IntArray>)

        fun updateDesk()

    }

    interface BattleDesk {

        fun showAttack()

        fun initDesk(array: Array<IntArray>
//                     , point: Point
        )

        fun updateDesk()

        fun setOnAttackListener(onAttackListener: GameBattleDesk.OnAttackListener)
    }

}
