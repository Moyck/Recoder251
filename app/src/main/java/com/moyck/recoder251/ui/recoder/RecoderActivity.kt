package com.moyck.recoder251.ui.recoder

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
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
import com.moyck.recoder251.ui.main.MainActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import androidx.core.content.FileProvider
import android.os.Build
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R
import com.moyck.recoder251.BuildConfig


class RecoderActivity : BaseActivity(), MCallBack, SeekBar.OnSeekBarChangeListener,
    AdapterView.OnItemClickListener {

    var currentIndex = 0
    private val items = ArrayList<RecoderItem>()
    lateinit var adapter: RecoderAdapter
    lateinit var handler: Handler
    var timer = Timer()

    override fun getLayout(): Int {
        return com.moyck.recoder251.R.layout.activity_recoder
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
        list_view.onItemClickListener = this
        handler = Handler {
            adapter.notifyDataSetChanged()
            return@Handler true
        }
    }

    fun updateList(){
        val recoderFiles = FileUtil.getFilesAllFile(getExternalFilesDir("")?.path)
        if (recoderFiles.size == 0) {
            re_.visibility = View.VISIBLE
            return
        }
        recoderFiles.sortWith(SortCp())
        items.clear()
        for (file in recoderFiles) {
            items.add(RecoderItem(file))
        }
        adapter.notifyDataSetChanged()
    }

    override fun onClick(position: Int) {
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
        startSeek()
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        MaterialDialog(this).show {
            listItems(com.moyck.recoder251.R.array.selection) { dialog, index, text ->
                Log.e("onItemClick", "index" + index)
                when (index) {
                    0 -> reNameFile(items[p2].file)
                    1 -> share(items[p2].file)
                    2 -> delectFile(items[p2].file)
                }
            }
        }
    }

    fun share(file:File){
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, file.path)
            type = "audio/*"
        }
        startActivity(Intent.createChooser(shareIntent, ""))
    }

    fun reNameFile(file:File) {
        MaterialDialog(this).show {
            input { materialDialog, charSequence ->
                file.renameTo(File(file.path.replace(file.name,charSequence.toString().trim()) +".m4a"))
                updateList()
            }
            title(com.moyck.recoder251.R.string.rename)
            positiveButton(com.moyck.recoder251.R.string.comfit)
            negativeButton(com.moyck.recoder251.R.string.cancel) { dialog ->
                // Do something
            }
        }
    }

    fun delectFile(file:File){
        MaterialDialog(this).show {
            title(com.moyck.recoder251.R.string.delect)
            positiveButton(com.moyck.recoder251.R.string.comfit){
                file.delete()
                updateList()
            }
            negativeButton(com.moyck.recoder251.R.string.cancel) {
                // Do something
            }
        }
    }

    fun startSeek() {
        timer = Timer()
        timer.schedule(object : TimerTask() {

            override fun run() {
                items[currentIndex].duration = MediaPlayerUtil.player.duration
                items[currentIndex].progress =
                    ((MediaPlayerUtil.player.currentPosition.toFloat() / MediaPlayerUtil.player.duration.toFloat()) * 100)
                handler.sendEmptyMessage(0)
                if (MediaPlayerUtil.player.duration < MediaPlayerUtil.player.currentPosition + 20) {
                    items[currentIndex].isPlaying = false
                    cancel()
                }
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
