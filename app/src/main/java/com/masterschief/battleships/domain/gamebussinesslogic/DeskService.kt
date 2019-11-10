package com.masterschief.battleships.domain.gamebussinesslogic

import com.masterschief.battleships.domain.entity.*
import com.masterschief.battleships.domain.entity.enums.Direction
import com.masterschief.battleships.domain.entity.enums.Direction.*
import com.masterschief.battleships.presentation.utils.*

class DeskService(val sizeDesk: Int = 10) {
    var array2d = Array(sizeDesk) { IntArray(sizeDesk) { FREE_CELL } }
    var arrayOfShips = mutableListOf<Ship>()
    private lateinit var arrayOfShipsCopy: MutableList<Ship>

    private lateinit var currentShip: Ship

    init {
        getDefaultDesk(array2d, arrayOfShips)

        //setRandomDesk(array2d, arrayOfShips)
    }

    fun rotate(ship: Ship) {
        if (ship.size != 1) {
            for (point in ship.pointArray) {
                if (point.x in 0..9 && point.y in 0..9) {
                    array2d[point.y][point.x] =
                        FREE_CELL
                }
            }

            for (shipItem in arrayOfShips) {
                if (shipItem != ship) {
                    for (point in shipItem.pointArray) {
                        array2d[point.y][point.x] =
                            WHOLE
                    }
                }
            }


            val newPoints = arrayListOf<Point>()
            for ((index, value) in ship.pointArray.withIndex()) {
                newPoints.add(
                    Point(
                        if (ship.position) ship.pointArray[0].y else (index + value.y),
                        if (ship.position) (index + value.x) else ship.pointArray[0].x
                    )
                )
            }
            ship.position = !ship.position
            ship.pointArray = newPoints

            if (isCorrectState(
                    ship,
                    array2d
                )
            ) {
                for (point in ship.pointArray) {
                    if (point.x in 0..9 && point.y in 0..9) {
                        array2d[point.y][point.x] =
                            CHOSEN
                    }
                }
            } else {
                for (point in ship.pointArray) {
                    if (point.x in 0..9 && point.y in 0..9) {
                        array2d[point.y][point.x] =
                            BESIDE
                    }
                }
            }
        }
    }

    fun endOfMove(ship: Ship) {
        val array = shipsToArray(ship)
        //log("3.0 endOfMove", "${isCorrectState(ship, array)}")

        if (!isCorrectState(ship, array)) {
            arrayOfShips = arrayOfShipsCopy
            for ((r, value) in array2d.withIndex()) {
                for (c in value.indices) {
                    array2d[r][c] = FREE_CELL
                }
            }

            for (shipItem in arrayOfShips) {
                for (point in shipItem.pointArray) {
                    array2d[point.y][point.x] =
                        WHOLE
                }
            }
        } else {
            arrayOfShipsCopy = arrayOfShips.copy()
        }
        //printArr("3.0 after true", array2d)
    }


    //maybe true

    private fun shipsToArray(ship: Ship): Array<IntArray> {
        var array2d = Array(sizeDesk) { IntArray(sizeDesk) { FREE_CELL } }

        for (shipItem in arrayOfShips) {
            if (shipItem != ship) {
                for (point in shipItem.pointArray) {
                    array2d[point.y][point.x] =
                        WHOLE
                }
            }
        }

        return array2d
    }

    fun move(dir: Direction, ship: Ship) {
        for (point in ship.pointArray) {
            array2d[point.y][point.x] =
                FREE_CELL
        }

        for (shipItem in arrayOfShips) {
            if (shipItem != ship) {
                for (point in shipItem.pointArray) {
                    array2d[point.y][point.x] =
                        WHOLE
                }
            }
        }



        for (point in ship.pointArray) {
            when (dir) {
                Direction.RIGHT -> point.x += 1
                LEFT -> point.x -= 1
                UP -> point.y -= 1
                DOWN -> point.y += 1
            }
            //array2d[point.y][point.x] = CHOSEN
        }

        if (isCorrectState(ship, array2d)) {
            for (point in ship.pointArray) {
                array2d[point.y][point.x] =
                    CHOSEN
            }
        } else {
            for (point in ship.pointArray) {
                array2d[point.y][point.x] =
                    BESIDE
            }
        }
        printArr("move", array2d)
    }

    fun mix() {
        array2d = Array(sizeDesk) { IntArray(sizeDesk) { FREE_CELL } }
        arrayOfShips = mutableListOf()

        setRandomDesk(array2d, arrayOfShips)
    }

    fun makeUnselected(unselectedShip: Ship) {
        val index = arrayOfShips.indexOf(unselectedShip)
        if (index > -1) {
            this.setShipState(index, WHOLE)
        }
    }

    fun makeSelected(chosenPoint: Point): Ship? {
        var selectedShip: Ship? = null
        for (ship in arrayOfShips) {
            if (ship.pointArray.contains(chosenPoint)) {
                selectedShip = ship
                for (point in ship.pointArray) {
                    array2d[point.y][point.x] =
                        CHOSEN
                }
            } else {
                for (point in ship.pointArray) {
                    array2d[point.y][point.x] =
                        WHOLE
                }
            }
        }
        selectedShip?.let {
            currentShip = it.copy()
        }
        arrayOfShipsCopy = arrayOfShips.copy()
        return selectedShip
    }

    private fun setShipState(index: Int, STATE: Int) {
        for (point in arrayOfShips[index].pointArray) {
            array2d[point.y][point.x] = STATE
        }
    }

}