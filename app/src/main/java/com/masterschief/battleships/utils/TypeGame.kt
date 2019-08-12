package com.masterschief.battleships.utils

import java.io.Serializable

enum class TypeGame : Serializable {
    SINGLE_GAME,
    BLUETOOTH_GAME,
    TWO_PLAYERS_GAME,
    NETWORK_GAME
}