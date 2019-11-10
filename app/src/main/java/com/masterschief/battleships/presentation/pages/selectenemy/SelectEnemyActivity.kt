package com.masterschief.battleships.presentation.pages.selectenemy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.masterschief.battleships.R
import com.masterschief.battleships.presentation.pages.base.FullScreenActivity
import com.masterschief.battleships.databinding.ActivityPrepareBluetoothGameBinding
import com.masterschief.battleships.domain.entity.enums.GameType
import com.masterschief.battleships.presentation.extensions.blink
import com.masterschief.battleships.presentation.pages.game.GameActivity

class SelectEnemyActivity : FullScreenActivity(), SelectEnemyContract.View {

    private lateinit var binding: ActivityPrepareBluetoothGameBinding
    private var presenter : SelectEnemyContract.Presenter? = null
    private val devicesList = arrayListOf<String>()
    private var arrayAdapter : ArrayAdapter<String>? = null
    private val handler = Handler()

    override fun getActivity() = this

    companion object {
        private val ARG_TYPE_GAME = "ARG_TYPE_GAME"
        private val ARG_DESK_GAME = "ARG_DESK_GAME"

        fun getIntent(context: Context, typeGame: GameType, desk : Array<IntArray>) =
            Intent(context, SelectEnemyActivity::class.java).apply {
                putExtra(ARG_TYPE_GAME, typeGame)
                putExtra(ARG_DESK_GAME, desk)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prepare_bluetooth_game)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        attachPresenter()
        initUI()
    }

    private fun attachPresenter() {
        presenter = lastCustomNonConfigurationInstance as SelectEnemyContract.Presenter?
        if (presenter == null) {
            presenter = SelectEnemyBluetoothPresenter()
        }
        presenter?.attachView(this)
    }

    override fun onStop() {
        presenter?.onStop()
        presenter?.detachView()
        super.onStop()
    }

    override fun onRetainCustomNonConfigurationInstance(): SelectEnemyContract.Presenter? {
        return presenter
    }

    private fun initUI() {
        binding.apply {
            arrayAdapter = ArrayAdapter(this@SelectEnemyActivity, R.layout.item_bluetooth_search, devicesList)
            devices.apply {
                this.adapter = arrayAdapter
                onItemClickListener = listViewListener
            }

            buttonUpdateSearch?.setOnClickListener {
                it.blink()
                arrayAdapter?.let { array ->
                    if (array.count > 0) {
                        presenter?.onSearchOpponents()
                    }
                }
            }

            buttonCreateGame.setOnClickListener {
                presenter?.onCreateGame()
            }

            buttonJoinGame.setOnClickListener {
                presenter?.onSearchOpponents()
            }

            buttonBack?.setOnClickListener {
                onBackPressed()
            }

        }
    }

    /*
        Contract's methods
     */

    override val context = this

    override fun updateDevicesList(device : String) {
        devicesList.add(device)
        arrayAdapter?.notifyDataSetChanged()
    }

    override fun searchingProcessShow() {
        handler.postDelayed( {
            binding.textViewOpponents.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))
            this.searchingProcessShow()
        }, 1000)
    }

    override fun acceptProcessShow() {
        handler.postDelayed( {
            binding.faq.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))
            this.acceptProcessShow()
        }, 1000)
    }

    override fun handlerInvisible() {
        handler.removeCallbacksAndMessages(null)
    }
//
    override fun turnOnOffSearchView(toggle: Boolean) {
        if (!toggle) handler.removeCallbacksAndMessages(null)

        binding.buttonUpdateSearch?.visibility = if (toggle) View.VISIBLE else View.INVISIBLE
        binding.devices.visibility = if (toggle) View.VISIBLE else View.INVISIBLE
        binding.textViewOpponents.visibility = if (toggle) View.VISIBLE else View.INVISIBLE
    }

    override fun statusHide(toggle : Boolean) {
        binding.faq.visibility = if (toggle) View.VISIBLE else View.INVISIBLE
    }

    override fun statusSet(message: String) {
        binding.faq.text = message
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                showMessage("Bluetooth was enabled")
            }
            Activity.RESULT_CANCELED -> {
                showMessage("Bluetooth was disabled")
            }
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val listViewListener = AdapterView.OnItemClickListener { _, _, _, position ->
        presenter?.onListViewClick(position.toInt())
    }

    override fun toGameActivity() {
        startActivity(GameActivity.getIntent(
            context = this,
            typeGame = intent.getSerializableExtra(ARG_TYPE_GAME) as GameType,
            desk = intent.getSerializableExtra(ARG_DESK_GAME) as Array<IntArray>
        ))
    }
}
