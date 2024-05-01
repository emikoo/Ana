package com.example.ana.presentation.ui.fragments.auth

import android.annotation.SuppressLint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ana.R
import com.example.ana.data.local.PrefsSettings
import com.example.ana.databinding.FragmentAuthBinding
import com.example.ana.presentation.extensions.activityNavController
import com.example.ana.presentation.extensions.navigateSafely
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.teenteen.teencash.presentation.base.BaseFragment
import java.util.concurrent.TimeUnit


class AuthFragment : BaseFragment<FragmentAuthBinding>() {
    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var prefsSettings: PrefsSettings
    private var state = Screen.PHONE_NUMBER

    enum class Screen {
        PHONE_NUMBER, CODE, NAME
    }

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
        prefsSettings = PrefsSettings(requireActivity().applicationContext)
        setPhoneView()
        binding.signin.setOnClickListener {
            when (state) {
                Screen.PHONE_NUMBER -> {
                    val phoneNumber = binding.input.text.toString()
                    startPhoneNumberVerification(phoneNumber)
                    if (phoneNumber.isBlank()) {
                        binding.error.setTextColor(resources.getColor(R.color.red))
                        binding.error.setText(R.string.error_phone_number1)
                    }
                    binding.input.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {}
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            binding.error.setTextColor(resources.getColor(R.color.grey))
                            binding.error.text = "Пожалуйста пишите в формате +7##########"
                        }
                    })
                }

                Screen.CODE -> {
                    val code = binding.smsCodeView.enteredCode
                    if (binding.smsCodeView.enteredCode.length == 6) {
                        verifyVerificationCode(code)
                    } else {
                        binding.subtitle.text = "Wrong code. Please try again"
                    }
                }

                Screen.NAME -> {
                    prefsSettings.saveName(binding.input.text.toString())
                    prefs.setFirstTimeLaunch(PrefsSettings.USER)
                    prefsSettings.saveCurrentUserId(auth.uid)
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

    private fun setCodeView() {
        state = Screen.CODE
        binding.textField.visibility = View.GONE
        binding.smsCodeView.visibility = View.VISIBLE
        binding.title.text = getString(R.string.type_sms_code)
        binding.subtitle.text =
            "A confirmation code was sent to the number " + binding.input.text.toString()
        binding.resend.visibility = View.VISIBLE
        binding.error.visibility = View.GONE
    }

    private fun setPhoneView() {
        state = Screen.PHONE_NUMBER
        binding.smsCodeView.visibility = View.GONE
        binding.textField.visibility = View.VISIBLE
        binding.title.text = getString(R.string.authorization)
        binding.subtitle.text = getString(R.string.by_phone_number)
        binding.textField.hint = "Phone number"
        binding.input.inputType = InputType.TYPE_CLASS_PHONE
        binding.error.setTextColor(resources.getColor(R.color.grey))
        binding.error.text = "Пожалуйста пишите в формате +7##########"
        binding.error.visibility = View.VISIBLE
        binding.resend.visibility = View.GONE
    }

    private fun setNameView() {
        state = Screen.NAME
        binding.input.setText("")
        binding.smsCodeView.visibility = View.GONE
        binding.textField.visibility = View.VISIBLE
        binding.title.text = getString(R.string.authorization)
        binding.subtitle.text = getString(R.string.what_s_your_name)
        binding.textField.hint = "Your name"
        binding.input.inputType = InputType.TYPE_CLASS_TEXT
        binding.error.setTextColor(resources.getColor(R.color.grey))
        binding.resend.visibility = View.GONE
    }

    override fun subscribeToLiveData() {}

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)         // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)   // Timeout duration
            .setActivity(requireActivity())      // Activity (for callback binding)
            .setCallbacks(callbacks)             // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            addUserToFirestore(FirebaseAuth.getInstance().currentUser)
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w("AuthFragment", "onVerificationFailed", e)
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

    private fun verifyVerificationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    fun addUserToFirestore() {
        val phone = binding.input.text.toString()
        usersCollection.document().set(mapOf("phone" to phone))
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d("AuthFragment", "signInWithCredential:success")
                    val user = task.result?.user
                    prefsSettings.savePhoneNumber(binding.input.text.toString())
                    addUserToFirestore()
                    setNameView()
                } else {
                    // Sign in failed, handle the error
                    Log.w("AuthFragment", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }

                }
            }
    }
}