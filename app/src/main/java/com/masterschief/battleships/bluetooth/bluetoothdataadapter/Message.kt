package com.masterschief.battleships.bluetooth.bluetoothdataadapter

import com.masterschief.battleships.domain.entity.Message
import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.domain.entity.enums.MessageType

fun Message.toByteArray() =
    byteArrayOf(type.numType).plus(when(type) {
        MessageType.INIT -> (message as Array<IntArray>).toByteArray()
        MessageType.ATTACK -> with((message as Point)) {
            byteArrayOf(x.toByte(), y.toByte())
        }
        MessageType.CHAT -> (message as String).toByteArray()
    })