package com.masterschief.battleships.gamelogic

import com.masterschief.battleships.utils.Point
import com.masterschief.battleships.utils.copy

 val SHIP_ARR = intArrayOf(4, 3, 2, 1)
private const val sizeDesk = 10

fun setRandomDesk(array2d : Array<IntArray>, arrayOfShips : MutableList<Ship>) {
    for (ship in 4 downTo 1) {
        repeat(SHIP_ARR[ship - 1]) {

            loop@ while (true) {
                val position = randomBool()
                val pArray = arrayListOf<Point>()
                val valToSize = randomCell(0)
                val valToShip = randomCell(ship - 1)

                if (isCorrectStateFor(position, Point(valToSize, valToShip), ship, array2d)) {
                    if (position) {
                        for (i in valToShip until (valToShip + ship)) {
                            array2d[i][valToSize] = WHOLE
                            pArray.add(Point(i, valToSize))
                        }
                        arrayOfShips.add(Ship(ship, position, pArray))
                        break@loop
                    } else {
                        //true
                        for (i in valToShip until (valToShip + ship)) {
                            array2d[valToSize][i] = WHOLE
                            pArray.add(Point(valToSize, i))
                        }
                        arrayOfShips.add(Ship(ship, position, pArray))
                        break@loop
                    }
                }
            }


        }
    }

}

fun isCorrectStateFor(position: Boolean, point: Point, shipSize: Int, array2d: Array<IntArray>): Boolean {
    val valToSize = point.y
    val valToShip = point.x

    val checkToShip = if (valToShip > 0) valToShip - 1 else valToShip

    var checkUntil = when {
        valToShip + shipSize == sizeDesk - 1 -> 10
        (valToShip + shipSize + 1) <= (sizeDesk - 1) -> valToShip + shipSize + 1
        else -> valToShip + shipSize
    }

    //according to position
    if (position) {
        for (i in checkToShip until checkUntil) {
            if ((valToSize > 0 && array2d[i][valToSize - 1] != FREE_CELL) || //left
                (valToSize < (sizeDesk - 1) && array2d[i][valToSize + 1] != FREE_CELL) || //right
                array2d[i][valToSize] != FREE_CELL
            ) //main
                return false
        }

        return true

    } else {
        for (i in checkToShip until checkUntil) {
            if ((valToSize > 0 && array2d[valToSize - 1][i] != FREE_CELL) || //top
                (valToSize < (sizeDesk - 1) && array2d[valToSize + 1][i] != FREE_CELL) || //bottom
                array2d[valToSize][i] != FREE_CELL
            ) //main
                return false
        }
        return true
    }
}



private fun randomBool() = Math.random() > 0.5

private fun randomCell(shipSize: Int) = (Math.random() * (sizeDesk - shipSize)).toInt()


fun isCorrectState(ship : Ship, array2d : Array<IntArray>): Boolean {
    for (point in ship.pointArray) {
        if (point.x !in 0..9 || point.y !in 0..9) return false
    }

    var checkArray = array2d.copy()
    val position = ship.position
    val shipSize = ship.size
    val point = ship.pointArray[0]

    if (position) {
        for (i in (point.y - 1)..(point.y + shipSize)) {
            if (i in 0..9) {
                if ((point.x - 1 >= 0 && checkArray[i][point.x - 1] != FREE_CELL) ||
                    (point.x + 1 < 10 && checkArray[i][point.x + 1] != FREE_CELL) ||
                    checkArray[i][point.x] != FREE_CELL
                ) {
                    return false
                }
            }
        }
        return true
    } else {
        for (i in (point.x - 1)..(point.x + shipSize)) {
            if (i in 0..9) {
                if ((point.y - 1 >= 0 && checkArray[point.y - 1][i] != FREE_CELL) ||
                    (point.y + 1 < 10 && checkArray[point.y + 1][i] != FREE_CELL) ||
                    checkArray[point.y][i] != FREE_CELL
                ) {
                    return false
                }
            }
        }
        return true
    }
}

fun getDefaultDesk(arrayCopy : Array<IntArray>, arrayOfShips : MutableList<Ship>) {
    val size = 10

    var state = 0
    var countOfShip = 1
    for (i in SHIP_ARR) {
        if (i > 2) {
            for (c in 0 until countOfShip) {
                var array = arrayListOf<Point>()
                for (count in 0 until i) {
                    arrayCopy[count][state] = 1
                    array.add(Point(count, state))
                }
                arrayOfShips.add(Ship(i, true, array))
                state += 2
            }
            countOfShip++
        } else if (i == 2){
            var row = 0
            for (c in 0 until countOfShip) {
                var array = arrayListOf<Point>()
                for (count in state until (state + i)) {
                    arrayCopy[row][count] = 1
                    array.add(Point(row, count))
                }
                arrayOfShips.add(Ship(i, false, array))
                row += 2
            }
            countOfShip++
        } else if (i == 1) {
            var row = 0
            for (c in 0 until countOfShip) {
                var array = arrayListOf<Point>()
                arrayCopy[row][size-1] = 1
                array.add(Point(row, size - 1))
                arrayOfShips.add(Ship(i, true, array))
                row += 2
            }
        }
    }
}
