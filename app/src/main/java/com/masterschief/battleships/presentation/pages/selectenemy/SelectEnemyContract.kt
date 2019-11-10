package com.masterschief.battleships.presentation.pages.selectenemy

import android.app.Activity
import com.masterschief.battleships.presentation.pages.base.BasePresenter
import com.masterschief.battleships.presentation.pages.base.BaseView

interface SelectEnemyContract {

    interface View : BaseView {

        fun updateDevicesList(device : String)

        fun searchingProcessShow()

        fun handlerInvisible()

        fun turnOnOffSearchView(toggle : Boolean)

        fun statusHide(toggle : Boolean)

        fun statusSet(message : String)

        fun acceptProcessShow()

        fun getActivity() : Activity

        fun toGameActivity()
    }

    interface Presenter : BasePresenter {

        fun onSearchOpponents()

        fun onCreateGame()

        fun onStop()

        fun onListViewClick(position : Int)
    }
}