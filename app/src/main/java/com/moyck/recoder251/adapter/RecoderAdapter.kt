package com.moyck.recoder251.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.io.File

/**
 * @package:com.moyck.recoder251.adapter
 * @date on 2019/12/4 18:51
 * @author  Moyuk
 */
class RecoderAdapter(private val files: ArrayList<File>, val context: Context) : BaseAdapter() {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

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

}