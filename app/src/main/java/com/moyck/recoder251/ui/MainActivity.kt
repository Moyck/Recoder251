package com.moyck.recoder251.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moyck.recoder251.R
import com.moyck.recoder251.base.BaseActivity
import com.moyck.recoder251.utils.StatusBarUtil

class MainActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        StatusBarUtil.setStatusBarLightMode(window)
    }

}
