package com.moyck.recoder251.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.moyck.recoder251.R
import com.moyck.recoder251.domain.MCallBack
import com.moyck.recoder251.domain.RecoderItem
import com.moyck.recoder251.utils.FileSizeUtil
import com.moyck.recoder251.utils.MediaPlayerUtil
import kotlinx.android.synthetic.main.item_recoder.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @package:com.moyck.recoder251.adapter
 * @date on 2019/12/4 18:51
 * @author  Moyuk
 */
class RecoderAdapter(
    private val items: List<RecoderItem>,
    private val context: Context,
    private val callBack: MCallBack,
    private val seekBarChange: SeekBar.OnSeekBarChangeListener
) :
    BaseAdapter() {

    var dateFormatDay = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE)
    var dateFormatTime = SimpleDateFormat("mm:ss", Locale.CHINESE)
    var currentViewHodler: ViewHodler? = null

    @SuppressLint("SetTextI18n")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView = p1
        var viewHodler: ViewHodler
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recoder, null)
            viewHodler = ViewHodler()
            viewHodler.imgPlay = convertView!!.findViewById(R.id.img_play)
            viewHodler.tvName = convertView.findViewById(R.id.tv_name)
            viewHodler.tvSize = convertView.findViewById(R.id.tv_size)
            viewHodler.tvTime = convertView.findViewById(R.id.tv_time)
            viewHodler.seekBar = convertView.findViewById(R.id.seekbar)
            viewHodler.tvDuration = convertView.findViewById(R.id.tv_duration)
            viewHodler.reSeek = convertView.findViewById(R.id.re_seek)
            convertView.tag = viewHodler
        } else {
            viewHodler = convertView.tag as ViewHodler
        }

        viewHodler.tvName.text = items[p0].file.name
        if (items[p0].isPlaying) {
            viewHodler.tvSize.visibility = View.GONE
            viewHodler.reSeek.visibility = View.VISIBLE
            viewHodler.imgPlay.setImageResource(R.drawable.stop)
            viewHodler.seekBar.seekbar.progress = items[p0].progress.toInt()
            viewHodler.seekBar.seekbar.setOnSeekBarChangeListener(seekBarChange)
            val dateDuration = Date()
            val dateProsition = Date()
            dateDuration.time = items[p0].duration.toLong()
            dateProsition.time = (items[p0].duration * items[p0].progress.toFloat() / 100).toLong()
            Log.e("ssdadateProsition", "" + items[p0].progress)
            viewHodler.tvDuration.text =
                dateFormatTime.format(dateProsition) + "/" + dateFormatTime.format(dateDuration)
        } else {
            viewHodler.tvSize.visibility = View.VISIBLE
            viewHodler.reSeek.visibility = View.GONE
            viewHodler.imgPlay.setImageResource(R.drawable.play)
            viewHodler.tvSize.text =
                dateFormatTime.format(items[p0].file.lastModified()) + "     " + FileSizeUtil.getAutoFileOrFilesSize(
                    items[p0].file.path
                )
        }


        val thisTime = dateFormatDay.format(items[p0].file.lastModified())
        viewHodler.tvTime.text = thisTime
        viewHodler.tvTime.visibility = View.GONE
        if (p0 != 0) {
            val lastTime = dateFormatDay.format(items[p0 - 1].file.lastModified())
            if (thisTime != lastTime) {
                viewHodler.tvTime.visibility = View.VISIBLE
            }
        } else {
            viewHodler.tvTime.visibility = View.VISIBLE
        }

        viewHodler.imgPlay.setOnClickListener {
            callBack.onClick(p0,viewHodler)
        }

        return convertView
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    class ViewHodler {
        lateinit var imgPlay: ImageView
        lateinit var tvName: TextView
        lateinit var tvSize: TextView
        lateinit var tvTime: TextView
        lateinit var tvDuration: TextView
        lateinit var seekBar: SeekBar
        lateinit var reSeek: RelativeLayout
    }

}