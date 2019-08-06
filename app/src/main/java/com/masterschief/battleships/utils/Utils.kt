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

fun main() {
    val size = 10
    var arrayCopy = Array(10) { IntArray(10) { 0 }}
    var arrayOfShips = mutableListOf<Ship>()

    var state = 0
    var countOfShip = 1
    for (i in SHIP_ARR) {
        if (i > 2) {
            for (c in 0 until countOfShip) {
                var array = arrayListOf<Point>()
                for (count in 0 until i) {
                    arrayCopy[count][state] = 1
                    array.add(Point(count, state))
                }
                arrayOfShips.add(Ship(i, true, array))
                state += 2
            }
            countOfShip++
        } else if (i == 2){
                var row = 0
                for (c in 0 until countOfShip) {
                    var array = arrayListOf<Point>()
                    for (count in state until (state + i)) {
                        arrayCopy[row][count] = 1
                        array.add(Point(row, count))
                    }
                    arrayOfShips.add(Ship(i, false, array))
                    row += 2
                }
                countOfShip++
        } else if (i == 1) {
            var row = 0
            for (c in 0 until countOfShip) {
                var array = arrayListOf<Point>()
                    arrayCopy[row][size-1] = 1
                array.add(Point(row, size - 1))
                arrayOfShips.add(Ship(i, true, array))
                row += 2
            }
        }
    }


    for (r in arrayCopy.indices) {
        for (c in arrayCopy[r].indices) {
            print("${arrayCopy[r][c]} ")
        }
        println()
    }

    var str = "shooted PRINT SHIPS ${arrayOfShips.size}\n"
    for (ship in arrayOfShips) {
        str += "${ship.size} ${ship.position} [${ship.pointArray.joinToString()}\n"
    }
    println()
    print(str)
}