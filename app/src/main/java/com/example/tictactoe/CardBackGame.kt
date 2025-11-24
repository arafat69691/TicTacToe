package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tictactoe.databinding.ActivityCardBackGameBinding


class CardBackGame : AppCompatActivity() {
    private lateinit var binding: ActivityCardBackGameBinding
    private lateinit var gridLayout: GridLayout
    private lateinit var scoreText: TextView

    private var score = 0
    private var selectedImages = mutableListOf<ImageView>()
    private var selectedTags = mutableListOf<Int>()


    private val images = listOf(
        R.drawable.apple,
        R.drawable.banana,
        R.drawable.orange,
        R.drawable.grape
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCardBackGameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        gridLayout = findViewById(R.id.gridLayout)
        scoreText = findViewById(R.id.scoreText)


        setupGame()

        binding.bkButton.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
        }
    }

    private fun setupGame() {
        val totalCards = images + images
        val shuffled = totalCards.shuffled()

        for (i in shuffled.indices) {
            val imageView = ImageView(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 200
                    height = 200
                    setMargins(8, 8, 8, 8)
                }
                setImageResource(R.drawable.card_back) // back image
                tag = shuffled[i]
                setOnClickListener { flipCard(this) }
            }
            gridLayout.addView(imageView)
        }
    }

    private fun flipCard(card: ImageView) {
        if (selectedImages.size == 2) return

        card.setImageResource(card.tag as Int)
        selectedImages.add(card)
        selectedTags.add(card.tag as Int)

        if (selectedImages.size == 2) {
            Handler().postDelayed({
                checkMatch()
            }, 800)
        }
    }

    private fun checkMatch() {
        if (selectedTags[0] == selectedTags[1]) {
            // Matched
            selectedImages.forEach { it.isClickable = false }
            score += 1
            scoreText.text = "Score: $score"
        } else {
            // Not matched
            selectedImages.forEach { it.setImageResource(R.drawable.card_back) }
        }
        selectedImages.clear()
        selectedTags.clear()
    }
}