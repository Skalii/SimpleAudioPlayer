package com.skalii.sample.simpleaudioplayer.ui.activity


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

import com.skalii.sample.simpleaudioplayer.ui.component.PlayerComponent
import com.skalii.sample.simpleaudioplayer.functonality.AudioPlayer
import com.skalii.sample.simpleaudioplayer.functonality.SongsCatcher

import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast


class PlayerActivity : AppCompatActivity() {

    private val songsCatcher = SongsCatcher()
    private val components = PlayerComponent()
    private lateinit var audioPlayer: AudioPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(components) {
            setContentView(this@PlayerActivity)
            audioPlayer = AudioPlayer(
                    this@PlayerActivity,
                    progressBar,
                    totalTime,
                    elapsedTime,
                    reminderTime,
                    playButton,
                    cover,
                    songsCatcher.also {
                        it.checkPermission(
                                this@PlayerActivity,
                                this@PlayerActivity,
                                components.songsList
                        )
                    }.currentSongUri
            )
        }
        songsCatcher.audioPlayer = audioPlayer
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            songsCatcher.PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        toast("Permission Granted!")
                        songsCatcher.doStuff(
                                contentResolver,
                                this,
                                components.songsList
                        )
                    }
                } else {
                    toast("No Permission Granted!")
                    finish()
                }
                return
            }
        }
    }

}