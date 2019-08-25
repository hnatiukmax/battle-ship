package com.masterschief.battleships.contracts

import android.app.Activity

interface PrepareBluetoothGameContract {

    interface View {

        fun getContext() : Activity

        fun updateDevicesList()

        fun setDevicesList(array : ArrayList<String>)

        fun showMessage(message : String)

        fun searchingProccesShow()

        fun handlerInvisible()

        fun turnOnOffSearchView(toggle : Boolean)

        fun statusHide(toggle : Boolean)

        fun statusSet(message : String)

        fun acceptProccesShow()
    }

    interface Presenter {

        fun attachView(view : View)

        fun detachView()

        fun onSearchOpponents()

        fun onCreateGame()

        fun onDestroy()

        fun onListViewClick(position : Int)
    }
}