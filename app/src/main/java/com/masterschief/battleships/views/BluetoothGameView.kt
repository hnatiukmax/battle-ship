package com.masterschief.battleships.views

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatDelegate
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.masterschief.battleships.R
import com.masterschief.battleships.activities.FullScreenActivity
import com.masterschief.battleships.activities.MainActivity
import com.masterschief.battleships.bluetooth.MessageModel
import com.masterschief.battleships.bluetooth.MessageType
import com.masterschief.battleships.contracts.BluetoothGameContract
import com.masterschief.battleships.contracts.PrepareGameContract
import com.masterschief.battleships.databinding.ActivityBluetoothGameViewBinding
import com.masterschief.battleships.databinding.ActivitySingleGameViewBinding
import com.masterschief.battleships.presenters.BluetoothGamePresenter
import com.masterschief.battleships.presenters.PreparePresenter
import com.masterschief.battleships.uigame.GameBattleDesk
import com.masterschief.battleships.uigame.GameDeskContract
import com.masterschief.battleships.utils.TypeGame
import com.masterschief.battleships.uigame.GameDeskContract.BattleDesk as BattleDesk1

class BluetoothGameView : FullScreenActivity(), View.OnClickListener, BluetoothGameContract.View {

    /*
        OnCLick Method
     */

    @ExperimentalStdlibApi
    override fun onClick(v: View?) {
        v?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))

        var nextIntent : Intent? = null
        when (v?.id) {
            R.id.battleDesk_first -> {
                presenter?.onWrite(MessageModel("battleDesk_first", MessageType.CHAT))
            }
            R.id.battleDesk_second -> {
                presenter?.onWrite(MessageModel("battleDesk_second", MessageType.CHAT))
            }
        }
        nextIntent?.let {
            startActivity(it)
        }
    }

    /*
       Life cycle
   */

    private fun attachPresenter() {
        presenter = lastCustomNonConfigurationInstance as BluetoothGameContract.Presenter?
        if (presenter == null) {
            presenter = BluetoothGamePresenter()
        }
        presenter?.attachView(this)
    }

    override fun onDestroy() {
        presenter?.detachView()
        super.onDestroy()
    }

    override fun onRetainCustomNonConfigurationInstance(): BluetoothGameContract.Presenter? {
        return presenter
    }


    private var presenter : BluetoothGameContract.Presenter? = null
    private lateinit var sheet : LinearLayout
    private lateinit var beh : BottomSheetBehavior<View>
    private lateinit var binding: ActivityBluetoothGameViewBinding
    private lateinit var desk1 : GameBattleDesk
    private lateinit var btn_chat : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth_game_view)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        attachPresenter()
        initUI()
    }

    private fun initUI() {
        sheet = findViewById(R.id.bottom_sheet)
        beh = BottomSheetBehavior.from(sheet)
        beh.state = BottomSheetBehavior.STATE_COLLAPSED

        btn_chat = findViewById(R.id.btn_chat)
        btn_chat.setOnClickListener(sheet_listener)

        desk1 = findViewById(R.id.battleDesk_first)
        desk1.setOnClickListener(this)


        Toast.makeText(this, "${desk1.width} ${desk1.height}", Toast.LENGTH_LONG).show()
    }

    private val sheet_listener = View.OnClickListener {
        it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))

        if (beh.state != BottomSheetBehavior.STATE_EXPANDED) {
            beh.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            beh.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    val listener = View.OnClickListener {
        beh.state = BottomSheetBehavior.STATE_COLLAPSED
    }



}
