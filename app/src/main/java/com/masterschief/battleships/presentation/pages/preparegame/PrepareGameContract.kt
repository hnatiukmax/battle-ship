package com.masterschief.battleships.presentation.pages.preparegame

import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.presentation.pages.base.BasePresenter
import com.masterschief.battleships.presentation.pages.base.BaseView

interface PrepareGameContract {

    interface View : BaseView {

        fun updateDesk()

        fun setDesk(desk : Array<IntArray>)

        fun getNext(desk : Array<IntArray>)
    }

    interface Presenter : BasePresenter{

        fun onDown(point : Point)

        fun onMove(point : Point)

        fun onUp()

        fun onRotate()

        fun onRandomlyClick()

        fun onNextClick()
    }

}