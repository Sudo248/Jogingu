package com.sudo248.jogingu.service

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sudo248.data.chat.model.FirebaseMessageType
import com.sudo248.data.chat.model.Message
import com.sudo248.data.chat.model.UserToken
import com.sudo248.data.local.database.models.firebase.UserDocument
import com.sudo248.data.shared_preference.PrefKeys
import com.sudo248.data.shared_preference.SharedPref
import com.sudo248.jogingu.util.MessageUtils
import com.sudo248.jogingu.util.NotificationUtils
import com.sudo248.jogingu.util.convertToString
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class FirebaseService : FirebaseMessagingService() {

    companion object {
        val newMessage: MutableLiveData<Message> = MutableLiveData()
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val prefs: SharedPref by lazy {
        SharedPref(applicationContext)
    }

    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val dbRefUsers by lazy { firebaseFirestore.collection("users") }
    private val dbRefUserToken by lazy { firebaseFirestore.collection("user-token") }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.data.isNotEmpty().run {
            val msg = MessageUtils.map(message.data)
            if (msg.senderId == MessageUtils.receiverId) return
            if (msg.type == FirebaseMessageType.MESSAGE) {
                newMessage.postValue(msg.message)
            }
            GlobalScope.launch { createNotifications(msg.message) }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            val refreshedToken = it.result ?: return@addOnCompleteListener
        }
        saveUserToken(token)
    }

    private fun saveUserToken(token: String) {
        scope.launch {
            prefs.setToken(token)
            prefs.getUserId()?.let { userId ->
                val user = dbRefUserToken.whereEqualTo("userId", userId).get().await()
                if (user.isEmpty) {
                    dbRefUserToken.add(UserToken(userId = userId, token = token)).await()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private suspend fun createNotifications(message: Message) {
        val senderId = message.senderId

        val documentSnapshot = dbRefUsers.document(senderId).get().await()
        val user = documentSnapshot.toObject(UserDocument::class.java) ?: return

        val newMessageNotification = NewMessageNotification(
            notificationId = senderId.hashCode(),
            userString = convertToString(user),
            senderName = "${user.firstName} ${user.lastName}",
            senderPhotoUrl = user.imageUrl,
            message = message.content
        )
        NotificationUtils.makeStatusNotification(applicationContext, newMessageNotification)
    }


}