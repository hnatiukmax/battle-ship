package com.masterschief.battleships.bluetooth

data class MessageModel(val message : String, val type : MessageType)

enum class MessageType {
    MOVE,
    CHAT
}