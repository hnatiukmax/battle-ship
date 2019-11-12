package com.masterschief.battleships.presentation.extensions

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.playSound(resSound : Int) {
    MediaPlayer.create(this, resSound).start()
}