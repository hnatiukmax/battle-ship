package com.masterschief.battleships.contracts

import com.masterschief.battleships.utils.Point

interface PrepareGameContract {

    interface View {

        fun updateDesk()

        fun setDesk(desk : Array<IntArray>)

    }

    interface Presenter {

        fun attachView(view : View)

        fun detachView()

        fun onDown(point : Point)

        fun onMove(point : Point)

        fun onUp()

        fun onRotate()

        fun onRandomlyButton()

        fun getData() : Array<IntArray>
    }

}