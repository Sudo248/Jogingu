package com.sudo.jogingu.ui.fragments.target.set_target

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.FragmentSetTargetBinding
import com.sudo.jogingu.ui.fragments.target.TargetViewModel
import com.sudo.domain.entities.Target
import com.sudo.jogingu.helper.ToastHelper
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class SetTargetFragment : Fragment() {
    private lateinit var binding: FragmentSetTargetBinding

    private val viewModel: TargetViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private lateinit var calendar: Calendar
    private var currentHour: Int = 0
    private var currentMinute: Int = 0
    private var amPm: String = ""
    private lateinit var timePickerDialog: TimePickerDialog
    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("hh:MM")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetTargetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTimePicker()
        subcribeUi()
        observer()
    }

    private fun subcribeUi() {
        binding.btnTargetSave.setOnClickListener {
            clearFocus()
            checkAndSaveTarget()
        }
    }

    private fun observer() {
        viewModel.target.observe(viewLifecycleOwner){
            setTarget(it)
        }
    }

    private fun setTarget(target: Target) {
        with(binding){
            edtTargetDistance.editText?.setText("${if(target.distance > 0) target.distance else ""}")
            edtTargetCalo.editText?.setText("${if(target.calo > 0) target.calo else ""}")
            edtTargetTimeStart.editText?.setText(timeFormat.format(target.timeStart))
            edtTargetPlace.editText?.setText(target.place)
            edtTargetRecurring.editText?.setText("${if(target.recursive > 0) target.recursive else ""}")
            edtTargetNotificationBefore.editText?.setText("${target.notificationBefore}")
        }
    }

    private fun checkAndSaveTarget(){
        with(binding){
            val distance = edtTargetDistance.editText?.text.toString()
            val calo = edtTargetCalo.editText?.text.toString()
            val recursive = edtTargetRecurring.editText?.text.toString()
            val place = edtTargetPlace.editText?.text.toString()

            if(distance == "" || calo == "" || recursive == ""){
                ToastHelper.makeText(
                    requireContext(),
                    getString(R.string.warning),
                    Toast.LENGTH_LONG
                )
                    .setGravity(Gravity.TOP, 0, 100)
                    .show()

                when {
                    distance == "" -> edtTargetDistance.editText?.requestFocus()
                    calo == "" -> edtTargetCalo.editText?.requestFocus()
                    else -> edtTargetRecurring.editText?.requestFocus()
                }
            }else{
                val target = Target(
                    distance = distance.toInt(),
                    calo = calo.toInt(),
                    recursive = recursive.toInt(),
                    place = place,
                    notificationBefore = edtTargetNotificationBefore.editText?.text.toString().toInt(),
                    timeStart = timeFormat.parse(edtTargetTimeStart.editText?.text.toString())!!.time
                )
                viewModel.saveTarget(target)
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpTimePicker(){
        binding.edtTargetTimeStartInner.setOnClickListener {
            calendar = Calendar.getInstance()
            currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            currentMinute = calendar.get(Calendar.MINUTE)

            timePickerDialog = TimePickerDialog(requireContext(), R.style.MyTimePickerDark,
                { _, hourOfDay, minutes ->
                    amPm = if (hourOfDay >= 12) {
                        "PM"
                    } else {
                        "AM"
                    }
                    binding.edtTargetTimeStartInner.setText("%02d:%02d".format(hourOfDay, minutes) + " " + amPm)
                }, 6, 0, false
            )
            timePickerDialog.show()
        }
    }

    private fun clearFocus(){
        with(binding){
            edtTargetDistance.editText?.clearFocus()
            edtTargetCalo.editText?.clearFocus()
            edtTargetPlace.editText?.clearFocus()
            edtTargetRecurring.editText?.clearFocus()
            edtTargetNotificationBefore.editText?.clearFocus()
        }
    }



}