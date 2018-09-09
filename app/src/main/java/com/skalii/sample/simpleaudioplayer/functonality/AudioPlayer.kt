package com.skalii.sample.simpleaudioplayer.functonality


import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView

import com.skalii.sample.simpleaudioplayer.R
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick


class AudioPlayer(
        private val context: Context,
        private val progressBar: SeekBar,
        private val totalTime: TextView,
        private val elapsedTime: TextView,
        private val remainderTime: TextView,
        private val playButton: ImageButton,
        var cover: ImageView,
        currentSongUri: Uri
) {

    var mediaPlayer = MediaPlayer.create(context, currentSongUri)
    private lateinit var handler: Handler


    init {
        setBar()
        setTexts()
        playButton.onClick { playbackControl() }
    }

    private fun setBar() =
            with(mediaPlayer) {
                seekTo(0)
                setVolume(1F, 1F)
                with(progressBar) {
                    max = duration
                    setOnSeekBarChangeListener(
                            object : SeekBar.OnSeekBarChangeListener {
                                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                                override fun onProgressChanged(
                                        seekBar: SeekBar,
                                        progress: Int,
                                        fromUser: Boolean
                                ) {
                                    if (fromUser) {
                                        seekTo(progress)
                                        setProgress(progress)
                                    }
                                }
                            }
                    )
                }
            }

    private fun setTexts() {
        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                val currentTime = msg.what
                progressBar.progress = currentTime
                totalTime.text = timeToString(mediaPlayer.duration)
                elapsedTime.text = timeToString(currentTime)
                remainderTime.text = timeToString(mediaPlayer.duration - currentTime)
            }
        }
        Thread(Runnable {
            while (mediaPlayer != null) {
                try {
                    val msg = Message()
                    msg.what = mediaPlayer.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()
    }

    private fun playbackControl() =
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                playButton.imageResource = R.drawable.ic_pause_circle_filled_black_24dp
            } else {
                mediaPlayer.pause()
                playButton.imageResource = R.drawable.ic_play_circle_filled_black_24dp
            }

    fun nextSong(
            context: Context,
            currentSongUri: Uri
    ) {
        mediaPlayer.stop()
        mediaPlayer = MediaPlayer.create(context, currentSongUri)
        setBar()
        setTexts()
        playbackControl()
        mediaPlayer.start()
    }

    companion object {

        fun timeToString(time: Int): String {
            val s = time / 1000 % 60
            return if (s < 10) "${time / 1000 / 60}:0$s" else "${time / 1000 / 60}:$s"
        }

    }

}