package com.sudo.jogingu.ui.fragments.run

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.FragmentRunningBinding

class RunningFragment : Fragment() {
    lateinit var binding: FragmentRunningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunningBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabResumeContinue.setOnClickListener {
            binding.glStartRun.setGuidelinePercent(0.5F)
        }

    }
}