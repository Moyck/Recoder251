package com.moyck.recoder251.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.moyck.recoder251.R
import java.io.File

/**
 * @package:com.moyck.recoder251.adapter
 * @date on 2019/12/4 18:51
 * @author  Moyuk
 */
class RecoderAdapter(private val files: ArrayList<File>, private val context: Context) :
    BaseAdapter() {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView = p1
        var viewHodler: ViewHodler
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recoder, null)
            viewHodler = ViewHodler()
            viewHodler.imgPlay = convertView!!.findViewById(R.id.img_play)
            viewHodler.tvName = convertView.findViewById(R.id.tv_name)
            viewHodler.tvSize = convertView.findViewById(R.id.tv_size)
            convertView.tag = viewHodler
        }else{
            viewHodler = convertView.tag as ViewHodler
        }
        return convertView
    }

    override fun getItem(p0: Int): Any {
        return files[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return files.size
    }

    class ViewHodler {

        lateinit var imgPlay: ImageView
        lateinit var tvName: TextView
        lateinit var tvSize: TextView

    }

}