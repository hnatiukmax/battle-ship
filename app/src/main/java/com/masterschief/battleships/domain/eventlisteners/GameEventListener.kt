package com.masterschief.battleships.domain.eventlisteners

import com.masterschief.battleships.domain.entity.Message
import com.masterschief.battleships.domain.entity.Point
import java.lang.Exception

interface GameEventListener {

    fun onInit(enemiesDesk : Array<IntArray>)

    fun onNewMessageChat(messageText : String)

    fun onDefend(point : Point)

    fun onError(error : Throwable)
}