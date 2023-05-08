package com.sudo248.data.repositories

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sudo248.data.firebase.FirebaseKeys
import com.sudo248.data.local.database.dao.JoginguDao
import com.sudo248.data.local.database.models.firebase.UserDocument
import com.sudo248.data.mapper.toUser
import com.sudo248.data.mapper.toUserDocument
import com.sudo248.data.shared_preference.SharedPref
import com.sudo248.data.util.calculateAge
import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Gender
import com.sudo248.domain.entities.User
import com.sudo248.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val pref: SharedPref,
) : UserRepository {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val storage: FirebaseStorage = Firebase.storage

    override suspend fun setUser(user: User) = coroutineScope {
        val userDocument = user.toUserDocument(userId = Firebase.auth.currentUser?.uid)
        val url = if (user.imageUrl.isNullOrEmpty()) {
            FirebaseKeys.DEFAULT_AVATAR_URL
        } else {
            uploadImage(
                "avatar_${userDocument.userId}_${System.currentTimeMillis()}",
                user.imageUrl!!
            )
        }

        firestore.collection(FirebaseKeys.COLLECTION_USER)
            .add(userDocument.copy(imageUrl = url)).await()

        pref.setUser(user.copy(userId = userDocument.userId, imageUrl = url))
    }

    override suspend fun updateUser(user: User, isUpdateImage: Boolean) = coroutineScope {
        val userDocument = user.toUserDocument()

        val url = if (isUpdateImage) {
            uploadImage(
                "avatar_${userDocument.userId}_${System.currentTimeMillis()}",
                user.imageUrl!!
            )
        } else {
            user.imageUrl!!
        }

        firestore.collection(FirebaseKeys.COLLECTION_USER).apply {
            val docId =
                whereEqualTo("userId", userDocument.userId).limit(1).get().await().first().id
            document(docId).set(userDocument.copy(imageUrl = url)).await()
        }
        pref.setUser(user.copy(imageUrl = url))
    }

    override suspend fun getUser(): Flow<DataState<User>> = flow {
        emit(DataState.Loading)
        try {
            val user = pref.getUser()
            if (user.userId == null) {
                Firebase.auth.currentUser?.let {
                    val userDocument = firestore.collection(FirebaseKeys.COLLECTION_USER)
                        .whereEqualTo("userId", it.uid)
                        .limit(1).get().await()
                    if (userDocument.isEmpty) {
                        emit(DataState.Error("No user"))
                    } else {
                        val _user = userDocument.first().toObject(UserDocument::class.java).toUser()
                        pref.setUser(_user)
                        emit(DataState.Success(_user))
                    }
                }
            } else {
                emit(DataState.Success(user))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(DataState.Error("${e.message}"))
        }
    }

    override suspend fun getFullNameUser(): String {
        return pref.getLastNameUser() + pref.getFirstNameUser()
    }

    override suspend fun getBMRUser(): Float {
        val height = pref.getHeightUser()
        val weight = pref.getWeightUser()
        val gender = pref.getGenderUser()
        val age = pref.getBirthDayUser()
        return if (gender == Gender.FEMALE) {
            (9.247f * weight) + (3.098f * height) - (4.33f * calculateAge(age)) + 447.593f
        } else {
            (13.397f * weight) + (4.799f * height) - (5.677f * calculateAge(age)) + 88.362f
        }
    }

    override suspend fun loadImageFromDevice(pathImage: String): Flow<DataState<ByteArray>> = flow {
        emit(DataState.Loading)
        try {

        } catch (e: Exception) {
            emit(DataState.Error("${e.message}"))
        }
    }

    private suspend fun uploadImage(imageName: String, imageUrl: String): String = coroutineScope {
        val typeImage = imageUrl.substringAfterLast(".")
        val stream = FileInputStream(File(imageUrl))
        val avatarRef = storage.reference.child("$imageName.$typeImage")
        avatarRef.putStream(stream).await()
        avatarRef.path
    }
}