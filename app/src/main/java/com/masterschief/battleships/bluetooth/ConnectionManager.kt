package com.masterschief.battleships.bluetooth

import android.bluetooth.BluetoothSocket
import io.reactivex.*
import io.reactivex.Observable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectionManager(private val bSocket : BluetoothSocket) {

    private val bInStream : InputStream = bSocket.inputStream
    private val bOutStream : OutputStream = bSocket.outputStream
    private val bBuffer : ByteArray = ByteArray(1024)

    fun listen()  = Observable.create<MessageModel> {
        var numBytes : Int

        while(true) {
            numBytes = bInStream.read(bBuffer)
            val str = String(bBuffer.sliceArray(0 until numBytes))

            if (str.isNotEmpty()) {
                it.onNext(MessageModel(str, MessageType.CHAT))
            }
        }
    }

    fun write(bytes : ByteArray) = Completable.create {
        try {
            bOutStream.write(bytes)
            it.onComplete()
        } catch (ex : IOException) {
            it.onError(Throwable("Error writing"))
        }
    }
}