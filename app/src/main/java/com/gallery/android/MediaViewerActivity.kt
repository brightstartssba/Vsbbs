package com.gallery.android

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.gallery.android.databinding.ActivityMediaViewerBinding
import com.gallery.android.model.MediaType

class MediaViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaViewerBinding
    private var exoPlayer: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()

        val mediaPath = intent.getStringExtra("media_path") ?: return
        val mediaType = MediaType.valueOf(intent.getStringExtra("media_type") ?: MediaType.IMAGE.name)

        setupUI(mediaPath, mediaType)
        
        binding.fabClose.setOnClickListener {
            finish()
        }
    }

    private fun setupUI(path: String, type: MediaType) {
        when (type) {
            MediaType.IMAGE -> {
                binding.imageView.visibility = View.VISIBLE
                binding.playerView.visibility = View.GONE
                loadImage(path)
            }
            MediaType.VIDEO -> {
                binding.imageView.visibility = View.GONE
                binding.playerView.visibility = View.VISIBLE
                setupVideoPlayer(path)
            }
            MediaType.ALL -> {
                // Determine type based on file extension
                if (isVideoFile(path)) {
                    binding.imageView.visibility = View.GONE
                    binding.playerView.visibility = View.VISIBLE
                    setupVideoPlayer(path)
                } else {
                    binding.imageView.visibility = View.VISIBLE
                    binding.playerView.visibility = View.GONE
                    loadImage(path)
                }
            }
        }
    }

    private fun loadImage(path: String) {
        Glide.with(this)
            .load(Uri.parse(path))
            .into(binding.imageView)
    }

    private fun setupVideoPlayer(path: String) {
        exoPlayer = ExoPlayer.Builder(this).build().also { player ->
            binding.playerView.player = player
            val mediaItem = MediaItem.fromUri(Uri.parse(path))
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            
            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        player.seekTo(0)
                        player.pause()
                    }
                }
            })
        }
    }

    private fun isVideoFile(path: String): Boolean {
        val videoExtensions = arrayOf(".mp4", ".avi", ".mov", ".mkv", ".webm", ".3gp")
        return videoExtensions.any { path.lowercase().endsWith(it) }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (binding.playerView.visibility == View.VISIBLE) {
            exoPlayer?.play()
        }
    }
}