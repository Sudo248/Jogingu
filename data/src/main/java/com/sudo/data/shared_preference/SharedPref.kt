package com.sudo.data.shared_preference

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.sudo.data.shared_preference.PrefKeys.namePrefs
import com.sudo.data.util.calculateAge
import com.sudo.domain.entities.Gender
import com.sudo.domain.entities.Target
import com.sudo.domain.entities.User
import java.util.Date

class SharedPref(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(namePrefs, Context.MODE_PRIVATE)
    }

    fun isFirstOpenApp(): Boolean{
        return prefs.getBoolean(PrefKeys.isFirstOpenApp, true)
    }

    fun setIsFirstOpenApp(isFirstOpen: Boolean){
        val editor = prefs.edit()
        editor.putBoolean(PrefKeys.isFirstOpenApp, isFirstOpen)
        editor.apply()
    }

    fun getDistanceTarget(): Int{
        return prefs.getInt(TargetKeys.distance, 0)
    }

    fun setDistanceTarget(distance: Int){
        val editor = prefs.edit()
        editor.putInt(TargetKeys.distance, distance)
        editor.apply()
    }

    fun getCaloTarget(): Int{
        return prefs.getInt(TargetKeys.calo, 0)
    }

    fun setCaloTarget(calo: Int){
        val editor = prefs.edit()
        editor.putInt(TargetKeys.calo, calo)
        editor.apply()
    }

    fun getRecursiveTarget(): Date?{
        val time = prefs.getLong(TargetKeys.recursive, 0)
        return if(time == 0L) null else Date(time)
    }

    fun setRecursiveTarget(date: Date?){
        val editor = prefs.edit()
        editor.putLong(TargetKeys.recursive, date?.time ?: 0)
        editor.apply()
    }

    suspend fun setTarget(target: Target){
        synchronized(prefs){
            setDistanceTarget(target.distance)
            setCaloTarget(target.calo)
            setRecursiveTarget(target.recursive)
        }
    }

    suspend fun getTarget(): Target{
        synchronized(prefs){
            return Target(
                distance = getDistanceTarget(),
                calo = getCaloTarget(),
                recursive = getRecursiveTarget()
            )
        }
    }

    fun setFirstNameUser(firstName: String){
        val editor = prefs.edit()
        editor.putString(UserKeys.firstName, firstName)
        editor.apply()
    }

    fun getFirstNameUser(): String{
        return prefs.getString(UserKeys.firstName, "") ?: ""
    }

    fun setLastNameUser(lastName: String){
        val editor = prefs.edit()
        editor.putString(UserKeys.lastName, lastName)
        editor.apply()
    }

    fun getLastNameUser(): String{
        return prefs.getString(UserKeys.lastName, "") ?: ""
    }

    fun setCityUser(city: String){
        val editor = prefs.edit()
        editor.putString(UserKeys.city, city)
        editor.apply()
    }

    fun getCityUser(): String{
        return prefs.getString(UserKeys.city, "") ?: ""
    }

    fun setCountryUser(country: String){
        val editor = prefs.edit()
        editor.putString(UserKeys.country, country)
        editor.apply()
    }

    fun getCountryUser(): String{
        return prefs.getString(UserKeys.country, "") ?: ""
    }

    fun setPrimarySportUser(primarySport: String){
        val editor = prefs.edit()
        editor.putString(UserKeys.primarySport, primarySport)
        editor.apply()
    }

    fun getPrimarySport(): String{
        return prefs.getString(UserKeys.primarySport, "") ?: ""
    }

    fun setGenderUser(gender: Gender){
        val editor = prefs.edit()
        editor.putInt(UserKeys.gender, gender.ordinal)
        editor.apply()
    }

    fun getGenderUser(): Gender{
        return Gender.values()[prefs.getInt(UserKeys.gender, 0)]
    }

    fun setBirthdayUser(birthday: Date){
        val editor = prefs.edit()
        editor.putLong(UserKeys.birthday, birthday.time)
        editor.apply()
    }

    fun getBirthDayUser(): Date{
        return Date(prefs.getLong(UserKeys.birthday, 0))
    }

    fun setHeightUser(height: Short){
        val editor = prefs.edit()
        editor.putInt(UserKeys.height, height.toInt())
        editor.apply()
    }

    fun getHeightUser(): Short{
        return prefs.getInt(UserKeys.height, 0).toShort()
    }

    fun setWeightUser(weight: Short){
        val editor = prefs.edit()
        editor.putInt(UserKeys.height, weight.toInt())
        editor.apply()
    }

    fun getWeightUser(): Short{
        return prefs.getInt(UserKeys.weight, 0).toShort()
    }

    fun setImageUser(imageInByteArray: ByteArray?){
        val editor = prefs.edit()
        editor.putString(UserKeys.imageInByteArray, Base64.encodeToString(imageInByteArray, Base64.DEFAULT))
        editor.apply()
    }

    fun getImageUser(): ByteArray?{
        return Base64.decode(prefs.getString(UserKeys.imageInByteArray,""), Base64.DEFAULT)
    }

    suspend fun setUser(user: User){
        synchronized(prefs){
            setFirstNameUser(user.firstName)
            setLastNameUser(user.lastName)
            setCityUser(user.city)
            setCountryUser(user.country)
            setPrimarySportUser(user.primarySport)
            setGenderUser(user.gender)
            setBirthdayUser(user.birthday)
            setHeightUser(user.height)
            setWeightUser(user.weight)
            setImageUser(user.imageInByteArray)
        }
    }

    suspend fun getUser(): User{
        synchronized(prefs){
            return User(
               firstName = getFirstNameUser(),
                lastName = getLastNameUser(),
                city = getCityUser(),
                country = getCountryUser(),
                primarySport = getPrimarySport(),
                gender = getGenderUser(),
                birthday = getBirthDayUser(),
                age = calculateAge(getBirthDayUser()).toByte(),
                height = getHeightUser(),
                weight = getWeightUser(),
                imageInByteArray = getImageUser()
            )
        }
    }

}