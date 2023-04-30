package com.sudo248.jogingu.ui.activities.main.fragments.target.set_target

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sudo248.domain.entities.Target
import com.sudo248.jogingu.R
import com.sudo248.jogingu.common.Constant
import com.sudo248.jogingu.databinding.FragmentSetTargetBinding
import com.sudo248.jogingu.helper.ToastHelper
import com.sudo248.jogingu.receiver.AlarmReceiver
import com.sudo248.jogingu.ui.activities.main.fragments.target.TargetViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SetTargetFragment : Fragment() {
    private lateinit var binding: FragmentSetTargetBinding

    private val viewModel: TargetViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private lateinit var timePickerDialog: TimePickerDialog

    @SuppressLint("SimpleDateFormat")
    private val timeFormat = SimpleDateFormat("HH:mm")

    private val calendar = Calendar.getInstance()

    init {
        calendar.timeInMillis = System.currentTimeMillis()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        viewModel.target.observe(viewLifecycleOwner) {
            setTarget(it)
        }
    }

    private fun setTarget(target: Target) {
        calendar.timeInMillis = target.timeStart
        Log.d("Sudoo", "setTarget: time: ${calendar.timeInMillis}")
        with(binding) {
            edtTargetDistance.editText?.setText("${if (target.distance > 0) target.distance else ""}")
            edtTargetCalo.editText?.setText("${if (target.calo > 0) target.calo else ""}")
            edtTargetTimeStart.editText?.setText(timeFormat.format(calendar.timeInMillis))
            edtTargetPlace.editText?.setText(target.place)
            edtTargetRecurring.editText?.setText("${if (target.recursive > 0) target.recursive else ""}")
            edtTargetNotificationBefore.editText?.setText("${target.notificationBefore}")
        }
    }

    private fun checkAndSaveTarget() {
        with(binding) {
            val distance = edtTargetDistance.editText?.text.toString()
            val calo = edtTargetCalo.editText?.text.toString()
            val recursive = edtTargetRecurring.editText?.text.toString()
            val place = edtTargetPlace.editText?.text.toString()

            if (distance == "" || calo == "" || recursive == "") {
                ToastHelper.makeText(
                    requireContext(), getString(R.string.warning), Toast.LENGTH_SHORT
                ).show()

                when {
                    distance == "" -> edtTargetDistance.editText?.requestFocus()
                    calo == "" -> edtTargetCalo.editText?.requestFocus()
                    else -> edtTargetRecurring.editText?.requestFocus()
                }
            } else {
                val target = Target(
                    distance = distance.toInt(),
                    calo = calo.toInt(),
                    recursive = recursive.toInt(),
                    place = place,
                    notificationBefore = edtTargetNotificationBefore.editText?.text.toString()
                        .toInt(),
                    timeStart = calendar.timeInMillis
                )
                Log.d("Sudoo", "checkAndSaveTarget: ${calendar.timeInMillis}")
                viewModel.saveTarget(target)
                scheduleRunNotification(
                    time = calendar.timeInMillis,
                    place = place,
                    distance = distance,
                    calo = calo,
                    timeStart = timeFormat.format(calendar.timeInMillis)
                )
                ToastHelper.makeText(
                    requireContext(), getString(R.string.save_success), Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpTimePicker() {
        binding.edtTargetTimeStartInner.setOnClickListener {

            timePickerDialog = TimePickerDialog(
                requireContext(), R.style.MyTimePickerDark, { _, hourOfDay, minutes ->
                    binding.edtTargetTimeStartInner.setText(
                        "%02d:%02d".format(
                            hourOfDay, minutes
                        )
                    )
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minutes)
                    Log.d("Sudoo", "setUpTimePicker: ${calendar.timeInMillis}")
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }
    }

    private fun clearFocus() {
        with(binding) {
            edtTargetDistance.editText?.clearFocus()
            edtTargetCalo.editText?.clearFocus()
            edtTargetPlace.editText?.clearFocus()
            edtTargetRecurring.editText?.clearFocus()
            edtTargetNotificationBefore.editText?.clearFocus()
        }
    }

    private fun scheduleRunNotification(
        time: Long, place: String, distance: String, calo: String, timeStart: String
    ) {
        Timber.d("scheduleRunNotification: $time")
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            action = Constant.ACTION_NOTIFICATION_RUN
            putExtra(Constant.KEY_TITLE_NOTIFICATION, "Target today")
            putExtra(
                Constant.KEY_CONTENT_NOTIFICATION,
                "Place: $place\nDistance: $distance\nCalo: $calo\nTime start: $timeStart"
            )
        }
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            Constant.NOTIFICATION_RUN_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

//    private fun scheduleRunNotification(time: Long) {
//        val intent = Intent(requireContext(), NotificationReceiver::class.java).apply {
//            action = Constant.ACTION_NOTIFICATION_RUN
//            putExtra(Constant.KEY_TITLE_NOTIFICATION, "Prepare running")
//            putExtra(Constant.KEY_CONTENT_NOTIFICATION, "You have run now")
//        }
//
//        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val pendingIntent = PendingIntent.getBroadcast(
//            requireContext(),
//            Constant.NOTIFICATION_RUN_ID,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            time,
//            AlarmManager.INTERVAL_DAY,
//            pendingIntent
//        )
//
//    }
}