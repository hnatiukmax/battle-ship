package com.masterschief.battleships.presentation.pages.preparegame


import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.masterschief.battleships.R
import com.masterschief.battleships.presentation.pages.base.FullScreenActivity
import com.masterschief.battleships.databinding.ActivityPrepareViewBinding
import com.masterschief.battleships.presentation.game.GameDeskContract
import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.domain.entity.enums.GameType
import com.masterschief.battleships.domain.entity.enums.GameType.*
import com.masterschief.battleships.presentation.pages.game.GameActivity
import com.masterschief.battleships.presentation.pages.selectenemy.SelectEnemyActivity

class PrepareGameActivity : FullScreenActivity(),
    PrepareGameContract.View {

    override val context = this

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private lateinit var binding: ActivityPrepareViewBinding
    private var presenter : PrepareGameContract.Presenter? = null
    private lateinit var typeGame : GameType

    companion object {
        private val ARG_TYPE_GAME = "ARG_TYPE_GAME"

        fun getIntent(context : Context, typeGame : GameType) =
            Intent(context, PrepareGameActivity::class.java).apply {
                putExtra(ARG_TYPE_GAME, typeGame)
            }
    }

    private fun initUI() {
        binding.apply {
            prepareViewDesk = gameDesk
            (prepareViewDesk as View).setOnTouchListener(listener)

            buttonAuto.setOnClickListener {
                presenter?.onRandomlyClick()
            }

            buttonNext.setOnClickListener {
                presenter?.onNextClick()
            }

            buttonBack.setOnClickListener {
                onBackPressed()
            }

            textViewRotate.setOnClickListener {
                presenter?.onRotate()
            }
        }
    }

    //UI
    private lateinit var prepareViewDesk: GameDeskContract.PrepareDesk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prepare_view)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        typeGame  = intent.getSerializableExtra(ARG_TYPE_GAME) as GameType

        initUI()
        attachPresenter()
    }

    /*
        Life cycle
    */

    private fun attachPresenter() {
        presenter = lastCustomNonConfigurationInstance as PrepareGameContract.Presenter?
        if (presenter == null) {
            presenter =
                PrepareGamePresenter()
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
            MotionEvent.ACTION_DOWN -> presenter?.onDown(Point(y, x))
            MotionEvent.ACTION_MOVE -> presenter?.onMove(Point(y, x))
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> { // отпускание
                presenter?.onUp()
            }
        }

        return@OnTouchListener true
    }

    override fun getNext(desk: Array<IntArray>) {
        if (typeGame == BLUETOOTH_GAME || typeGame == NETWORK_GAME) {
            startActivity(SelectEnemyActivity.getIntent(this, typeGame, desk))
        } else {
            startActivity(GameActivity.getIntent(this, typeGame, desk))
        }
    }
}
