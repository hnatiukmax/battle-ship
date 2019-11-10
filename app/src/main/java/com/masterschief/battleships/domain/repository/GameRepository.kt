package com.masterschief.battleships.domain.repository

import com.masterschief.battleships.domain.entity.Message
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*

interface GameRepository {

    fun sendMessage(message : Message)

}