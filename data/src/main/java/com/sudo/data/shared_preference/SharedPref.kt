package com.sudo.data.shared_preference

import android.content.Context
import android.content.SharedPreferences
import java.util.Date

class SharedPref(private val context: Context) {

    private val namePrefs = "JoginguPrefs"
    private val isFirstOpenApp = "is_first_open_app"
    private val distanceTarget = "distance_target"
    private val caloTarget = "calo_target"
    private val recursiveTarget = "recursive_target"

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(namePrefs, Context.MODE_PRIVATE)
    }
    private val editor = prefs.edit()

    fun isFirstOpenApp(): Boolean{
        return prefs.getBoolean(isFirstOpenApp, true)
    }

    fun setIsFirstOpenApp(isFirstOpen: Boolean){
        editor.putBoolean(isFirstOpenApp, isFirstOpen)
        editor.apply()
    }

    fun getDistanceTarget(): Int{
        return prefs.getInt(distanceTarget, 0)
    }
    fun setDistanceTarget(distance: Int){
        editor.putInt(distanceTarget, distance)
        editor.apply()
    }

    fun getCaloTarget(): Int{
        return prefs.getInt(caloTarget, 0)
    }

    fun setCaloTarget(calo: Int){
        editor.putInt(caloTarget, calo)
        editor.apply()
    }

    fun getRecursiveTarget(): Date?{
        val time = prefs.getLong(recursiveTarget, 0)
        return if(time == 0L) null else Date(time)
    }

    fun setRecursiveTarget(date: Date?){
        if (date != null) {
            editor.putLong(recursiveTarget, date.time)
        }else{
            editor.putLong(recursiveTarget, 0)
        }
        editor.apply()
    }

}