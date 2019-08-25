package com.masterschief.battleships.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.AsyncTask
import android.os.Handler
import com.masterschief.battleships.utils.MY_UUID
import com.masterschief.battleships.utils.log
import io.reactivex.Single
import java.io.IOException
import java.util.*


fun waitToConnect(): Single<BluetoothSocket> = Single.create {
    var bServerSocket: BluetoothServerSocket? = null
    try {
        bServerSocket = BluetoothAdapter
            .getDefaultAdapter()
            .listenUsingRfcommWithServiceRecord("BluetoothChat", UUID.fromString(MY_UUID))
    } catch (ex: IOException) {
        it.onError(Throwable("Creating ServerSocket Error"))
    }

    var bSocket: BluetoothSocket? = null

    while (true) {
        try {
            bSocket = bServerSocket?.accept()
        } catch (ex: IOException) {
            log("Accept Error", ex.message!!)
        }

        if (bSocket != null) {
            bServerSocket?.close()

            it.onSuccess(bSocket)
        }
    }
}

fun connectToOpponent(device : BluetoothDevice) : Single<BluetoothSocket> = Single.create {

    class AsyncConnectThread : AsyncTask<BluetoothDevice, Void, BluetoothSocket>() {
        private lateinit var bSocket : BluetoothSocket

        override fun doInBackground(vararg params: BluetoothDevice?): BluetoothSocket {
            bSocket = params[0]!!.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID))

            BluetoothAdapter.getDefaultAdapter().cancelDiscovery()

            try {
                bSocket.connect()
            } catch (ex : IOException) {
                bSocket.close()
            }

            return bSocket
        }

        override fun onPostExecute(result: BluetoothSocket?) {
            super.onPostExecute(result)
            result?.let { result ->
                if (bSocket.isConnected) {
                    it.onSuccess(result)
                } else {
                    it.onError(Throwable("Can't connect"))
                }
            }
        }
    }

    val async = AsyncConnectThread()
    async.execute(device)
}
