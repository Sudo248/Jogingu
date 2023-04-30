package com.sudo248.jogingu.ui.activities.main.fragments.profile

import android.annotation.SuppressLint
import android.app.Dialog
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
import com.sudo248.data.util.calculateAge
import com.sudo248.data.util.genId
import com.sudo248.domain.common.UiState
import com.sudo248.domain.entities.Gender
import com.sudo248.domain.entities.User
import com.sudo248.jogingu.ui.activities.main.MainActivity
import com.sudo248.jogingu.R
import com.sudo248.jogingu.databinding.FragmentProfileBinding
import com.sudo248.jogingu.helper.ToastHelper
import com.sudo248.jogingu.util.DialogUtils
import com.sudo248.jogingu.util.loadImageFromUrl
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
    private var isSave = false
    private val loadingDialog: Dialog by lazy {
        DialogUtils.loadingDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity is MainActivity){
            (activity as MainActivity).supportActionBar?.hide()
        }
        setUpUi()
        observer()
    }

    private fun observer() {
        viewModel.isUpdate.observe(viewLifecycleOwner){ isUpdate ->
            if(isUpdate) {
                binding.btnProfileSave.text = getString(R.string.update)
            }
        }

        viewModel.user.observe(viewLifecycleOwner){
            setUser(it)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                UiState.LOADING -> {
                    loadingDialog.show()
                }
                UiState.SUCCESS -> {
                    loadingDialog.hide()
                    if (isSave) parentFragmentManager.popBackStack()
                }
                else -> {
                    loadingDialog.hide()
                }
            }
        }
    }

    private fun setUpUi(){
        createGenderAdapter()
        binding.profileEdtBirthDay.editText?.setOnClickListener {
            createDatePicker()
        }
        binding.btnProfileSave.setOnClickListener {
            getUser()?.let { user ->
                viewModel.onSaveClick(user)
                ToastHelper.makeText(
                    requireContext(),
                    getString(R.string.save_success),
                    Toast.LENGTH_SHORT
                )
                    .setGravity(Gravity.TOP, 0, 100)
                    .show()
            }
        }
    }

    private fun setUser(user: User){
        this.userId = user.userId
        with(binding){

            profileEdtFirstName.editText?.setText(user.firstName)
            profileEdtLastName.editText?.setText(user.lastName)
            profileEdtCity.editText?.setText(user.city)
            profileEdtCountry.editText?.setText(user.country)
            profileEdtPrimarySport.editText?.setText(user.primarySport)
            profileEdtBirthDay.editText?.setText(dateFormat.format(user.birthday))
            profileEdtHeight.editText?.setText("${user.height}")
            profileEdtWeight.editText?.setText("${user.weight}")
            profileEdtGender.editText?.setText(genders[user.gender.ordinal])

            if (user.imageUrl != null) {
                profileImageExpanded.loadImageFromUrl(user.imageUrl)
            } else if(user.imageInByteArray != null){
                Glide.with(this.root).load(user.imageInByteArray).into(profileImageExpanded)
            } else {
                profileImageExpanded.setImageResource(R.drawable.img_user)
            }
        }
    }

    private fun getUser(): User? {
        try {
            with(binding){
                return User(
                    userId = userId ?: genId("user"),
                    firstName = profileEdtFirstName.editText?.text.toString(),
                    lastName = profileEdtLastName.editText?.text.toString(),
                    city =  profileEdtCity.editText?.text.toString(),
                    country = profileEdtCountry.editText?.text.toString(),
                    primarySport = profileEdtPrimarySport.editText?.text.toString(),
                    gender = Gender.valueOf(profileEdtGender.editText?.text.toString().uppercase()),
                    birthday = dateFormat.parse(profileEdtBirthDay.editText?.text.toString())!!,
                    height = profileEdtHeight.editText?.text.toString().toInt(),
                    weight = profileEdtWeight.editText?.text.toString().toInt(),
                    imageInByteArray = viewModel.user.value?.imageInByteArray,
                    imageUrl = viewModel.user.value?.imageUrl
                )
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Some field required", Toast.LENGTH_SHORT).show()
            return null
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

    private fun validate() {

    }

    private fun createGenderAdapter(gender: String? = null){
        val adapter = ArrayAdapter(requireContext(), R.layout.item_gender, genders)
        val autoCompleteTextView = binding.profileEdtGender.editText as? AutoCompleteTextView
        autoCompleteTextView?.setText(gender)
        autoCompleteTextView?.setAdapter(adapter)
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