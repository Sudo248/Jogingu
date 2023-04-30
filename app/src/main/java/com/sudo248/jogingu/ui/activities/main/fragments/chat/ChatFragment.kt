package com.sudo248.jogingu.ui.activities.main.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.sudo248.jogingu.databinding.FragmentChatBinding
import com.sudo248.jogingu.ui.activities.main.MainActivity
import com.sudo248.jogingu.util.loadImageFromUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatFragment (private val otherId: String) : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    companion object {
        fun newInstance(otherId: String): ChatFragment {
            return ChatFragment(otherId)
        }
    }

    private val autoScroll = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            scroll()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            scroll()
        }

        override fun onChanged() {
            super.onChanged()
            scroll()
        }
    }

    private fun scroll() {
        lifecycleScope.launch {
            delay(100)
            binding.rcvChat.scrollToPosition(chatAdapter.messages.size - 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater)
        setupUi()
        setupAdapter()
        setOnClickListener()
        observer()
        return binding.root
    }

    private fun setupUi() {
        getMainActivity().hideBottomNavigation()
        viewModel.setOtherId(otherId)
        viewModel.otherUser.observe(viewLifecycleOwner) {
            binding.txtTitleChat.text = "${it.firstName} ${it.lastName}"
            binding.imgAvatar.loadImageFromUrl(it.imageUrl)
        }
        binding.apply {

//            edtInputMessage.setOnFocusChangeListener { _, hasFocus ->
//                if (hasFocus) {
//                    imgAddImage.gone()
//                } else {
//                    imgAddImage.visible()
//                }
//            }

//            edtInputMessage.addTextChangedListener {
//                if (it.isNullOrEmpty()) {
//                    imgAddImage.visible()
//                } else {
//                    imgAddImage.gone()
//                }
//            }
        }
    }

    private fun setupAdapter() {
        chatAdapter = ChatAdapter(viewModel.auth.uid)
        chatAdapter.registerAdapterDataObserver(autoScroll)
        binding.rcvChat.adapter = chatAdapter
    }

    private fun setOnClickListener() {
        binding.apply {
            imgBack.setOnClickListener {
                onBackPress()
            }

            imgSend.setOnClickListener {
                viewModel.sendMessage(edtInputMessage.text.toString())
                edtInputMessage.text.clear()
            }
        }
    }

    private fun observer() {
        viewModel.newMessage.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                chatAdapter.addMessage(it)
            }
        }
        viewModel.conversation.observe(viewLifecycleOwner) {
            chatAdapter.submitList(it.messages)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getMainActivity().showBottomNavigation()
        chatAdapter.unregisterAdapterDataObserver(autoScroll)
    }

    private fun getMainActivity(): MainActivity {
        return activity as MainActivity
    }

    private fun onBackPress() {
        findNavController().popBackStack()
    }
}