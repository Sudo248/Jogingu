package com.sudo.jogingu.ui.fragments.target.set_target

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.FragmentSetTargetBinding
import java.util.*

import kotlin.text.*


class SetTargetFragment : Fragment() {
    lateinit var binding: FragmentSetTargetBinding
    lateinit var calendar: Calendar
    var currentHour: Int = 0
    var currentMinute: Int = 0
    var amPm: String = ""
    lateinit var timePickerDialog: TimePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetTargetBinding.inflate(inflater, container, false)

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

        return binding.root
    }

}