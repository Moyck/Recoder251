package com.moyck.recoder251.ui.recoder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.moyck.recoder251.R
import com.moyck.recoder251.adapter.RecoderAdapter
import com.moyck.recoder251.base.BaseActivity
import com.moyck.recoder251.domain.MCallBack
import com.moyck.recoder251.domain.RecoderItem
import com.moyck.recoder251.utils.FileUtil
import com.moyck.recoder251.utils.MediaPlayerUtil
import com.moyck.recoder251.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_recoder.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class RecoderActivity : BaseActivity(), MCallBack, SeekBar.OnSeekBarChangeListener {
    var currentIndex = 0
    private val items = ArrayList<RecoderItem>()
    lateinit var adapter: RecoderAdapter
    lateinit var handler: Handler
    var currentViewHodler : RecoderAdapter.ViewHodler?=null
    var timer = Timer()

    override fun getLayout(): Int {
        return R.layout.activity_recoder
    }

    override fun initView() {
        val recoderFiles = FileUtil.getFilesAllFile(getExternalFilesDir("")?.path)
        if (recoderFiles.size == 0) {
            re_.visibility = View.VISIBLE
            return
        }
        recoderFiles.sortWith(SortCp())
        for (file in recoderFiles) {
            items.add(RecoderItem(file))
        }
        StatusBarUtil.setStatusBarLightMode(window)
        adapter = RecoderAdapter(items, this, this, this)
        list_view.adapter = adapter
        handler = Handler {
            adapter.notifyDataSetChanged()
            return@Handler true
        }
    }

    override fun onClick(position: Int,viewHodler: RecoderAdapter.ViewHodler) {
        Log.e("onClickonClick", "sss$position")
        if (items[position].isPlaying) {
            MediaPlayerUtil.pause()
            items[currentIndex].isPlaying = false
            adapter.notifyDataSetChanged()
            return
        }

        items[currentIndex].isPlaying = false
        items[position].isPlaying = true
        MediaPlayerUtil.startPlay(items[position].file)
        adapter.notifyDataSetChanged()
        currentIndex = position
        currentViewHodler = viewHodler
        startSeek()
    }

    fun startSeek() {
        timer = Timer()
        timer.schedule(object : TimerTask() {

            override fun run() {
                currentViewHodler?.seekBar!!.progress =  ((MediaPlayerUtil.player.currentPosition.toFloat() / MediaPlayerUtil.player.duration.toFloat()) * 100).toInt()
//                items[currentIndex].duration = MediaPlayerUtil.player.duration
//                items[currentIndex].progress =
//                    ((MediaPlayerUtil.player.currentPosition.toFloat() / MediaPlayerUtil.player.duration.toFloat()) * 100)
//                handler.sendEmptyMessage(0)
//                if (MediaPlayerUtil.player.duration < MediaPlayerUtil.player.currentPosition + 20) {
//                    items[currentIndex].isPlaying = false
//                    cancel()
//                }
//                Log.e(
//                    "sdfsdf",
//                    MediaPlayerUtil.player.currentPosition.toString() + "  " + MediaPlayerUtil.player.duration
//                )
            }

        }, 0, 1000)
    }


    class SortCp : Comparator<File> {

        override fun compare(p0: File, p1: File): Int {
            return if (p0.lastModified() > p1.lastModified()) -1 else 1
        }

    }

    override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
        if (p2) {
            timer.cancel()
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar) {
        Log.e("onProgressChanged", "onStartTrackingTouch")
    }

    override fun onStopTrackingTouch(p0: SeekBar) {
        Log.e("onProgressChanged", "onStopTrackingTouch")
        items[currentIndex].progress = p0.progress.toFloat()
        MediaPlayerUtil.seekTo(p0.progress.toFloat() / 100)
        startSeek()
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerUtil.stop()
    }
}
