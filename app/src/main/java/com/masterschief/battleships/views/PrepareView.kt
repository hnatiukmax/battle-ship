package com.masterschief.battleships.views

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.masterschief.battleships.R
import com.masterschief.battleships.activities.FullScreenActivity
import com.masterschief.battleships.databinding.ActivityMainBinding
import com.masterschief.battleships.databinding.ActivityPrepareViewBinding

class PrepareView : FullScreenActivity() {

    private lateinit var binding : ActivityPrepareViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prepare_view)
    }
}
