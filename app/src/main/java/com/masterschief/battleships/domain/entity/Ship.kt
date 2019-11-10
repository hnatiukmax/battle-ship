package com.masterschief.battleships.domain.entity

import com.masterschief.battleships.presentation.utils.copy

data class Ship(
    val size : Int,
    var position: Boolean,
    var pointArray : ArrayList<Point>
) {
    fun copy() : Ship {
        return Ship(
            this.size,
            this.position,
            this.pointArray.copy()
        )
    }
}