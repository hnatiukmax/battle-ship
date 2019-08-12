package com.masterschief.battleships.utils

import android.util.Log
import com.masterschief.battleships.gamelogic.SHIP_ARR
import com.masterschief.battleships.gamelogic.Ship

private const val appTag = "eulerSquare"

fun log(tag : String, message: String) {
    Log.i(appTag, "$tag $message")
}

fun printArr(tag : String, array : Array<IntArray>) {
    var str = "shooted PRINT ARRAY 2D\n"
    array.forEach {
        for (i in it) {
            str += "$i  "
        }
        str += '\n'
    }
    log(tag, str)
}

fun printShips(tag : String, ships : List<Ship>) {
    var str = "shooted PRINT SHIPS ${ships.size}\n"
    for (ship in ships) {
        str += "${ship.size} ${ship.position} [${ship.pointArray.joinToString()}\n"
    }

    log(tag, str)
}

fun Array<IntArray>.copy() : Array<IntArray> {
    var arrayCopy = Array(this.size) { IntArray(this.size) { 0 }}

    for ((r, intArr) in this.withIndex()) {
        for ((c, value) in intArr.withIndex()) {
            arrayCopy[r][c] = value
        }
    }
    return arrayCopy
}

fun ArrayList<Point>.copy() : ArrayList<Point>{
    var copyArray = ArrayList<Point>()
    this.forEach {
        copyArray.add(Point(it.y, it.x))
    }
    return copyArray
}

fun MutableList<Ship>.copy() : MutableList<Ship> {
    var mutableListCopy = mutableListOf<Ship>()

    for (ship in this) {
        mutableListCopy.add(ship.copy())
    }

    return mutableListCopy
}