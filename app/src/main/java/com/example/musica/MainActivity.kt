package com.example.musica

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // MediaPlayer y variables para manejar pistas
    private var mediaPlayer: MediaPlayer? = null
    private var currentTrackIndex = 0
    private var isPaused = false // Estado de pausa

    private val tracks = listOf("musica.mp3", "musica2.mp3", "musica3.mp3")
    private val coverImages = listOf(R.drawable.portada, R.drawable.portada2, R.drawable.portada3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de vistas
        val buttonPlay: Button = findViewById(R.id.button_play)
        val buttonStop: Button = findViewById(R.id.button_stop)
        val buttonPreview: Button = findViewById(R.id.button_preview)
        val buttonNext: Button = findViewById(R.id.button_next)
        val imageViewCover: ImageView = findViewById(R.id.imageViewCover)

        prepareTrack()

        buttonPlay.setOnClickListener {
            if (isPaused) {
                mediaPlayer?.start()
                isPaused = false
            } else {
                playTrack()
            }
        }

        buttonStop.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                isPaused = true
            }
        }

        buttonPreview.setOnClickListener {
            playPreview()
        }

        buttonNext.setOnClickListener {
            nextTrack()
            imageViewCover.setImageResource(coverImages[currentTrackIndex])
        }
    }

    private fun prepareTrack() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            val assetFileDescriptor = assets.openFd(tracks[currentTrackIndex])
            setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
            assetFileDescriptor.close()
            prepare()
        }
    }

    private fun playTrack() {
        prepareTrack()
        mediaPlayer?.start()
        val imageViewCover: ImageView = findViewById(R.id.imageViewCover)
        imageViewCover.setImageResource(coverImages[currentTrackIndex])
        isPaused = false
    }

    private fun playPreview() {
        playTrack()
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.seekTo(0)
            mediaPlayer?.pause()
        }
        mediaPlayer?.seekTo(0)
        mediaPlayer?.start()
    }

    private fun nextTrack() {
        currentTrackIndex = (currentTrackIndex + 1) % tracks.size
        Toast.makeText(this, "Cambiando a la pista ${currentTrackIndex + 1}", Toast.LENGTH_SHORT).show()
        playTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
