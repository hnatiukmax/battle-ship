package com.masterschief.battleships.gamelogic

import com.masterschief.battleships.utils.Point
import com.masterschief.battleships.utils.log
import com.masterschief.battleships.gamelogic.Direction.*

class DeskService(val sizeDesk: Int = 10) {
    var array = Array(sizeDesk) { IntArray(sizeDesk) { FREE_CELL } }
    private var arrayOfShips = mutableListOf<Ship>()

    private val SHIP_ARR = intArrayOf(4, 3, 2, 1)

    init {
        array[2][2] = 1
        array[2][3] = 1
        array[2][4] = 1
        array[2][5] = 1
        arrayOfShips.add(Ship(4, false, arrayListOf(Point(2,2), Point(2,3), Point(2,4), Point(2,5))))

//        this.setRandomDesk()
    }

    fun mix() {
        array = Array(sizeDesk) { IntArray(sizeDesk) { FREE_CELL } }
        arrayOfShips = mutableListOf<Ship>()

        this.setRandomDesk()
    }

    fun move(dir : Direction, ship : Ship) {
        for (point in ship.pointArray) {
            array[point.y][point.x] = 0
        }

        for (point in ship.pointArray) {
            when(dir) {
                RIGHT -> point.x++
                LEFT -> point.x--
                UP ->  point.y--
                DOWN -> point.y++
            }
            array[point.y][point.x] = CHOSEN
        }
    }

    fun unChosen(ship : Ship) {
        for (i in ship.pointArray) {
            array[i.y][i.x] = WHOLE
        }
    }

    fun setChosen(chosenPoint: Point) : Ship? {
        var selectedShip : Ship? = null
        for (ship in arrayOfShips) {
            if (ship.pointArray.contains(chosenPoint)) {
                selectedShip = ship
                for (point in ship.pointArray) {
                    array[point.y][point.x] = CHOSEN
                }
            } else {
                for (point in ship.pointArray) {
                    array[point.y][point.x] = WHOLE
                }
            }
        }
        return selectedShip?.copy()
    }

    private fun setRandomDesk() {
        for (ship in 4 downTo 1) {
            repeat(SHIP_ARR[ship - 1]) {

                loop@ while (true) {
                    val position = randomBool()
                    val pArray = arrayListOf<Point>()
                    val valToSize = randomCell(0)
                    val valToShip = randomCell(ship - 1)

                    val checkToShip = if (valToShip > 0) valToShip - 1 else valToShip

                    var checkUntil = when {
                        valToShip + ship == sizeDesk - 1 -> 10
                        (valToShip + ship + 1) <= (sizeDesk - 1) -> valToShip + ship + 1
                        else -> valToShip + ship
                    }


                    //according to position
                    if (position) {
                        for (i in checkToShip until checkUntil) {
                            if ((valToSize > 0 && array[i][valToSize - 1] != FREE_CELL) || //left
                                (valToSize < (sizeDesk - 1) && array[i][valToSize + 1] != FREE_CELL) || //right
                                array[i][valToSize] != FREE_CELL
                            ) //main
                                continue@loop
                        }

                        //true
                        for (i in valToShip until (valToShip + ship)) {
                            array[i][valToSize] = WHOLE
                            pArray.add(Point(i, valToSize))
                        }
                        arrayOfShips.add(Ship(ship, position, pArray))
                        break@loop

                    } else {
                        for (i in checkToShip until checkUntil) {
                            if ((valToSize > 0 && array[valToSize - 1][i] != FREE_CELL) || //top
                                (valToSize < (sizeDesk - 1) && array[valToSize + 1][i] != FREE_CELL) || //bottom
                                array[valToSize][i] != FREE_CELL
                            ) //main
                                continue@loop
                        }

                        //true
                        for (i in valToShip until (valToShip + ship)) {
                            array[valToSize][i] = WHOLE
                            pArray.add(Point(valToSize, i))
                        }
                        arrayOfShips.add(Ship(ship, position, pArray))
                        break@loop
                    }
                }

            }
        }
    }


    private fun randomBool() = Math.random() > 0.5

    private fun randomCell(shipSize: Int) = (Math.random() * (sizeDesk - shipSize)).toInt()
}