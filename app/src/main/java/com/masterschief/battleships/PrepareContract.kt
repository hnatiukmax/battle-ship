package com.masterschief.battleships

import com.masterschief.battleships.utils.Point

interface PrepareContract {

    interface ViewContract {

        fun updateDesk()

        fun setDesk(desk : Array<IntArray>)

    }

    interface PresenterContract {

        fun attachView(view : ViewContract)

        fun detachView()

        fun onDown(point : Point)

        fun onMove(point : Point)

        fun onUp()

        fun onRotate()

        fun onRandomlyButton()
    }

}