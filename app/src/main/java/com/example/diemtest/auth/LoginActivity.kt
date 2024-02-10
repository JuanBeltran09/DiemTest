package com.example.diemtest.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.example.diemtest.MainActivity
import com.example.diemtest.R
import com.example.diemtest.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    val auth = FirebaseAuth.getInstance()
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendOtp.setOnClickListener{
            if(binding.userNumber.text!!.isEmpty()){
                binding.userNumber.error = "Please enter your number"
            }else{
                sendOtp(binding.userNumber.text.toString())
            }
        }

        binding.verifyOtp.setOnClickListener{
            if(binding.userOtp.text!!.isEmpty()){
                binding.userOtp.error = "Please enter your OTP"
            }else{
                verifyOtp(binding.userOtp.text.toString())
            }
        }
    }

    private fun verifyOtp(otp: String) {
        //binding.sendOtp.showLoadingButton()

        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)

        signInWithPhoneAuthCredential(credential)
    }

    private fun sendOtp(number: String) {

        //binding.sendOtp.showLoadingButton()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                //binding.sendOtp.showNormalButton()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LoginActivity.verificationId = verificationId

                //binding.sendOtp.showNormalButton()

                binding.numberLayout.visibility = GONE
                binding.otpLayout.visibility = VISIBLE
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+57$number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                //binding.sendOtp.showNormalButton()
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()

                }
            }
    }
}