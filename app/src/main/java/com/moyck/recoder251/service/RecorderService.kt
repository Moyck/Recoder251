package com.moyck.recoder251.service

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import com.moyck.recoder251.R
import com.moyck.recoder251.ui.main.MainActivity
import com.moyck.recoder251.ui.main.MainContract
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class RecorderService : Service() {

    var mMediaRecorder: MediaRecorder? = null
    var dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
    var isRecording = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val builder = Notification.Builder(this.applicationContext)
        val nfIntent = Intent(this, MainActivity::class.java)
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.ic_launcher
                )
            ) // 设置下拉列表中的图标(大图标)
            .setContentTitle("正在后台运行") // 设置下拉列表里的标题
            .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
            .setContentText("点击返回App") // 设置上下文内容
            .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //修改安卓8.1以上系统报错
            val notificationChannel =
                NotificationChannel("195", "reco", NotificationManager.IMPORTANCE_MIN);
            notificationChannel.enableLights(false)
            notificationChannel.setShowBadge(false)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET;
            val manager = getSystemService (NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
            builder.setChannelId("195")
        }

        val notification = builder.build()// 获取构建好的Notification
        startForeground(195, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        startRecoder()
    }

    override fun onDestroy() {
        stopRecoder()
        stopForeground(true)
        super.onDestroy()
    }

    fun stopRecoder() {
        if (!isRecording) {
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
//        if (isRecording) {
//            stopRecoder()
//            return
//        }
        if (mMediaRecorder == null) {
            mMediaRecorder = MediaRecorder()
            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)// 设置麦克风
            mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//            mMediaRecorder?.setOnErrorListener(this)
//            mMediaRecorder?.setOnInfoListener(this)
        }
        try {
            val fileName = dateFormat.format(Date()) + ".m4a"
            val filePath = getExternalFilesDir("")
            filePath?.mkdirs()
            mMediaRecorder?.setOutputFile(filePath?.path + "/" + fileName)
            Log.e("MainPresenter", "" + filePath?.path + "/" + fileName)
            mMediaRecorder?.prepare()
            mMediaRecorder?.start()
            isRecording = true
//            startFlash()
        } catch (e: Exception) {
            Log.e("MainPresenter", "" + e.localizedMessage)
        }
    }

}
