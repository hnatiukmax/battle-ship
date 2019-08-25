package com.masterschief.battleships.activities

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.masterschief.battleships.R
import com.masterschief.battleships.databinding.ActivityMainBinding
import com.masterschief.battleships.utils.TypeGame
import com.masterschief.battleships.views.PrepareView
import com.masterschief.battleships.views.SingleGameView
import kotlin.system.exitProcess

class MainActivity : FullScreenActivity(), View.OnClickListener {
    /*
        onClick methods
        button's function
    */
    override fun onClick(v: View?) {
        v?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))

        var nextIntent : Intent? = null
        when(v?.id) {
            R.id.button_single -> {
                nextIntent = Intent(this, PrepareView::class.java)
                nextIntent.putExtra("type", TypeGame.SINGLE_GAME)
            }
            R.id.button_two_players ->
                nextIntent = Intent(this, SingleGameView::class.java)
            R.id.button_play_bluetooth -> {
                if (BluetoothAdapter.getDefaultAdapter() != null) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                } else {
                    showMessage("Bluetooth is absent")
                }
            }
            R.id.button_play_network ->
                nextIntent = null
            R.id.button_exit -> {
                finishAffinity()
                exitProcess(0)
            }
        }
        nextIntent?.let {
            startActivity(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(resultCode) {
            0 -> {
                showMessage("Bluetooth wasn't enabled")
            }
            -1 -> {
                val nextIntent = Intent(this, PrepareView::class.java)
                nextIntent.putExtra("type", TypeGame.BLUETOOTH_GAME)
                startActivity(nextIntent)
            }
        }
    }

    private val REQUEST_ENABLE_BT = 1
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        initUI()
    }


    /*
        init ui this activity
    */
    private fun initUI() {
        binding.buttonSingle.setOnClickListener(this)
        binding.buttonTwoPlayers.setOnClickListener(this)
        binding.buttonPlayBluetooth.setOnClickListener(this)
        binding.buttonPlayNetwork.setOnClickListener(this)
        binding.buttonExit.setOnClickListener(this)

        binding.imageView.setOnClickListener(this)
    }
}
