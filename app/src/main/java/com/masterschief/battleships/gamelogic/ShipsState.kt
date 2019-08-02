package com.masterschief.battleships.gamelogic

const val FREE_CELL = 0
const val WHOLE = 1
const val CHOSEN = 2
const val SHOOTED = 3
const val DESTROYED = 4
const val BESIDE = 5

enum class Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT
}
