package com.moyck.recoder251.ui.recoder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moyck.recoder251.R
import com.moyck.recoder251.adapter.RecoderAdapter
import com.moyck.recoder251.base.BaseActivity
import com.moyck.recoder251.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_recoder.*
import java.io.File

class RecoderActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_recoder
    }

    override fun initView() {
        StatusBarUtil.setStatusBarLightMode(window)
        val list = ArrayList<File>()
        list.add(File("w"))
        list.add(File("w"))
        list.add(File("w"))
        list.add(File("w"))
        list.add(File("w"))
        list.add(File("w"))
        list.add(File("w"))
        list.add(File("w"))
        list_view.adapter = RecoderAdapter(list, this)
    }

}
