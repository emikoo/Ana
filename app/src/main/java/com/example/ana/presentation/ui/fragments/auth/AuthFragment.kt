package com.example.ana.presentation.ui.fragments.auth

import android.annotation.SuppressLint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.preference.PreferenceManager
import com.example.ana.R
import com.example.ana.data.local.PrefsSettings
import com.example.ana.data.model.cardImagesList
import com.example.ana.data.model.cardList
import com.example.ana.databinding.FragmentAuthBinding
import com.example.ana.presentation.extensions.activityNavController
import com.example.ana.presentation.extensions.navigateSafely
import com.example.ana.presentation.extensions.updateLanguage
import com.example.ana.presentation.ui.adapters.CustomSpinnerAdapter
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.teenteen.teencash.presentation.base.BaseFragment
import java.util.concurrent.TimeUnit


class AuthFragment : BaseFragment<FragmentAuthBinding>() {
    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var state = Screen.PHONE_NUMBER
    var phoneNumber = ""
    var userName = ""
    private var isLangChanged = false

    enum class Screen { PHONE_NUMBER, CODE, NAME }

    override fun attachBinding(
        list: MutableList<FragmentAuthBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentAuthBinding.inflate(layoutInflater, container, attachToRoot))
    }

    @SuppressLint("ResourceType")
    override fun setupViews() {
        prefs.getSettingsLanguage()
        setStrings()
        setPhoneView()
        setupSpinner()
        setupButton()
    }

    private fun setStrings() {
        binding.title.setText(R.string.authorization)
        binding.subtitle.setText(R.string.by_phone_number)
        binding.textField.setHint(R.string.phone_number)
        binding.signin.setText(R.string.sign_in)
        binding.resend.setText(R.string.didn_t_get_the_code)
        binding.policy.setText(R.string.policy)
        if (binding.error.text == getString(R.string.error_phone_number1)) {
            binding.error.setText(R.string.error_phone_number1)
        } else binding.error.text = getString(R.string.please_write_in_format)
    }

    private fun setupButton() {
        binding.signin.setOnClickListener {
            progressDialog.show()
            when (state) {
                Screen.PHONE_NUMBER -> {
                    phoneNumber = binding.input.text.toString()
                    if (phoneNumber.isBlank()) {
                        binding.error.setTextColor(resources.getColor(R.color.red))
                        binding.error.setText(R.string.error_phone_number1)
                        progressDialog.dismiss()
                    } else {
                        startPhoneNumberVerification(phoneNumber)
                        binding.input.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {}
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                binding.error.setTextColor(resources.getColor(R.color.grey))
                                binding.error.text = getString(R.string.please_write_in_format)
                            }
                        })
                    }
                }

                Screen.CODE -> {
                    val code = binding.smsCodeView.enteredCode
                    if (binding.smsCodeView.enteredCode.length == 6) {
                        verifyVerificationCode(code)
                    } else {
                        binding.subtitle.text = getString(R.string.wrong_code_please_try_again)
                        progressDialog.dismiss()
                    }
                }

                Screen.NAME -> {
                    userName = binding.input.text.toString()
                    prefs.setFirstTimeLaunch(PrefsSettings.USER)
                    prefs.saveCurrentUserId("$phoneNumber")
                    addUserToFirestore()
                    activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                }
            }
        }
        binding.btnBack.setOnClickListener {
            when (state) {
                Screen.CODE -> {
                    setPhoneView()
                }

                Screen.PHONE_NUMBER -> TODO()
                Screen.NAME -> {
                    setCodeView()
                }
            }
        }
    }

    private fun setupSpinner() {
        val languages = resources.getStringArray(R.array.languages).toList()
        val spinner = binding.languageSpinner
        val adapter = CustomSpinnerAdapter(requireContext(), languages)
        spinner.adapter = adapter
        setSpinnerPosition()
        binding.spinnerButton.setOnClickListener {
            spinner.performClick()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("RestrictedApi")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        PreferenceManager(requireContext()).updateLanguage(
                            "eng",
                            requireContext(),
                            prefs
                        )
                    }

                    1 -> {
                        PreferenceManager(requireContext()).updateLanguage(
                            "kk",
                            requireContext(),
                            prefs
                        )
                    }

                    2 -> {
                        PreferenceManager(requireContext()).updateLanguage(
                            "ru",
                            requireContext(),
                            prefs
                        )
                    }
                }
                setStrings()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setSpinnerPosition() {
        val currentLanguage = prefs.getSettingsLanguage()
        val spinner = binding.languageSpinner

        val languageCodes = arrayOf("eng", "kk", "ru")

        val position = languageCodes.indexOf(currentLanguage)
        if (position >= 0) {
            spinner.setSelection(position)
        } else {
            spinner.setSelection(0)
        }
    }

    private fun setPhoneView() {
        state = Screen.PHONE_NUMBER
        binding.btnBack.visibility = View.INVISIBLE
        binding.smsCodeView.visibility = View.GONE
        binding.textField.visibility = View.VISIBLE
        binding.title.text = getString(R.string.authorization)
        binding.subtitle.text = getString(R.string.by_phone_number)
        binding.textField.hint = getString(R.string.phone_number)
        binding.input.inputType = InputType.TYPE_CLASS_PHONE
        binding.error.setTextColor(resources.getColor(R.color.grey))
        binding.error.text = getString(R.string.please_write_in_format)
        binding.error.visibility = View.VISIBLE
        binding.resend.visibility = View.GONE
    }

    private fun setCodeView() {
        state = Screen.CODE
        binding.textField.visibility = View.GONE
        binding.smsCodeView.visibility = View.VISIBLE
        binding.btnBack.visibility = View.VISIBLE
        binding.title.text = getString(R.string.type_sms_code)
        binding.subtitle.text =
            getString(R.string.a_confirmation_code_was_sent_to_the_number) + binding.input.text.toString()
        binding.resend.visibility = View.VISIBLE
        binding.error.visibility = View.GONE
        progressDialog.dismiss()
    }

    private fun setNameView() {
        state = Screen.NAME
        binding.input.setText("")
        binding.smsCodeView.visibility = View.GONE
        binding.textField.visibility = View.VISIBLE
        binding.title.text = getString(R.string.authorization)
        binding.subtitle.text = getString(R.string.what_s_your_name)
        binding.textField.hint = getString(R.string.your_name)
        binding.input.inputType = InputType.TYPE_CLASS_TEXT
        binding.error.setTextColor(resources.getColor(R.color.grey))
        binding.resend.visibility = View.GONE
        progressDialog.dismiss()
    }

    private fun verifyVerificationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w("AuthFragment", "onVerificationFailed", e)
            progressDialog.dismiss()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            resendToken = token
            setCodeView()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    checkDocumentExists()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        binding.subtitle.text = getString(R.string.wrong_code_please_try_again)
                        progressDialog.dismiss()
                    }
                }
            }
    }

    private fun checkDocumentExists() {
        val docRef = db.collection("users").document(phoneNumber)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    prefs.setFirstTimeLaunch(PrefsSettings.USER)
                    prefs.saveCurrentUserId(phoneNumber)
                    progressDialog.dismiss()
                    activityNavController().navigateSafely(R.id.action_global_mainFlowFragment)
                } else {
                    setNameView()
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    private fun addUserToFirestore() {
        usersCollection.document(phoneNumber)
            .set(mapOf("phone" to phoneNumber, "name" to userName, "photo" to ""))
        for (card in cardList) {
            for (image in cardImagesList) {
                usersCollection.document(phoneNumber).collection("wish_card")
                    .document(card.name.toString())
                    .collection("images").document(image.name.toString()).set(image)
            }
        }
    }

    override fun subscribeToLiveData() {}

    companion object {
        private const val TAG = "AuthFragment"
    }
}