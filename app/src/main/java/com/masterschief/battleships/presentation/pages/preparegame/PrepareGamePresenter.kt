package com.masterschief.battleships.presentation.pages.preparegame

import android.os.Handler
import com.masterschief.battleships.domain.entity.*
import com.masterschief.battleships.domain.entity.enums.Direction
import com.masterschief.battleships.domain.gamebussinesslogic.*
import com.masterschief.battleships.presentation.pages.base.BaseView
import com.masterschief.battleships.presentation.utils.printArr

class PrepareGamePresenter : PrepareGameContract.Presenter {

    private var currentShip: Ship? = null
    private var view: PrepareGameContract.View? = null
    private var deskService: DeskService = DeskService()

    override fun attachView(view: BaseView) {
        if (view is PrepareGameContract.View) {
            this.view = view
            view.setDesk(deskService.array2d)
            view.updateDesk()
        }
    }

    override fun detachView() {
        this.view = null
    }

    override fun onNextClick() {
        view?.getNext(deskService.array2d)
    }

    override fun onDown(point: Point) {
        when {
            deskService.array2d[point.y][point.x] == WHOLE -> {

                currentShip = deskService.makeSelected(point)
                view?.updateDesk()
            }

            deskService.array2d[point.y][point.x] != CHOSEN && currentShip != null -> {
                deskService.makeUnselected(currentShip!!)
                currentShip = null
                view?.updateDesk()
            }
        }
    }

    override fun onMove(point: Point) {
        if (currentShip != null && !currentShip?.pointArray?.contains(point)!!) {
            val direction = if (currentShip?.position!!) when {
                point.x > currentShip?.pointArray?.get(0)?.x!! -> Direction.RIGHT
                point.x < currentShip?.pointArray?.get(0)?.x!! -> Direction.LEFT
                point.y > currentShip?.pointArray?.last()?.y!! -> Direction.DOWN
                else -> Direction.UP
            } else when {
                point.x > currentShip?.pointArray?.last()?.x!! -> Direction.RIGHT
                point.x < currentShip?.pointArray?.get(0)?.x!! -> Direction.LEFT
                point.y > currentShip?.pointArray?.get(0)?.y!! -> Direction.DOWN
                else -> Direction.UP
            }

            deskService.move(direction, currentShip!!)
            view?.updateDesk()
        }
    }

    override fun onUp() {
        currentShip?.let {
            deskService.endOfMove(it)
            view?.updateDesk()
        }
        printArr("end", deskService.array2d)
    }

    override fun onRotate() {
        currentShip?.let {
            deskService.rotate(it)
            view?.updateDesk()

            Handler().postDelayed(
                {
                    deskService.endOfMove(currentShip!!)
                    view?.updateDesk()
                },
                200
            )
        }
        printArr("end", deskService.array2d)
    }

    override fun onRandomlyClick() {
        deskService.mix()

        view?.setDesk(deskService.array2d)
        view?.updateDesk()
    }
}