package com.skalii.sample.simpleaudioplayer.functonality


import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.widget.SeekBar

import com.skalii.sample.simpleaudioplayer.R
import com.skalii.sample.simpleaudioplayer.ui.component.PlayerComponent

import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick


class AudioPlayer(
        private val context: Context,
        val components: PlayerComponent,
        var currentSongUri: Uri,
        var songCursor: Cursor
) {

    lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler: Handler

    init {
        components.playButton.onClick { playbackControl() }
    }

    fun create(currentSongUri: Uri = this.currentSongUri) {
        mediaPlayer = MediaPlayer.create(context, currentSongUri)
        setBar()
        setTexts()
    }

    fun nextSong(currentSongUri: Uri) {
        mediaPlayer.stop()
        create(currentSongUri)
        playbackControl()
        mediaPlayer.start()
    }

    private fun playbackControl() =
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                components.playButton.imageResource = R.drawable.ic_pause_circle_filled_black_24dp
            } else {
                mediaPlayer.pause()
                components.playButton.imageResource = R.drawable.ic_play_circle_filled_black_24dp
            }

    private fun setBar() =
            with(mediaPlayer) {
                seekTo(0)
                setVolume(1F, 1F)
                with(components.progressBar) {
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
        components.titleSong.text =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
        components.artistSong.text =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                val currentTime = msg.what
                components.progressBar.progress = currentTime
                components.totalTime.text = timeToString(mediaPlayer.duration)
                components.elapsedTime.text = timeToString(currentTime)
                components.reminderTime.text = timeToString(mediaPlayer.duration - currentTime)
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

    companion object {

        fun timeToString(time: Int): String {
            val s = time / 1000 % 60
            return if (s < 10) "${time / 1000 / 60}:0$s" else "${time / 1000 / 60}:$s"
        }

    }

}