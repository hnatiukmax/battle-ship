package com.masterschief.battleships.bluetooth.bluetoothdataadapter

fun Array<IntArray>.toByteArray() : ByteArray {
    val resultArray = ByteArray(size * size)

    for ((i, rowArray) in this.withIndex()) {
        for ((j, element) in rowArray.withIndex()) {
            resultArray[i*size+j] = element.toByte()
        }
    }
    return resultArray
}