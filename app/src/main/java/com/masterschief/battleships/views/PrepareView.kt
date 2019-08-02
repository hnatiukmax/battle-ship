package com.masterschief.battleships.views

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.databinding.DataBindingUtil
import android.drm.DrmStore
import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.masterschief.battleships.R
import com.masterschief.battleships.activities.FullScreenActivity
import com.masterschief.battleships.activities.MainActivity
import com.masterschief.battleships.databinding.ActivityMainBinding
import com.masterschief.battleships.databinding.ActivityPrepareViewBinding
import com.masterschief.battleships.gamelogic.*
import com.masterschief.battleships.uigame.GameDeskContract
import com.masterschief.battleships.utils.Point
import com.masterschief.battleships.utils.TypeGame
import com.masterschief.battleships.utils.log
import kotlin.system.exitProcess
import com.masterschief.battleships.gamelogic.Direction.*
import android.databinding.adapters.TextViewBindingAdapter.setText
import com.masterschief.battleships.utils.printArr


class PrepareView : FullScreenActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        v?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))

        var intent: Intent? = null
        when (v?.id) {
            R.id.button_auto -> {
                deskService.mix()
                prepareViewDesk.initDesk(deskService.array)
                prepareViewDesk.updateDesk()
                printArr(deskService.array)
            }
            R.id.button_next -> {
                when (getIntent().getSerializableExtra("type")) {

                }
            }
            R.id.button_back -> {
                intent = Intent(this, MainActivity::class.java)
            }

        }
        intent?.let {
            startActivity(it)
        }
    }

    private lateinit var binding: ActivityPrepareViewBinding
    private lateinit var deskService: DeskService

    private var currentShip: Ship? = null

    var sDown: String? = null
    var sMove: String? = null
    var sUp: String? = null

    var cp: Int? = null
    var cy: Int? = null

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
        prepareViewDesk.initDesk(deskService.array)
        (prepareViewDesk as View).setOnTouchListener(listener)

        binding.buttonAuto?.setOnClickListener(this)
        binding.buttonNext?.setOnClickListener(this)
        binding.buttonBack?.setOnClickListener(this)
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
                deskService.array[y][x] == WHOLE -> {

                    currentShip = deskService.setChosen(Point(y, x))
                    prepareViewDesk.updateDesk()
                    //printArr(deskService.array)
                }

                deskService.array[y][x] == CHOSEN -> {
                    //rotate
                }

                currentShip != null -> {
                    deskService.unChosen(currentShip!!)
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

                    log("shooted click | direction = $direction ")
                    deskService.move(direction, currentShip!!)
                    prepareViewDesk.updateDesk()
                    printArr(deskService.array)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> { // отпускание

            }
        }

        binding.textViewPrepareUp?.text = sDown + "\n" + sMove + "\n" + sUp
        return@OnTouchListener true
    }
}
