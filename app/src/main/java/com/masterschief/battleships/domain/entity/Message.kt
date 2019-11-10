package com.masterschief.battleships.domain.entity

import com.masterschief.battleships.domain.entity.enums.MessageType

data class Message(
    val message : Any,
    val type : MessageType
)