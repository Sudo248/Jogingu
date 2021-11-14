package com.sudo.jogingu.helper

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.sudo.jogingu.R

object ToastHelper{
    private lateinit var toast: Toast
    @SuppressLint("ShowToast", "ResourceAsColor")
    fun makeText(
        context: Context,
        content: String,
        duration: Int
    ): ToastHelper{
        this.toast = Toast.makeText(context, content, duration)
        val view = toast.view
        view?.setBackgroundResource(R.drawable.bg_toast)
        val textView = view?.findViewById<TextView>(android.R.id.message)
        textView?.setTextColor(R.color.black)
        return this
    }

    fun setGravity(gravity: Int, xOffSet: Int = 0, yOffSet: Int = 0): ToastHelper{
        this.toast.setGravity(gravity, xOffSet, yOffSet)
        return this
    }

    fun show(){
        this.toast.show()
    }


}