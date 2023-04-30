package com.sudo248.jogingu.ui.activities.main.fragments.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.sudo248.data.chat.model.Conversation
import com.sudo248.data.chat.model.ConversationDocument
import com.sudo248.data.chat.model.FirebaseMessage
import com.sudo248.data.local.database.models.firebase.UserDocument
import com.sudo248.data.mapper.toUser
import com.sudo248.data.shared_preference.SharedPref
import com.sudo248.domain.common.UiState
import com.sudo248.domain.entities.User
import com.sudo248.jogingu.service.FirebaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatViewModel() : ViewModel() {

    val auth by lazy { FirebaseAuth.getInstance().currentUser!! }
    private val dbRefConversation by lazy {
        FirebaseFirestore.getInstance().collection("conversations")
    }
    private val dbRefUser by lazy {
        FirebaseFirestore.getInstance().collection("users")
    }

    private val _state = MutableLiveData<UiState>(UiState.IDLE)
    val state: LiveData<UiState> = _state

    val otherUser = MutableLiveData<User>()

    val conversation = MutableLiveData<Conversation>()

    val newMessage = FirebaseService.newMessage

    fun setOtherId(otherId: String) = viewModelScope.launch{
        getUserInfo(otherId)
        getConversation(otherId)
    }

    fun getUserInfo(otherId: String) = viewModelScope.launch(Dispatchers.IO){
        val userDoc = dbRefUser.whereEqualTo("userId", otherUser).limit(1).get().await().first().toObject(UserDocument::class.java)
        otherUser.postValue(userDoc.toUser())

    }

    fun getConversation(otherId: String) = viewModelScope.launch(Dispatchers.IO) {
        _state.postValue(UiState.LOADING)
        val listUser = listOf(auth.uid, otherId).sorted()
        val conversationDocs =
            dbRefConversation.whereEqualTo("conversationId", "${listUser[0]}-${listUser[1]}")
                .limit(1).get().await()
        val currentConversation = if (conversationDocs.isEmpty) {
            createNewConversation("${listUser[0]}-${listUser[1]}", auth.uid, otherId)
        } else {
            conversationDocs.first().toObject(ConversationDocument::class.java).toConversation()
        }
        conversation.postValue(currentConversation)
    }

    private suspend fun createNewConversation(conversationId: String, firstUserId: String, secondUserId: String): Conversation {
        val conversationDocument = ConversationDocument(
            conversationId = conversationId,
            firstUserId = firstUserId,
            secondUserId = secondUserId,
        )
        dbRefConversation.add(conversationDocument).await()
        return conversationDocument.toConversation()
    }

    fun sendMessage(message: String) {

    }

}