package com.masterschief.battleships.presentation.pages.game

import com.masterschief.battleships.domain.entity.Message
import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.domain.eventlisteners.GameEventListener
import com.masterschief.battleships.domain.gamebussinesslogic.GameController
import com.masterschief.battleships.presentation.pages.base.BasePresenter
import com.masterschief.battleships.presentation.pages.base.BaseView

interface GameContract {

    interface View : BaseView {

        val ownBattleDesk : Array<IntArray>

        fun setEnemiesDesk(enemiesArrayDesk : Array<IntArray>)

        fun updateOwnGameDesk()

        fun updateEnemiesDesk()

        fun onFinish(isWin : Boolean)
    }

    interface Presenter : BasePresenter, GameEventListener, GameController.OnFinishGameListener {

        fun sendMessage(messageText : String)

        fun onAttack(point : Point)
    }
}