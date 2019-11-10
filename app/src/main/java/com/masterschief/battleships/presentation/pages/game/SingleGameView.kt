package com.masterschief.battleships.presentation.pages.game

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.masterschief.battleships.R
import com.masterschief.battleships.presentation.pages.base.FullScreenActivity
import com.masterschief.battleships.databinding.ActivitySingleGameViewBinding
import kotlinx.android.synthetic.main.layout_game.view.*

class SingleGameView : FullScreenActivity() {

    private lateinit var binding: ActivitySingleGameViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_game_view)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        initUI()
    }

    private fun initUI() {
        binding.root.chat.visibility = View.INVISIBLE

    }

}
