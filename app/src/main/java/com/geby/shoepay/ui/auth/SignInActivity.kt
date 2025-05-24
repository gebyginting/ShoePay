package com.geby.shoepay.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.geby.shoepay.databinding.ActivitySignInBinding
import com.geby.shoepay.ui.shoes.ShoesListActivity
import com.geby.shoepay.utilities.ResultState
import com.geby.shoepay.utilities.UserPreference
import com.geby.shoepay.viewmodel.AuthViewModel
import com.geby.shoepay.viewmodel.ViewModelFactory

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val authViewModel: AuthViewModel by viewModels{ factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        signIn()
        signupButton()
    }

    private fun signIn() {
        with(binding) {
            signinButton.setOnClickListener {
                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString().trim()

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.error = "Format email tidak valid"
                    return@setOnClickListener
                }
                authViewModel.login(email, password)
            }
        }
        authViewModel.loginResult.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    Toast.makeText(this, "Sedang Loading", Toast.LENGTH_SHORT).show()
                }
                is ResultState.Success -> {
                    saveUser()
                }
                is ResultState.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun signupButton() {
        binding.goToSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveUser() {
        authViewModel.fetchAndSaveUserData(UserPreference(this)) {
            // ini dijalankan setelah data berhasil disimpan
            val intent = Intent(this@SignInActivity, ShoesListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }



//    private fun checkUser() {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//
//        if (currentUser == null) {
//            startActivity(Intent(this, SignInActivity::class.java))
//            finish()
//        } else {
//            val user = User(
//                uid = currentUser.uid,
//                name = currentUser.displayName ?: "Unnamed",
//                email = currentUser.email ?: ""
//            )
//
//            userViewModel.saveUser(user) { success ->
//                if (success) {
//                    // Lanjutkan ke halaman utama
//                    startActivity(Intent(this, PaymentActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this, "Gagal menyimpan data user", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
}