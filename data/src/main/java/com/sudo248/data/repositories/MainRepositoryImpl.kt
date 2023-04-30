package com.sudo248.data.repositories

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sudo248.data.local.database.dao.JoginguDao
import com.sudo248.data.mapper.*
import com.sudo248.data.shared_preference.SharedPref
import com.sudo248.data.util.calculateAge
import com.sudo248.data.util.toRunInStatistic
import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.*
import com.sudo248.domain.entities.Target
import com.sudo248.domain.repositories.MainRepository
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val pref: SharedPref
) : MainRepository{

    override suspend fun isFirstOpenApp(): Boolean {
        return pref.isFirstOpenApp()
    }

    override suspend fun isFirstOpenApp(isFirstOpenApp: Boolean) {
        pref.setIsFirstOpenApp(isFirstOpenApp)
    }
}