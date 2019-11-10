package com.masterschief.battleships.presentation.pages.base

interface BasePresenter {

    fun attachView(view : BaseView)

    fun detachView()
}