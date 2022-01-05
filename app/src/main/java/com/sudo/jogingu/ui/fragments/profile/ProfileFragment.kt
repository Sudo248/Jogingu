package com.sudo.jogingu.ui.fragments.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log.d
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.sudo.data.util.calculateAge
import com.sudo.data.util.genId
import com.sudo.domain.entities.Gender
import com.sudo.domain.entities.User
import com.sudo.jogingu.ui.activities.main.MainActivity
import com.sudo.jogingu.R
import com.sudo.jogingu.databinding.FragmentProfileBinding
import com.sudo.jogingu.helper.ToastHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    private val genders by lazy { listOf(getString(R.string.female), getString(R.string.male), getString(R.string.other)) }
    private var userId: String? = null

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
        Timber.d("type of activity: ${activity!!::class.java.name} ${activity is MainActivity}")
        if(activity is MainActivity){
            (activity as MainActivity).supportActionBar?.hide()
        }
        setUpUi()
        observer()
    }

    private fun observer() {
        viewModel.saveOrUpdate.observe(viewLifecycleOwner){
            binding.btnProfileSave.text = it
        }

        viewModel.user.observe(viewLifecycleOwner){
            setUser(it)
        }
    }

    private fun setUpUi(){
        createGenderAdapter()
        binding.profileEdtBirthDay.editText?.setOnClickListener {
            createDatePicker()
        }
        binding.btnProfileSave.setOnClickListener {
            viewModel.onSaveClick(getUser())
            ToastHelper.makeText(
                requireContext(),
                getString(R.string.save_success),
                Toast.LENGTH_SHORT
            )
                .setGravity(Gravity.TOP, 0, 100)
                .show()
        }
    }

    private fun setUser(user: User){
        this.userId = user.userId
        with(binding){
            profileCollapsingToolbar.title = "${user.firstName} ${user.lastName}"
            profileEdtCity.editText?.setText(user.city)
            profileEdtCountry.editText?.setText(user.country)
            profileEdtPrimarySport.editText?.setText(user.primarySport)
            profileEdtBirthDay.editText?.setText(dateFormat.format(user.birthday))
            profileEdtHeight.editText?.setText("${user.height}")
            profileEdtWeight.editText?.setText("${user.height}")
            profileEdtGender.editText?.setText(genders[user.gender.ordinal])

            if(user.imageInByteArray != null){
                Glide.with(this.root).load(user.imageInByteArray).into(profileImageExpanded)
            }
        }
    }

    private fun getUser(): User{
        with(binding){
            return User(
                userId = userId ?: genId("user"),
                firstName = "Duong",
                lastName = "Le",
                city =  profileEdtCity.editText?.text.toString(),
                country = profileEdtCountry.editText?.text.toString(),
                primarySport = profileEdtPrimarySport.editText?.text.toString(),
                gender = Gender.valueOf(profileEdtGender.editText?.text.toString().uppercase()),
                birthday = dateFormat.parse(profileEdtBirthDay.editText?.text.toString())!!,
                height = profileEdtHeight.editText?.text.toString().toShort(),
                weight = profileEdtWeight.editText?.text.toString().toShort(),
                imageInByteArray = null
            )
        }
    }

    private fun checkRequiredInformation(): Boolean{
        var res = false
        if(binding.profileEdtHeight.editText?.text?.equals("") == true){
            res = true
            binding.profileEdtHeight.error = getString(R.string.required)
        }

        if(binding.profileEdtWeight.editText?.text?.equals("") == true){
            res = true
            binding.profileEdtHeight.error = getString(R.string.required)
        }

        if(binding.profileEdtBirthDay.editText?.text?.equals("") == true){
            res = true
        }

        return res
    }

    private fun createGenderAdapter(gender: String? = null){
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
            binding.profileEdtBirthDay.editText?.setText(dateFormat.format(datePicker.selection))
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).supportActionBar?.show()
    }

}