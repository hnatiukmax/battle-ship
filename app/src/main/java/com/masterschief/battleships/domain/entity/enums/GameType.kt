package com.masterschief.battleships.domain.entity.enums

import java.io.Serializable

enum class GameType : Serializable {
    SINGLE_GAME,
    BLUETOOTH_GAME,
    TWO_PLAYERS_GAME,
    NETWORK_GAME
}