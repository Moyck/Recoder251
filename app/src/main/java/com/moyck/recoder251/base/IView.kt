package com.moyck.recoder251.base
import android.content.Context


interface IView {

    fun getLayout():Int

    fun initView()

    fun showToast(msg:String)

    fun getContext():Context

    fun pop()

}