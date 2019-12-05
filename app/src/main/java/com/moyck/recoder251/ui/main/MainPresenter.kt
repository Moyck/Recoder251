package com.moyck.recoder251.ui.main

import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import com.moyck.recoder251.service.RecorderService
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class MainPresenter : MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener {

    var mMediaRecorder: MediaRecorder? = null
    var dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
    lateinit var view: MainContract.View
    lateinit var model: MainContract.Model
    var isRecording = false


    fun bind(view: MainContract.View, model: MainContract.Model) {
        this.view = view
        this.model = model
    }

    fun stopRecoder() {
        if (!isRecording) {
            return
        }
        view.getContext().stopService(Intent(view.getContext(), RecorderService::class.java))
        isRecording = false
    }

    fun startRecoder() {
        if (isRecording) {
            stopRecoder()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            view.getContext()
                .startForegroundService(Intent(view.getContext(), RecorderService::class.java))
        } else {
            view.getContext().startService(Intent(view.getContext(), RecorderService::class.java))
        }
        isRecording = true
        startFlash()
    }

    fun startFlash() {
        var isFlash = true
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (isRecording) {
                    view.flash(isFlash)
                    isFlash = !isFlash
                } else {
                    view.flash(false)
                    cancel()
                }
            }
        }, 0, 1000)
    }

    override fun onError(p0: MediaRecorder?, p1: Int, p2: Int) {
        isRecording = false
    }

    override fun onInfo(p0: MediaRecorder?, p1: Int, p2: Int) {

    }

}