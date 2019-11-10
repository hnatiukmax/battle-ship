package com.masterschief.battleships.presentation.extensions

import android.view.View
import android.view.animation.AnimationUtils
import com.masterschief.battleships.R

fun View.blink(resId : Int = R.anim.blink) {
    startAnimation(AnimationUtils.loadAnimation(context, resId))
}