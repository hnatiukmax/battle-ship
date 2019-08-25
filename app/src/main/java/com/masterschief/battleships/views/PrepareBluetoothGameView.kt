package com.masterschief.battleships.views

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.masterschief.battleships.R
import com.masterschief.battleships.activities.FullScreenActivity
import com.masterschief.battleships.contracts.PrepareBluetoothGameContract
import com.masterschief.battleships.databinding.ActivityPrepareBluetoothGameBinding
import com.masterschief.battleships.presenters.PrepareBluetoothGamePresenter

class PrepareBluetoothGameView : FullScreenActivity(),
    PrepareBluetoothGameContract.View, View.OnClickListener {

    /*
        On Click Method
     */
    override fun onClick(v: View?) {
        v?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))

        when (v?.id) {
            R.id.button_createGame -> {
                presenter?.onCreateGame()
            }
            R.id.button_joinGame -> {
                presenter?.onSearchOpponents()
            }
            R.id.button_updateSearch -> {
                if (adapter!!.count > 0) {
                    presenter?.onSearchOpponents()
                }
            }
        }
    }

    private val listViewListener = AdapterView.OnItemClickListener { _, _, _, position ->
        presenter?.onListViewClick(position.toInt())
    }

    private lateinit var binding: ActivityPrepareBluetoothGameBinding
    private var presenter : PrepareBluetoothGameContract.Presenter? = null
    private var adapter : ArrayAdapter<String>? = null
    private lateinit var handler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prepare_bluetooth_game)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        handler = Handler()

        attachPresenter()
        initUI()
    }

    private fun initUI() {
        binding.listViewDevices.adapter = adapter
        binding.listViewDevices.onItemClickListener = listViewListener

        binding.buttonUpdateSearch?.setOnClickListener(this)
        binding.buttonCreateGame.setOnClickListener(this)
        binding.buttonJoinGame.setOnClickListener(this)
    }

    /*
        Life cycle method's
     */

    private fun attachPresenter() {
        presenter = lastCustomNonConfigurationInstance as PrepareBluetoothGameContract.Presenter?
        if (presenter == null) {
            presenter = PrepareBluetoothGamePresenter()
        }
        presenter?.attachView(this)
    }

    override fun onDestroy() {
        presenter?.onDestroy()
        presenter?.detachView()
        super.onDestroy()
    }

    override fun onRetainCustomNonConfigurationInstance(): PrepareBluetoothGameContract.Presenter? {
        return presenter
    }

    /*
        Contract's methods
     */

    override fun updateDevicesList() {
        adapter?.notifyDataSetChanged()
    }

    override fun setDevicesList(array: ArrayList<String>) {
        adapter = ArrayAdapter(this, R.layout.item_bluetooth_search, array)
    }

    override fun getContext(): Activity {
        return this
    }

    override fun searchingProccesShow() {
        handler.postDelayed( {
            binding.textViewOpponents.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))
            this.searchingProccesShow()
        }, 1000)
    }

    override fun acceptProccesShow() {
        handler.postDelayed( {
            binding.textViewStatus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))
            this.acceptProccesShow()

        }, 1000)
    }

    override fun handlerInvisible() {
        handler.removeCallbacksAndMessages(null)
    }

    override fun turnOnOffSearchView(toggle: Boolean) {
        if (!toggle) handler.removeCallbacksAndMessages(null)

        binding.buttonUpdateSearch?.visibility = if (toggle) View.VISIBLE else View.INVISIBLE
        binding.listViewDevices.visibility = if (toggle) View.VISIBLE else View.INVISIBLE
        binding.textViewOpponents.visibility = if (toggle) View.VISIBLE else View.INVISIBLE
    }

    override fun statusHide(toggle : Boolean) {
        binding.textViewStatus.visibility = if (toggle) View.VISIBLE else View.INVISIBLE
    }

    override fun statusSet(message: String) {
        binding.textViewStatus.text = message
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == -1) {
            showMessage("Bluetooth was enabled")
        }
        if (resultCode == 0) {

        }

        showMessage(resultCode.toString())
    }
}
