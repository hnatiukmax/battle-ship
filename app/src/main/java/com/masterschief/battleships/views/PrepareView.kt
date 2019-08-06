package com.masterschief.battleships.views


import android.content.Intent
import android.databinding.DataBindingUtil

import android.os.Bundle
import android.os.Handler

import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.masterschief.battleships.R
import com.masterschief.battleships.activities.FullScreenActivity
import com.masterschief.battleships.activities.MainActivity

import com.masterschief.battleships.databinding.ActivityPrepareViewBinding
import com.masterschief.battleships.gamelogic.*
import com.masterschief.battleships.uigame.GameDeskContract
import com.masterschief.battleships.utils.Point

import com.masterschief.battleships.utils.log

import com.masterschief.battleships.gamelogic.Direction.*

import com.masterschief.battleships.utils.printArr


class PrepareView : FullScreenActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        v?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))

        var intent: Intent? = null
        when (v?.id) {
            R.id.button_auto -> {
                currentShip = null
                deskService.mix()
                prepareViewDesk.initDesk(deskService.array2d)
                prepareViewDesk.updateDesk()
                printArr("end", deskService.array2d)
            }
            R.id.button_next -> {
                when (getIntent().getSerializableExtra("type")) {
                }
            }
            R.id.button_back -> {
                intent = Intent(this, MainActivity::class.java)
            }
            R.id.textView_rotate -> {
                currentShip?.let {
                    deskService.rotate(it)
                    prepareViewDesk.updateDesk()
                }
                Handler().postDelayed( {
                    deskService.endOfMove(currentShip!!)
                    prepareViewDesk.updateDesk()},
                    200
                )
                printArr("end", deskService.array2d)

            }
        }
        intent?.let {
            startActivity(it)
        }
    }

    private lateinit var binding: ActivityPrepareViewBinding
    private lateinit var deskService: DeskService

    private var currentShip: Ship? = null

    //UI
    private lateinit var prepareViewDesk: GameDeskContract.PrepareDesk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prepare_view)

        initUI()
    }

    private fun initUI() {
        deskService = DeskService()

        prepareViewDesk = binding.gameDesk as GameDeskContract.PrepareDesk
        prepareViewDesk.initDesk(deskService.array2d)
        (prepareViewDesk as View).setOnTouchListener(listener)

        binding.buttonAuto?.setOnClickListener(this)
        binding.buttonNext?.setOnClickListener(this)
        binding.buttonBack?.setOnClickListener(this)
        binding.textViewRotate?.setOnClickListener(this)
    }

    private val listener = View.OnTouchListener { v, event ->
        val x = (event.x / (v.width / 10)).toInt()
        val y = (event.y / (v.width / 10)).toInt()

        if ((x >= 10 || x < 0) || (y >= 10 || y < 0)) {
            return@OnTouchListener false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN // нажатие
            -> when {
                deskService.array2d[y][x] == WHOLE -> {

                    currentShip = deskService.makeSelected(Point(y, x))
                    prepareViewDesk.updateDesk()
                }

                deskService.array2d[y][x] != CHOSEN && currentShip != null -> {
                    deskService.makeUnselected(currentShip!!)
                    currentShip = null
                    prepareViewDesk.updateDesk()
                }

            }
            MotionEvent.ACTION_MOVE // движение
            -> {
                if (currentShip != null && !currentShip?.pointArray?.contains(Point(y, x))!!) {
                    val direction = if (currentShip?.position!!) when {
                        x > currentShip?.pointArray?.get(0)?.x!! -> RIGHT
                        x < currentShip?.pointArray?.get(0)?.x!! -> LEFT
                        y > currentShip?.pointArray?.last()?.y!! -> DOWN
                        else -> UP
                    } else when {
                        x > currentShip?.pointArray?.last()?.x!! -> RIGHT
                        x < currentShip?.pointArray?.get(0)?.x!! -> LEFT
                        y > currentShip?.pointArray?.get(0)?.y!! -> DOWN
                        else -> UP
                    }

                    deskService.move(direction, currentShip!!)
                    prepareViewDesk.updateDesk()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> { // отпускание
                currentShip?.let {
                    deskService.endOfMove(it)
                    prepareViewDesk.updateDesk()
                }
                printArr("end", deskService.array2d)
            }
        }

        return@OnTouchListener true
    }
}
