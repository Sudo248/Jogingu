package com.sudo.jogingu.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.datepicker.MaterialDatePicker
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.FragmentProfileBinding
import java.util.Date

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()

    }

    private fun setUpUi(){
        createGenderAdapter()
        binding.profileEdtBirthDay.editText?.setOnClickListener {
            createDatePicker()
        }


    }

    private fun createGenderAdapter(gender: String? = null){
        val genders = listOf(getString(R.string.female), getString(R.string.male), getString(R.string.other))
        val adapter = ArrayAdapter(requireContext(), R.layout.item_gender, genders)
        val autoCompleteTextView = binding.profileEdtGender.editText as? AutoCompleteTextView
        autoCompleteTextView?.setAdapter(adapter)
        autoCompleteTextView?.setText(gender)
    }

    private fun createDatePicker(now: Date? = null){
        val selectDate = now?.time ?: MaterialDatePicker.todayInUtcMilliseconds()
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Your Birthday")
                .setSelection(selectDate)
                .build()

        activity?.supportFragmentManager?.let { datePicker.show(it, "Show date picker") }

        datePicker.addOnPositiveButtonClickListener {
            binding.profileEdtBirthDay.editText?.setText(datePicker.headerText)
        }

    }


}