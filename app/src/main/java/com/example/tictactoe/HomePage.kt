package com.example.tictactoe

import android.content.Intent
import android.graphics.Matrix
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Surface
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tictactoe.databinding.ActivityHomePageBinding

class HomePage : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupVideoBackground()


        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnHowToPlay.setOnClickListener {
            startActivity(Intent(this, HowToPlay::class.java))
        }
        binding.moreGame.setOnClickListener {
            startActivity(Intent(this, CardBackGame::class.java))
        }
        binding.moreGame.setOnClickListener {
            startActivity(Intent(this, CardBackGame::class.java))
        }
        binding.btnExit.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes") { _, _ -> finishAffinity() }
                .setNegativeButton("No", null)
                .show()
        }
    }
    private fun setupVideoBackground() {
        binding.textureView.surfaceTextureListener = object : android.view.TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surfaceTexture: android.graphics.SurfaceTexture, width: Int, height: Int) {
                val surface = Surface(surfaceTexture)
                mediaPlayer = MediaPlayer.create(this@HomePage, R.raw.background) // Make sure you have background.mp4 in res/raw
                mediaPlayer.setSurface(surface)
                mediaPlayer.isLooping = true
                mediaPlayer.start()

                // Scale video like centerCrop
                val videoWidth = mediaPlayer.videoWidth.toFloat()
                val videoHeight = mediaPlayer.videoHeight.toFloat()
                val scaleX = width / videoWidth
                val scaleY = height / videoHeight
                val scale = maxOf(scaleX, scaleY)
                val matrix = Matrix()
                matrix.setScale(scale, scale, width / 2f, height / 2f)
                binding.textureView.setTransform(matrix)
            }

            override fun onSurfaceTextureSizeChanged(surface: android.graphics.SurfaceTexture, width: Int, height: Int) {}
            override fun onSurfaceTextureDestroyed(surface: android.graphics.SurfaceTexture): Boolean {
                mediaPlayer.release()
                return true
            }
            override fun onSurfaceTextureUpdated(surface: android.graphics.SurfaceTexture) {}
        }
    }
}