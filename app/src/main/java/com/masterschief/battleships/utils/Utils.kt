package com.masterschief.battleships.utils

import android.util.Log

private const val appTag = "eulerSquare"

fun log(message: String) {
    Log.i(appTag, message)
}

fun printArr(array : Array<IntArray>) {
    var str = "shooted \n"
    array.forEach {
        for (i in it) {
            str += "$i  "
        }
        str += '\n'
    }
    log(str)
}