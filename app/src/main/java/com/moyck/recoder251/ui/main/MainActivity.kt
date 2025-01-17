package com.moyck.recoder251.ui.main

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.isVisible
import com.moyck.recoder251.R
import com.moyck.recoder251.base.BaseActivity
import com.moyck.recoder251.ui.recoder.RecoderActivity
import com.moyck.recoder251.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.*
import android.app.Activity
import android.os.Build
import androidx.core.app.ComponentActivity.ExtraData
import com.moyck.recoder251.service.RecorderService


class MainActivity : BaseActivity(), MainContract.View, View.OnClickListener,
    TextView.OnEditorActionListener {


    var popupWindow: PopupWindow? = null
    private val presenter = MainPresenter()
    private val mBlog = "http://www.moyck.com:8080/articles/2019/11/29/1575013596580.html"
    private val zhihu = "https://www.bing.com"
    var imgStart: ImageView? = null
    var imgDownload: ImageView? = null

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        StatusBarUtil.setStatusBarLightMode(window)
        initWebview()
        img_home.setOnClickListener(this)
        img_more.setOnClickListener(this)
        et_url.setText(zhihu)
        et_url.setOnEditorActionListener(this)
        presenter.bind(this, MainModel())
        presenter.startFlash()
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        webview.loadUrl(p0?.text.toString())
        return false
    }

    fun initWebview() {
        webview.loadUrl(zhihu)
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
        webSettings.domStorageEnabled = true
        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                if (!url.startsWith("http")) {
                    return true
                }
                et_url.setText(url)
                return false
            }

        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.img_more -> showMorePop()
            R.id.img_start -> startRecoder()
            R.id.img_download -> toRecoderList()
            R.id.item_tv_help -> showHelpPage()
            R.id.img_home -> {
                hide404()
                webview.loadUrl(mBlog)
            }
            else -> show404()
        }
        if (popupWindow != null && resources.getResourceName(p0.id).contains("item_tv")) {
            popupWindow!!.dismiss()
        }
    }

    fun startRecoder() {
        if (!checkPermission()) {
            return
        }
        if (presenter.isRecording()) {
            presenter.stopRecoder()
            imgStart?.setColorFilter(Color.TRANSPARENT)
        } else {
            presenter.startRecoder()
            imgStart?.setColorFilter(Color.GREEN)
        }
    }

    fun hide404() {
        re_.visibility = View.GONE
        re_help.visibility = View.GONE
    }

    fun show404() {
        re_.visibility = View.VISIBLE
        re_help.visibility = View.GONE
    }

    fun showHelpPage() {
        re_help.visibility = View.VISIBLE
    }

    fun toRecoderList() {
        startActivity(Intent(this, RecoderActivity::class.java))
    }

    override fun flash(isLight: Boolean) {
        tv_.setTextColor(if (isLight) Color.GREEN else resources.getColor(R.color.hint_gray))
    }

    fun showMorePop() {
        val view = LayoutInflater.from(this).inflate(R.layout.pop_more, null)
        view.findViewById<ImageView>(R.id.img_start).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.img_download).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv1).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv2).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv3).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv4).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv5).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv6).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv7).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv8).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv9).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv10).setOnClickListener(this)
        view.findViewById<TextView>(R.id.item_tv_help).setOnClickListener(this)
        imgStart = view.findViewById<ImageView>(R.id.img_start)
        imgDownload = view.findViewById<ImageView>(com.moyck.recoder251.R.id.img_download)
        if (presenter.isRecording()) {
            imgStart?.setColorFilter(Color.GREEN)
        }
        popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(BitmapDrawable())
        popupWindow!!.showAsDropDown(img_more, -50, -100)
    }

    private fun checkPermission(): Boolean {
        val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE = arrayOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
        try {
            //检测是否有写的权限
            val permission = ActivityCompat.checkSelfPermission(
                this,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )
            val permission2 =
                ActivityCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO")
            if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
                return false
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onBackPressed() {
        if (re_.isVisible || re_help.isVisible) {
            hide404()
            return
        }
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            presenter.stopRecoder()
            super.onBackPressed()
        }
    }
}
