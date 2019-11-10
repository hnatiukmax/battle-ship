package com.masterschief.battleships.bluetooth

import android.bluetooth.BluetoothSocket
import com.masterschief.battleships.bluetooth.bluetoothdataadapter.toByteArray
import com.masterschief.battleships.bluetooth.bluetoothdataadapter.toMessage
import com.masterschief.battleships.domain.entity.Message
import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.domain.entity.enums.MessageType
import com.masterschief.battleships.domain.eventlisteners.GameEventListener
import com.masterschief.battleships.domain.repository.GameRepository
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception

class BluetoothGameRepository(
    private val socket : BluetoothSocket,
    private val eventListener: GameEventListener
) : GameRepository {

    private val inStream : InputStream = socket.inputStream
    private val outStream : OutputStream = socket.outputStream
    private val buffer : ByteArray = ByteArray(1024)

    init {
        listen()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    when(result.type) {
                        MessageType.INIT -> eventListener.onInit(result.message as Array<IntArray>)
                        MessageType.ATTACK -> eventListener.onDefend(result.message as Point)
                        MessageType.CHAT -> eventListener.onNewMessageChat(result.message as String)
                    }
                },
                { error ->
                    eventListener.onError(error)
                })
    }

    override fun sendMessage(message: Message) {
        write(message.toByteArray())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                eventListener.onError(Throwable("Not send"))
            })
    }

    private fun listen()  = Observable.create<Message> {
        var numBytes : Int

        while(true) {
            numBytes = inStream.read(buffer)

           try {
               it.onNext(buffer.sliceArray(0 until  numBytes).toMessage())
           } catch (e : Exception) {
               it.onError(e)
           }
        }
    }

    private fun write(bytes : ByteArray) = Completable.create {
        try {
            outStream.write(bytes)
            it.onComplete()
        } catch (ex : IOException) {
            it.onError(Throwable("Error writing"))
        }
    }
}