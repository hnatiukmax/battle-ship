package com.masterschief.battleships

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button

class MainActivity : FullScreenActivity() {

    lateinit var btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.button_single)
        btn.setOnClickListener(listener)

    }

    val listener = View.OnClickListener {
        val anim = AnimationUtils.loadAnimation(this, R.anim.blink);
        it.startAnimation(anim)
    }
}
