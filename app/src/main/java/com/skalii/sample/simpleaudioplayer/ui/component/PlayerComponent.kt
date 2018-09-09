package com.skalii.sample.simpleaudioplayer.ui.component


import android.view.Gravity
import android.view.View
import android.widget.*

import com.skalii.sample.simpleaudioplayer.R
import com.skalii.sample.simpleaudioplayer.ui.activity.PlayerActivity

import org.jetbrains.anko.*


class PlayerComponent : AnkoComponent<PlayerActivity> {

    lateinit var titleSong: TextView
    lateinit var artistSong: TextView
    lateinit var cover: ImageView
    lateinit var progressBar: SeekBar
    lateinit var totalTime: TextView
    lateinit var elapsedTime: TextView
    lateinit var reminderTime: TextView
    lateinit var playButton: ImageButton
    lateinit var songsList: ListView

    override fun createView(ui: AnkoContext<PlayerActivity>): View =

            with(ui) {
                linearLayout {

                    lparams {
                        width = matchParent
                        height = matchParent
                    }

                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER_HORIZONTAL

                    titleSong =
                            textView {
                                id = R.id.titleSong
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                topMargin = dip(10)
                            }

                    artistSong =
                            textView {
                                id = R.id.artistSong
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                topMargin = dip(5)
                            }

                    cover =
                            imageView {
                                id = R.id.cover
                                imageResource = R.drawable.empty_cover
                            }.lparams {
                                width = dip(350)
                                height = dip(350)
                                topMargin = dip(5)
                            }

                    progressBar =
                            seekBar {
                                id = R.id.progressBar
                            }.lparams {
                                width = dip(382)
                                height = wrapContent
                                topMargin = dip(-10)
                            }

                    elapsedTime =
                            textView {
                                id = R.id.elapsedTime
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                topMargin = dip(-5)
                                leftMargin = dip(-150)
                            }

                    totalTime =
                            textView {
                                id = R.id.totalTime
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                topMargin = dip(-20)
                            }

                    reminderTime =
                            textView {
                                id = R.id.remainigTime
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                topMargin = dip(-20)
                                rightMargin = dip(-150)
                            }

                    songsList =
                            listView {
                                id = R.id.songsList
                            }.lparams {
                                width = matchParent
                                height = wrapContent
                                topMargin = dip(5)
                            }

                    playButton =
                            imageButton {
                                id = R.id.playButton
                                imageResource = R.drawable.ic_play_circle_filled_black_24dp
                                background = null

                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                topMargin = dip(-400)
                            }

                }
            }

}