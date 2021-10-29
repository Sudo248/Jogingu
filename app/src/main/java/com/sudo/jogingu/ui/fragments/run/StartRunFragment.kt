package com.sudo.jogingu.ui.fragments.run

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.FragmentStartRunBinding

class StartRunFragment : Fragment() {
    lateinit var binding: FragmentStartRunBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartRunBinding.inflate(inflater, container, false)

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.fabStart.setOnClickListener {
//            activity?.findNavController(R.id.fc_run)?.navigate(R.id.action_startRunFragment_to_runningFragment)
//        }
//    }
}