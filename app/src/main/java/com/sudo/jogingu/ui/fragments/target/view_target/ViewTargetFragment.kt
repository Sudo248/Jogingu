package com.sudo.jogingu.ui.fragments.target.view_target

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sudo.domain.entities.Target
import com.sudo.jogingu.databinding.FragmentViewTargetBinding
import com.sudo.jogingu.ui.fragments.target.TargetViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class ViewTargetFragment : Fragment() {

    private lateinit var binding: FragmentViewTargetBinding

    private val viewModel: TargetViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("hh:MM")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewTargetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.target.observe(viewLifecycleOwner){
            setTarget(it)
        }

    }

    private fun setTarget(target: Target) {
        with(binding){
            tvViewTargetAmountDistance.text = "${target.distance}"
            tvViewTargetAmountCalo.text = "${target.calo}"
            tvViewTargetTimeStart.text = timeFormat.format(target.timeStart)
            tvViewTargetSpecificPlace.text = target.place
            tvViewTargetNumberDaysRecurring.text = "${target.recursive}"
            tvViewTargetMinutesNotificationEarlier.text = "${target.notificationBefore}"
        }
    }

}