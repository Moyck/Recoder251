package com.moyck.recoder251.utils

import android.media.MediaPlayer
import android.util.Log
import java.io.File
import android.R.attr.start
import android.R.attr.path
import java.io.FileInputStream
import java.lang.Exception


/**
 * @package:com.moyck.recoder251.utils
 * @date on 2019/12/5 14:36
 * @author  Moyuk
 */
object MediaPlayerUtil {

    var player = MediaPlayer()
    lateinit var file:File

    fun startPlay(file: File) {
        try {
            this.file = file
            val fis = FileInputStream(file)
            player.reset()
            player.setDataSource(fis.fd)
            player.prepare()
            player.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun pause() {
        player.pause()
    }

    fun stop() {
        player.stop()
    }

    fun seekTo(progress: Float) {
        if (!player.isPlaying){
            startPlay(file)
        }
        player.seekTo((player.duration * progress).toInt())
    }

}