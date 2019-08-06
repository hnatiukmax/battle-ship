package com.masterschief.battleships.gamelogic

import com.masterschief.battleships.utils.Point
import com.masterschief.battleships.utils.copy

data class Ship(
    val size : Int,
    var position: Boolean,
    var pointArray : ArrayList<Point>
) {
    fun copy() : Ship {
        return Ship(this.size, this.position, this.pointArray.copy())
    }
}

fun main() {
    var ship = Ship(3, false, arrayListOf(Point(7,7), Point(7,8), Point(7,9)))

    val state = arrayListOf<Point>()
    for ((index, value) in ship.pointArray.withIndex()) {
        state.add(Point((if (ship.position) ship.pointArray[0].y else (index + value.y)), if (ship.position) (index + value.x) else ship.pointArray[0].x))
    }

    print(state.joinToString())


}