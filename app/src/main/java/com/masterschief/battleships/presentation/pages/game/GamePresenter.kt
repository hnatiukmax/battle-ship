package com.masterschief.battleships.presentation.pages.game

import com.masterschief.battleships.bluetooth.BluetoothGameRepository
import com.masterschief.battleships.presentation.app.MyApplication
import com.masterschief.battleships.domain.entity.Message
import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.domain.entity.enums.GameType
import com.masterschief.battleships.domain.entity.enums.MessageType
import com.masterschief.battleships.domain.gamebussinesslogic.GameController
import com.masterschief.battleships.domain.repository.GameRepository
import com.masterschief.battleships.presentation.pages.base.BaseView

class GamePresenter(
    private val gameType : GameType
) : GameContract.Presenter {

    private var gameRepository : GameRepository? = null
    private var view : GameContract.View? = null
    private lateinit var controller : GameController
    private lateinit var ownDesk : Array<IntArray>
    private lateinit var enemiesDesk: Array<IntArray>
    private var isAttackAvailable = true

    init {
        when(gameType) {
            GameType.BLUETOOTH_GAME -> {
                MyApplication.instance.currentSocket?.let {
                    gameRepository = BluetoothGameRepository(
                        socket = it,
                        eventListener = this
                    )
                }
            }
            GameType.NETWORK_GAME -> {

            }
        }
    }

    override fun attachView(view: BaseView) {
        if (view is GameContract.View) {
            this.view = view

            sendData(
                Message(
                    type = MessageType.INIT,
                    message = view.ownBattleDesk
                ))

            ownDesk = view.ownBattleDesk
        }
    }

    override fun detachView() {
        this.view = null
    }

    override fun onInit(enemiesDesk: Array<IntArray>) {
        this.enemiesDesk = enemiesDesk
        controller = GameController(ownDesk, enemiesDesk, this)
        view?.setEnemiesDesk(enemiesDesk)
    }

    override fun onNewMessageChat(messageText: String) {
        // todo - message battle chat
    }

    override fun onDefend(point: Point) {
        isAttackAvailable = true
        controller.onDefend(point)
        view?.updateOwnGameDesk()
    }

    override fun sendMessage(messageText: String) {
        sendData(Message(
            type = MessageType.CHAT,
            message = messageText
        ))
    }

    override fun onAttack(point: Point) {
        if (isAttackAvailable) {
            if (controller.onAttack(point)) {
                sendData(
                    Message(
                        type = MessageType.ATTACK,
                        message = point
                    )
                )
                view?.updateEnemiesDesk()
                isAttackAvailable = false
            }
        } else {
            view?.showMessage("Wait for enemies attack!")
        }
    }

    override fun onError(error: Throwable) {
        view?.showMessage(error.message ?: "You have some bluetooth problems..")
    }

    private fun sendData(message: Message) {
        gameRepository?.sendMessage(message)
    }

    override fun onFinishGame(isWin: Boolean) {
        view?.onFinish()
    }
}
