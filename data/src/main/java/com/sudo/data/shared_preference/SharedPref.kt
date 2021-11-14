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

    fun getPlaceTarget(): String? {
        return prefs.getString(TargetKeys.place, null)
    }

    fun setPlaceTarget(place: String?){
        val editor = prefs.edit()
        editor.putString(TargetKeys.place, place)
        editor.apply()
    }

    fun getRecursiveTarget(): Int{
        return prefs.getInt(TargetKeys.recursive, 0)
    }

    fun setRecursiveTarget(recursive: Int){
        val editor = prefs.edit()
        editor.putInt(TargetKeys.recursive, recursive)
        editor.apply()
    }

    fun getNotificationTarget(): Int{
        return prefs.getInt(TargetKeys.notificationBefore, 10)
    }

    fun setNotificationTarget(notificationBefore: Int){
        val editor = prefs.edit()
        editor.putInt(TargetKeys.notificationBefore, notificationBefore)
        editor.apply()
    }

    fun setTimeStart(time: Long){
        val editor = prefs.edit()
        editor.putLong(TargetKeys.timeStart, time)
        editor.apply()
    }

    fun getTimeStart(): Long{
        return prefs.getLong(TargetKeys.timeStart, 0L)
    }

    suspend fun setTarget(target: Target){
        synchronized(prefs){
            setDistanceTarget(target.distance)
            setCaloTarget(target.calo)
            setRecursiveTarget(target.recursive)
            setPlaceTarget(target.place)
            setNotificationTarget(target.notificationBefore)
            setTimeStart(target.timeStart)
        }
    }

    suspend fun getTarget(): Target{
        synchronized(prefs){
            return Target(
                distance = getDistanceTarget(),
                calo = getCaloTarget(),
                recursive = getRecursiveTarget(),
                place = getPlaceTarget(),
                notificationBefore = getNotificationTarget(),
                timeStart = getTimeStart()
            )
        }
    }

    suspend fun deleteTarget(){
        synchronized(prefs){
            setDistanceTarget(0)
            setCaloTarget(0)
            setTimeStart(0L)
            setNotificationTarget(0)
            setPlaceTarget(null)
            setRecursiveTarget(0)
        }
    }

    fun setUserId(userId: String?){
        val editor = prefs.edit()
        editor.putString(UserKeys.userId, userId)
        editor.apply()
    }

    fun getUserId(): String?{
        return prefs.getString(UserKeys.userId, null)
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
        imageInByteArray?.let{
            val editor = prefs.edit()
            editor.putString(UserKeys.imageInByteArray, Base64.encodeToString(it, Base64.DEFAULT))
            editor.apply()
        }
    }

    fun getImageUser(): ByteArray?{
        prefs.getString(UserKeys.imageInByteArray, null)?.let{
            return Base64.decode(it, Base64.DEFAULT)
        }
        return null
    }

    suspend fun setUser(user: User){
        synchronized(prefs){
            setUserId(user.userId)
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
                userId = getUserId(),
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