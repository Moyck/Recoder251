package com.moyck.recoder251.ui.main

import android.media.MediaRecorder
import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class MainPresenter {

    var mMediaRecorder: MediaRecorder? = null
    var dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
    lateinit var view: MainContract.View
    lateinit var model: MainContract.Model
    var isRecording = false



    fun bind(view: MainContract.View, model: MainContract.Model){
        this.view = view
        this.model = model
    }

    fun stopRecoder(){
        if (!isRecording){
            return
        }
        try {
            mMediaRecorder?.stop()
            mMediaRecorder?.release()
            mMediaRecorder = null
        } catch (e: RuntimeException) {
            mMediaRecorder?.reset()
            mMediaRecorder?.release()
            mMediaRecorder = null
        }
        isRecording = false
    }

    fun startRecoder() {
        if (isRecording){
            stopRecoder()
            return
        }
        if (mMediaRecorder == null){
            mMediaRecorder = MediaRecorder()
            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)// 设置麦克风
            mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
        try {
            val fileName = dateFormat.format(Date()) + ".m4a"
            val filePath = view.getContext().filesDir.path + fileName
            mMediaRecorder?.setOutputFile(filePath)
            mMediaRecorder?.prepare()
            mMediaRecorder?.start()
            isRecording = true
        } catch (e: Exception) {
            Log.e("MainPresenter",""+e.localizedMessage)
        }


    }

}