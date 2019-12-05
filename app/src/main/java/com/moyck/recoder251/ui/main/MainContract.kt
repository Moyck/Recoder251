package com.moyck.recoder251.ui.main

import com.moyck.recoder251.base.IModel
import com.moyck.recoder251.base.IView

abstract class MainContract {

    interface View :IView{
        fun flash(isLight:Boolean)
    }


    interface Model :IModel{

    }
}