package com.moyck.recoder251.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.PopupWindow
import androidx.cardview.widget.CardView
import com.moyck.recoder251.R
import com.moyck.recoder251.base.BaseActivity
import com.moyck.recoder251.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View, View.OnClickListener {

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        StatusBarUtil.setStatusBarLightMode(window)
        initWebview()
        img_home.setOnClickListener(this)
        img_more.setOnClickListener(this)
    }

    fun initWebview() {
        webview.loadUrl("http://www.moyck.com")
        //声明WebSettings子类

        val webSettings = webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.allowFileAccess = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = "utf-8"
        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                webview.loadUrl(url)
                return true
            }

        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.img_more -> showMorePop()
        }
    }

    fun showMorePop(){
        val view = LayoutInflater.from(this).inflate(R.layout.pop_more, null)
        val popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.isOutsideTouchable = false
        popupWindow.showAsDropDown(img_more,-50,-100)
    }

    override fun onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
