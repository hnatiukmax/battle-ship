package com.masterschief.battleships.presentation.pages.main

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.masterschief.battleships.R
import com.masterschief.battleships.databinding.ActivityMainBinding
import com.masterschief.battleships.domain.entity.enums.GameType.*
import com.masterschief.battleships.presentation.pages.base.BaseView
import com.masterschief.battleships.presentation.pages.base.FullScreenActivity
import com.masterschief.battleships.presentation.pages.preparegame.PrepareGameActivity

class MainActivity : FullScreenActivity(), BaseView {

    private val REQUEST_ENABLE_BT = 1
    private lateinit var binding: ActivityMainBinding

    override val context = this

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initUI()
    }

    private fun initUI() {
        binding.apply {
            buttonSingle.setOnClickListener {
                startActivity(PrepareGameActivity.getIntent(context, SINGLE_GAME))
            }

            buttonTwoPlayers.setOnClickListener {
                startActivity(PrepareGameActivity.getIntent(context, TWO_PLAYERS_GAME))
            }

            buttonPlayBluetooth.setOnClickListener {
                if (BluetoothAdapter.getDefaultAdapter() != null) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                } else {
                    showMessage(getString(R.string.main_page_bluetooth_is_not_available))
                }
            }

            buttonPlayNetwork.setOnClickListener {
                startActivity(PrepareGameActivity.getIntent(context, NETWORK_GAME))
            }

            buttonExit.setOnClickListener {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_CANCELED -> {
                showMessage(getString(R.string.main_page_bluetooth_was_not_enabled))
            }
            Activity.RESULT_OK -> {
                startActivity(PrepareGameActivity.getIntent(context, BLUETOOTH_GAME))
            }
        }
    }
}
