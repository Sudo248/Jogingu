package com.sudo.jogingu.ui.fragments.target.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sudo.jogingu.ui.fragments.target.set_target.SetTargetFragment
import com.sudo.jogingu.ui.fragments.target.view_target.ViewTargetFragment


class TargetPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0->{
                ViewTargetFragment()
            }
            1->{
                SetTargetFragment()
            }
            else->{
                ViewTargetFragment()
            }

        }
    }
}