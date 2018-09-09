package com.skalii.sample.simpleaudioplayer.functonality


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

import java.io.File
import com.skalii.sample.simpleaudioplayer.R


class SongsCatcher {

    private lateinit var arrayList: ArrayList<String>
    lateinit var audioPlayer: AudioPlayer
    lateinit var currentSongUri: Uri
    lateinit var songCursor: Cursor
    val PERMISSION_REQUEST = 1


    fun checkPermission(
            context: Context,
            activity: Activity,
            songsList: ListView
    ) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity, Manifest.permission.READ_EXTERNAL_STORAGE
                    )) {
                ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST
                )
            } else {
                ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST
                )
            }
        } else {
            doStuff(
                    activity.contentResolver,
                    context,
                    songsList
            )
        }
    }

    @SuppressLint("Recycle")
    fun getMusic(contentResolver: ContentResolver) {
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        songCursor = contentResolver.query(songUri, null, null, null, null)
        if (songCursor.moveToFirst()) {
            with(songCursor) {
                do {
                    arrayList.add(
                            "${getString(getColumnIndex(MediaStore.Audio.Media.TITLE))} - " +
                                    "${AudioPlayer.timeToString(getInt(getColumnIndex(MediaStore.Audio.Media.DURATION)))}\n" +
                                    "${getString(getColumnIndex(MediaStore.Audio.Media.ARTIST))} -  " +
                                    getString(getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    )
                } while (moveToNext())
                songCursor.moveToPosition(0)
                currentSongUri = Uri.fromFile(File(getString(getColumnIndex(MediaStore.Audio.Media.DATA))))
            }
        }
    }

    fun doStuff(
            contentResolver: ContentResolver,
            context: Context,
            songsList: ListView
    ) {
        arrayList = ArrayList()
        getMusic(contentResolver)
        songsList.run {
            adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    arrayList
            )
            onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                songCursor.moveToPosition(position)
                currentSongUri = Uri.fromFile(File(songCursor.getString(1)))
                audioPlayer.nextSong(currentSongUri)

                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(context, currentSongUri)
                val data = mmr.embeddedPicture

                if (data != null) {
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    audioPlayer.components.cover.setImageBitmap(bitmap)
                } else {
                    audioPlayer.components.cover.setImageResource(R.drawable.empty_cover)
                }
                audioPlayer.components.cover.adjustViewBounds = true
            }
        }

    }

}