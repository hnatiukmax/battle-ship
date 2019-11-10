package com.masterschief.battleships.presentation.app

import android.app.Application
import android.bluetooth.BluetoothSocket

class MyApplication : Application() {
    companion object {
        val instance = MyApplication()
    }

    var currentSocket : BluetoothSocket? = null


    override fun onCreate() {
        super.onCreate()

    }
}