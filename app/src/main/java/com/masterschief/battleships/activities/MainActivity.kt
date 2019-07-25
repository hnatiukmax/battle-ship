package com.masterschief.battleships.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.masterschief.battleships.R
import com.masterschief.battleships.databinding.ActivityMainBinding
import com.masterschief.battleships.views.PrepareView
import kotlin.system.exitProcess

class MainActivity : FullScreenActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.executePendingBindings()
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


    /*
        onClick methods
        button's function
    */
    override fun onClick(v: View?) {
        v?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))

        var intent : Intent? = null
        when(v?.id) {
            R.id.button_single ->
                intent = Intent(this, PrepareView::class.java)
            R.id.button_two_players ->
                intent = null
            R.id.button_play_bluetooth ->
                intent = null
            R.id.button_play_network ->
                intent = null
            R.id.button_exit -> {
                finishAffinity()
                exitProcess(0)
            }
        }
        intent?.let {
            startActivity(it)
        }
    }
}
