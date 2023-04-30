package com.sudo248.jogingu.ui.activities.main.fragments.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sudo248.data.chat.model.Message
import com.sudo248.jogingu.databinding.ItemChatMeBinding
import com.sudo248.jogingu.databinding.ItemChatOtherBinding
import com.sudo248.jogingu.util.invisible
import com.sudo248.jogingu.util.loadImageFromUrl
import com.sudo248.jogingu.util.visible
import kotlinx.coroutines.coroutineScope

class ChatAdapter(
    private val clientId: String
) : RecyclerView.Adapter<ChatViewHolder>() {

    companion object {
        const val VIEW_CHAT_ME = 1
        const val VIEW_CHAT_OTHER = 2
    }

    val messages: MutableList<Message> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(messages: List<Message>) {
        this.messages.clear()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }


    suspend fun addMessage(message: Message) = coroutineScope {
        messages.add(message)
        if (messages.size > 1) {
            notifyItemChanged(messages.size - 2)
        }
        notifyItemInserted(messages.size - 1)
    }

    override fun getItemViewType(position: Int): Int = when (messages[position].senderId) {
        clientId -> VIEW_CHAT_ME
        else -> VIEW_CHAT_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return when (viewType) {
            VIEW_CHAT_ME -> {
                ChatMeViewHolder(
                    ItemChatMeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                ChatOtherViewHolder(
                    ItemChatOtherBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.onBind(
            messages[position],
            position,
            isSameNext = (position < messages.size - 1 && messages[position].senderId == messages[position + 1].senderId)
        )
        Log.d("sudoo", "onBindViewHolder: messages: ${messages.size} ")
        if (position < messages.size - 1) {
            Log.d("sudoo", "onBindViewHolder: position: $position")
            Log.d(
                "sudoo",
                "onBindViewHolder: ${position < messages.size - 1} && ${messages[position].senderId == messages[position + 1].senderId}"
            )
        }
    }

    override fun getItemCount(): Int = messages.size


}

abstract class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun onBind(message: Message, position: Int, isSameNext: Boolean = false)
}

class ChatMeViewHolder(private val binding: ItemChatMeBinding) :
    ChatViewHolder(binding.root) {
    override fun onBind(message: Message, position: Int, isSameNext: Boolean) {
        binding.apply {
            if (isSameNext) {
                root.setPadding(root.paddingLeft, root.paddingTop, root.paddingRight, 0)
            }
            txtContent.text = message.content
        }
    }

}

class ChatOtherViewHolder(private val binding: ItemChatOtherBinding) :
    ChatViewHolder(binding.root) {
    override fun onBind(message: Message, position: Int, isSameNext: Boolean) {
        binding.apply {
            if (isSameNext) {
                cardAvatar.invisible()
                root.setPadding(root.paddingLeft, root.paddingTop, root.paddingRight, 0)
            } else {
                cardAvatar.visible()
                imgAvatar.loadImageFromUrl(message.senderImage)
            }
            txtContent.text = message.content
        }
    }
}
