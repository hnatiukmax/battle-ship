package com.masterschief.battleships.presentation.pages.game

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.masterschief.battleships.R
import com.masterschief.battleships.presentation.pages.base.FullScreenActivity
import com.masterschief.battleships.databinding.ActivityGameBinding
import com.masterschief.battleships.domain.entity.Point
import com.masterschief.battleships.domain.entity.enums.GameType
import com.masterschief.battleships.presentation.extensions.playSound
import com.masterschief.battleships.presentation.gameui.GameBattleDesk
import com.masterschief.battleships.presentation.pages.main.MainActivity
import kotlinx.android.synthetic.main.layout_game.*

class GameActivity : FullScreenActivity(), GameContract.View {

    override val context = this
    override val ownBattleDesk: Array<IntArray>
        get() = intent.getSerializableExtra(ARG_DESK_GAME) as Array<IntArray>

    private lateinit var binding: ActivityGameBinding
    private var presenter: GameContract.Presenter? = null
    private lateinit var gameType: GameType

    companion object {
        private val ARG_TYPE_GAME = "ARG_TYPE_GAME"
        private val ARG_DESK_GAME = "ARG_DESK_GAME"

        fun getIntent(context: Context, typeGame: GameType, desk: Array<IntArray>) =
            Intent(context, GameActivity::class.java).apply {
                putExtra(ARG_TYPE_GAME, typeGame)
                putExtra(ARG_DESK_GAME, desk)
            }
    }

    private fun initUI() {
        binding.apply {
            ownDesk.apply {
                initDesk(ownBattleDesk)
                updateDesk()
                isOwn = true
            }

            enemiesDesk.apply {
                setOnAttackListener(deskListener)
                isOwn = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)

        gameType = intent.getSerializableExtra(ARG_TYPE_GAME) as GameType

        attachPresenter()
        initUI()
    }

    private fun attachPresenter() {
        presenter = lastCustomNonConfigurationInstance as GameContract.Presenter?
        if (presenter == null) {
            presenter = GamePresenter(gameType)
        }
        presenter?.attachView(this)
    }

    override fun onDestroy() {
        presenter?.detachView()
        super.onDestroy()
    }

    override fun onRetainCustomNonConfigurationInstance(): GameContract.Presenter? {
        return presenter
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val deskListener = object : GameBattleDesk.OnAttackListener {
        override fun onAttack(point: Point) {
            presenter?.onAttack(point)
        }
    }

    override fun setEnemiesDesk(enemiesArrayDesk: Array<IntArray>) {
        binding.apply {
            enemiesDesk.initDesk(enemiesArrayDesk)
            enemiesDesk.updateDesk()
        }
    }

    override fun updateOwnGameDesk() {
        playSound(R.raw.defend)
        binding.apply {
            ownDesk.updateDesk()
        }
    }

    override fun updateEnemiesDesk() {
        playSound(R.raw.shoot)
        binding.apply {
            enemiesDesk.updateDesk()
        }
    }

    override fun onFinish(isWin : Boolean) {
        AlertDialog.Builder(this)
            .setTitle("Game is over. Yor are ${if(isWin) "winner" else "loser"}")
            .setPositiveButton("Ok") { _, _ ->
                startActivity(Intent(context, MainActivity::class.java))
                finish()
            }.show()
    }
}
