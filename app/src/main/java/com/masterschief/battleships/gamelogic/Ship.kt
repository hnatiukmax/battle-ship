package com.masterschief.battleships.gamelogic

import com.masterschief.battleships.utils.Point

data class Ship(
    val size : Int,
    var position: Boolean,
    var pointArray : ArrayList<Point>
)