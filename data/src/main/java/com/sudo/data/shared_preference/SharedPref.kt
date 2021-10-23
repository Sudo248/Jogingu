package com.sudo.data.shared_preference

import android.content.Context
import android.content.SharedPreferences

class SharedPref(private val context: Context) {

    private val namePrefs = "JoginguPrefs"
    private val isFirstOpenApp = "is_first_open_app"

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(namePrefs, Context.MODE_PRIVATE)
    }

    suspend fun isFirstOpenApp(): Boolean{
        return prefs.getBoolean(isFirstOpenApp, true)
    }

    suspend fun setIsFirstOpenApp(isFirstOpen: Boolean){
        val editor = prefs.edit()
        editor.putBoolean(isFirstOpenApp, isFirstOpen)
        editor.apply()
    }

}