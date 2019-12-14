package com.moyck.recoder251.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.moyck.recoder251.R
import com.moyck.recoder251.base.BaseActivity
import com.moyck.recoder251.ui.main.MainActivity
import com.moyck.recoder251.utils.StatusBarUtil

class SplashActivity : BaseActivity() {
    val handler = Handler()

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        StatusBarUtil.setStatusBarLightMode(window)
        handler.postDelayed(object :Runnable{
            override fun run() {
                startActivity( Intent(this@SplashActivity, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        },1000)
    }

}
