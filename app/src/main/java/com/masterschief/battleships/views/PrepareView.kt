package com.masterschief.battleships.views


import android.content.Intent
import android.databinding.DataBindingUtil

import android.os.Bundle

import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.masterschief.battleships.contracts.PrepareGameContract
import com.masterschief.battleships.R
import com.masterschief.battleships.activities.FullScreenActivity
import com.masterschief.battleships.activities.MainActivity
import com.masterschief.battleships.databinding.ActivityPrepareViewBinding
import com.masterschief.battleships.uigame.GameDeskContract
import com.masterschief.battleships.utils.Point
import com.masterschief.battleships.presenters.PreparePresenter
import com.masterschief.battleships.utils.TypeGame

class PrepareView : FullScreenActivity(), View.OnClickListener,
    PrepareGameContract.View {
    override fun onClick(v: View?) {
        v?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink))

        var nextIntent : Intent? = null
        when (v?.id) {
            R.id.button_auto -> {
                presenter?.onRandomlyButton()
            }
            R.id.button_next -> {
                when (typeGame) {
                    TypeGame.BLUETOOTH_GAME -> {
                        nextIntent = Intent(this, PrepareBluetoothGameView::class.java)
                        presenter?.let {
                            nextIntent!!.putExtra("data", it.getData())
                        }
                    }
                }
            }
            R.id.button_back -> {
                nextIntent = Intent(this, MainActivity::class.java)
            }
            R.id.textView_rotate ->
                presenter?.onRotate()
        }
        nextIntent?.let {
            startActivity(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(this, resultCode, Toast.LENGTH_LONG).show()
    }

    private lateinit var binding: ActivityPrepareViewBinding
    private var presenter : PrepareGameContract.Presenter? = null
    private lateinit var typeGame : TypeGame

    //UI
    private lateinit var prepareViewDesk: GameDeskContract.PrepareDesk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prepare_view)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        typeGame  = intent.getSerializableExtra("type") as TypeGame

        initUI()
        attachPresenter()
    }

    private fun initUI() {
        prepareViewDesk = binding.gameDesk
        (prepareViewDesk as View).setOnTouchListener(listener)

        binding.buttonAuto.setOnClickListener(this)
        binding.buttonNext.setOnClickListener(this)
        binding.buttonBack.setOnClickListener(this)
        binding.textViewRotate.setOnClickListener(this)
    }

    /*
        Life cycle
    */

    private fun attachPresenter() {
        presenter = lastCustomNonConfigurationInstance as PrepareGameContract.Presenter?
        if (presenter == null) {
            presenter = PreparePresenter()
        }
        presenter?.attachView(this)
    }

    override fun onDestroy() {
        presenter?.detachView()
        super.onDestroy()
    }

    override fun onRetainCustomNonConfigurationInstance(): PrepareGameContract.Presenter? {
        return presenter
    }

    /*
        Contract interface methods
    */

    override fun updateDesk() {
        prepareViewDesk.updateDesk()
    }

    override fun setDesk(desk: Array<IntArray>) {
        prepareViewDesk.initDesk(desk)
    }

    private val listener = View.OnTouchListener { v, event ->
        val x = (event.x / (v.width / 10)).toInt()
        val y = (event.y / (v.width / 10)).toInt()

        if ((x >= 10 || x < 0) || (y >= 10 || y < 0)) {
            return@OnTouchListener false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN // нажатие
            ->
                presenter?.onDown(Point(y,x))
            MotionEvent.ACTION_MOVE // движение
            ->
                presenter?.onMove(Point(y,x))

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> { // отпускание
                presenter?.onUp()
            }
        }

        return@OnTouchListener true
    }
}
