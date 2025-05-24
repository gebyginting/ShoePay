package com.geby.shoepay.ui.auth

import com.geby.shoepay.utilities.ResultState
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.geby.shoepay.databinding.ActivitySignUpBinding
import com.geby.shoepay.viewmodel.AuthViewModel
import com.geby.shoepay.viewmodel.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val authViewModel: AuthViewModel by viewModels{ factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUp()
        goToSignIn()
    }

    private fun signUp() {
        with(binding) {
            signupButton.setOnClickListener {
                val username = inputUsername.text.toString().trim()
                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString().trim()

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.error = "Format email tidak valid"
                    return@setOnClickListener
                }

                authViewModel.signUp(username, email, password)
            }
        }
        authViewModel.signUpResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    Toast.makeText(this, "Sedang Loading", Toast.LENGTH_SHORT).show()
                }
                is ResultState.Success -> {
                    Toast.makeText(this, "Hasil: ${result.data}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)                }
                is ResultState.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }


    private fun goToSignIn() {
        binding.goToSignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}