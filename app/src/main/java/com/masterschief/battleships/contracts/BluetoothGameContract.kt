package com.masterschief.battleships.contracts

import com.masterschief.battleships.bluetooth.MessageModel

interface BluetoothGameContract {

    interface View {

        fun showMessage(message : String)
    }

    interface Presenter {

        fun attachView(view : BluetoothGameContract.View)

        fun detachView()

        fun onWrite(what : MessageModel)

    }
}