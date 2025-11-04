package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    enum class Turn {
        NOUGHT,
        CROSS
    }

    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.NOUGHT

    private var crossScore = 0
    private var noughtScore = 0
    private lateinit var binding: ActivityMainBinding
    private val boardList = mutableListOf<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBoard()
        setTurnLabel()
        updateScore()

       // Restart korar jonno
        binding.restartButton.setOnClickListener {
            resetScores()
        }
        val prefs = getSharedPreferences("tictactoe", MODE_PRIVATE)
        prefs.edit().putInt("xScore", crossScore).apply()
        binding.homeMain.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
        }
    }

    private fun initBoard() {
        boardList.addAll(
            listOf(
                binding.a1, binding.a2, binding.a3,
                binding.b1, binding.b2, binding.b3,
                binding.c1, binding.c2, binding.c3
            )
        )
    }

    fun boardTapped(view: View) {
        if (view !is Button) return
        addToBoard(view)

        if (checkForVictory(NOUGHT)) {
            noughtScore++
            updateScore()
            result("Player O Wins!")
            return
        }

        if (checkForVictory(CROSS)) {
            crossScore++
            updateScore()
            result("Player X Wins!")
            return
        }

        if (fullBoard()) {
            result("Draw!")
        }
    }

    private fun checkForVictory(symbol: String): Boolean {
        // Horizontal
        if (match(binding.a1, symbol) && match(binding.a2, symbol) && match(binding.a3, symbol)) return true
        if (match(binding.b1, symbol) && match(binding.b2, symbol) && match(binding.b3, symbol)) return true
        if (match(binding.c1, symbol) && match(binding.c2, symbol) && match(binding.c3, symbol)) return true

        // Vertical
        if (match(binding.a1, symbol) && match(binding.b1, symbol) && match(binding.c1, symbol)) return true
        if (match(binding.a2, symbol) && match(binding.b2, symbol) && match(binding.c2, symbol)) return true
        if (match(binding.a3, symbol) && match(binding.b3, symbol) && match(binding.c3, symbol)) return true

        // Diagonal
        if (match(binding.a1, symbol) && match(binding.b2, symbol) && match(binding.c3, symbol)) return true
        if (match(binding.a3, symbol) && match(binding.b2, symbol) && match(binding.c1, symbol)) return true

        return false
    }

    private fun match(button: Button, symbol: String) = button.text == symbol

    private fun result(title: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage("X: $crossScore  |  O: $noughtScore")
            .setPositiveButton("Play Again") { _, _ -> resetBoard() }
            .setCancelable(false)
            .show()
    }

    private fun addToBoard(button: Button) {
        if (button.text.isNotEmpty()) return

        button.text = if (currentTurn == Turn.NOUGHT) {
            currentTurn = Turn.CROSS
            NOUGHT
        } else {
            currentTurn = Turn.NOUGHT
            CROSS
        }

        setTurnLabel()
    }

    private fun fullBoard(): Boolean = boardList.all { it.text.isNotEmpty() }

    private fun resetBoard() {
        for (button in boardList) button.text = ""
        firstTurn = if (firstTurn == Turn.NOUGHT) Turn.CROSS else Turn.NOUGHT
        currentTurn = firstTurn
        setTurnLabel()
    }

    private fun resetScores() {
        crossScore = 0
        noughtScore = 0
        resetBoard()
        updateScore()
    }

    private fun updateScore() {
        binding.playerXScoreTv.text = crossScore.toString()
        binding.playerOScoreTv.text = noughtScore.toString()
    }

    private fun setTurnLabel() {
        val turnText = when (currentTurn) {
            Turn.CROSS -> "Turn: X"
            Turn.NOUGHT -> "Turn: O"
        }
        binding.turnTv.text = turnText
    }

    companion object {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }
}
