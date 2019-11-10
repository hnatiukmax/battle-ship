package com.masterschief.battleships.bluetooth.bluetoothdataadapter

import com.masterschief.battleships.domain.entity.FREE_CELL
import com.masterschief.battleships.domain.entity.Message
import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.domain.entity.enums.MessageType
import kotlin.math.sqrt

fun ByteArray.toMessage() : Message {
    val type = MessageType.getTypeByNum(this[0])
    val dataByte = sliceArray(1 until size)
    val message : Any = when(type) {
        MessageType.INIT -> dataByte.toArrayOfIntArray()
        MessageType.ATTACK -> Point(dataByte[1].toInt(), dataByte[0].toInt())
        MessageType.CHAT -> String(dataByte)
        else -> IllegalArgumentException("Can't convert to message")
    }
    return Message(message, type)
}

fun ByteArray.toArrayOfIntArray() : Array<IntArray> {
    val sizeResultArray = sqrt(size.toDouble()).toInt()
    val resultArray = Array(sizeResultArray) { IntArray(sizeResultArray) { FREE_CELL } }

    for ((index, element) in this.withIndex()) {
        resultArray[index / sizeResultArray][index % sizeResultArray] = element.toInt()
    }

    return resultArray
}