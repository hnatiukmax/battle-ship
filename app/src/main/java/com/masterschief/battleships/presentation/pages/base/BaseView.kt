package com.masterschief.battleships.presentation.pages.base

import android.content.Context

interface BaseView {

    val context : Context

    fun showMessage(message : String)
}