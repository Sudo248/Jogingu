package com.sudo.jogingu.ui.fragments.target

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.FragmentTargetBinding
import com.sudo.jogingu.ui.fragments.target.adapter.TargetPageAdapter
import android.content.Intent
import android.graphics.Rect
import android.view.*

import androidx.localbroadcastmanager.content.LocalBroadcastManager

import android.view.ViewTreeObserver.OnGlobalLayoutListener


class TargetFragment : Fragment() {
    lateinit var binding: FragmentTargetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTargetBinding.inflate(inflater, container, false)

        val adapter = TargetPageAdapter(childFragmentManager, lifecycle)
        binding.vp2ContainFragment.adapter = adapter

        TabLayoutMediator(binding.tabLayoutTarget, binding.vp2ContainFragment) { tab, position ->
            when(position) {
                0->{
                    tab.text = getString(R.string.target_view_tab)
                }
                1->{
                    tab.text = getString(R.string.target_set_tab)
                }
            }
        }.attach()

        return binding.root
    }



}