package com.masterschief.battleships.domain.entity.enums

enum class MessageType(val numType : Byte) {
    INIT(0),
    ATTACK(1),
    CHAT(2);

    companion object {
        fun getTypeByNum(numType : Byte) =
            when(numType) {
                0.toByte() -> INIT
                1.toByte() -> ATTACK
                else -> CHAT
            }
    }
}