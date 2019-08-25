package com.masterschief.battleships.presenters

import com.masterschief.battleships.app.MyApplication
import com.masterschief.battleships.bluetooth.ConnectionManager
import com.masterschief.battleships.bluetooth.MessageModel
import com.masterschief.battleships.contracts.BluetoothGameContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BluetoothGamePresenter : BluetoothGameContract.Presenter {

    private var connectionManager : ConnectionManager? = null
    private var view : BluetoothGameContract.View? = null
    private var disposable : CompositeDisposable? = null

    /*
        Override methods
     */

    override fun attachView(view: BluetoothGameContract.View) {
        this.view = view

        MyApplication.instance.currentSocket?.let {
            connectionManager = ConnectionManager(it)
            disposable = CompositeDisposable()
            this.onListen()
        }
    }

    override fun detachView() {
        this.view = null

        disposable = null
    }

    @ExperimentalStdlibApi
    override fun onWrite(what: MessageModel) {
        disposable!!.add(connectionManager!!.write(what.message.encodeToByteArray())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    try {
                        view?.showMessage("Write Success")
                    } catch (ex : Throwable) {

                    }
                },
                {
                    try {
                        view?.showMessage("Write Error")
                    } catch (ex : Throwable) {

                    }
                }
            )
        )
    }

    private fun onListen() {
        disposable!!.add(connectionManager!!.listen()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view?.showMessage(it.message)
                },
                {
                    it.message?.let {
                        view?.showMessage(it)
                    }
                }
            )
        )
    }
}