package com.sudo248.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Account
import com.sudo248.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.Timer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val firebaseAuth: FirebaseAuth = Firebase.auth

    override suspend fun login(account: Account): DataState<String> =
        suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(account.email, account.password)
                .addOnSuccessListener {
                    continuation.resume(DataState.Success(it.user?.uid ?: ""))
                }
                .addOnFailureListener {
                    continuation.resume(DataState.Error(it.message ?: "Unknown"))
                }
        }

    override suspend fun signup(account: Account): DataState<String> =
        suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(account.email, account.password)
                .addOnCompleteListener {
                    if (it.exception != null) {
                        continuation.resume(DataState.Error(it.exception?.message ?: "Unknown"))
                    } else {
                        continuation.resume(DataState.Success(it.result.user?.uid ?: ""))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(DataState.Error(it.message ?: "Unknown"))
                }
        }

    override suspend fun getCurrentAccount(): DataState<Account> {
        val firebaseUser = firebaseAuth.currentUser ?: return DataState.Error("Required login")

        return DataState.Success(
            Account(
                accountId = firebaseUser.uid, email = firebaseUser.email ?: "", password = ""
            )
        )
    }

    override suspend fun logout(userId: String) {

    }
}