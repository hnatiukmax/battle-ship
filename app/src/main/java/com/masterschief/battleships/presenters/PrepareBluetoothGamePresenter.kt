package com.masterschief.battleships.presenters

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import com.masterschief.battleships.app.MyApplication
import com.masterschief.battleships.bluetooth.connectToOpponent
import com.masterschief.battleships.bluetooth.waitToConnect
import com.masterschief.battleships.contracts.PrepareBluetoothGameContract
import com.masterschief.battleships.views.BluetoothGameView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PrepareBluetoothGamePresenter : PrepareBluetoothGameContract.Presenter {

    private val REQUEST_ENABLE_BT = 1
    private var view: PrepareBluetoothGameContract.View? = null
    private var devicesNameList = ArrayList<String>()
    private var discoveredDevicesList = mutableSetOf<BluetoothDevice>()
    private var bAdapter = BluetoothAdapter.getDefaultAdapter()

    //test
    private var disposable = CompositeDisposable()

    /*
        Override methods
     */

    override fun attachView(view: PrepareBluetoothGameContract.View) {
        this.view = view

        view.apply {
            turnOnOffSearchView(false)
            getContext().registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
            setDevicesList(devicesNameList)
        }
    }

    override fun detachView() {
        this.view = null
    }

    override fun onSearchOpponents() {
        when {
            !bAdapter.isEnabled -> {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                view?.getContext()?.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
            bAdapter.isDiscovering -> {
                view?.showMessage("Discovering is already working")
                return
            }
            bAdapter.isEnabled -> {
                devicesNameList.clear()
                discoveredDevicesList.clear()

                view?.apply {
                    searchingProccesShow()
                    turnOnOffSearchView(true)
                    statusHide(false)
                }

                bAdapter.startDiscovery()

                Handler().postDelayed({
                    bAdapter.cancelDiscovery()
                    view?.handlerInvisible()
                }, 15000)
            }
        }
    }

    override fun onCreateGame() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 20)
        view?.getContext()?.startActivity(discoverableIntent)

        view?.apply {
            turnOnOffSearchView(false)
            statusSet("Waiting for the opponents")
            acceptProccesShow()
        }

        disposable.add(waitToConnect()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callBackConnect,
                {
                    view?.showMessage("Error Server")
                }
            )
        )

    }

    override fun onListViewClick(position: Int) {
        disposable.add(connectToOpponent(discoveredDevicesList.elementAt(position))
            .subscribeOn(Schedulers.io())
            .subscribe(callBackConnect,
                {
                    view?.showMessage("Error Connection")
                }
            )
        )
    }

    override fun onDestroy() {
        bAdapter.cancelDiscovery()
    }

    /*
        Private's methods
     */

    //receiver for bluettoth search
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    discoveredDevicesList.add(device)
                    device.name.apply {
                        if (!devicesNameList.contains(this)) {
                            devicesNameList.add(device.name)
                        }
                    }
                    view?.updateDevicesList()
                }
            }
        }
    }

    private val callBackConnect:  (BluetoothSocket) -> Unit = { socket ->
        MyApplication.instance.currentSocket = socket

        val nextIntent = Intent(view?.getContext(), BluetoothGameView::class.java)
        view?.getContext()?.startActivity(nextIntent)

        view?.apply {
            handlerInvisible()
            turnOnOffSearchView(false)

            statusHide(true)
            statusSet("Connected")
        }
    }
}