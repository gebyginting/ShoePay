package com.geby.shoepay.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.geby.shoepay.databinding.ActivityProfileBinding
import com.geby.shoepay.ui.auth.SignInActivity
import com.geby.shoepay.ui.payment.PaymentHistoryActivity
import com.geby.shoepay.utilities.UserPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
        userPreference = UserPreference(this)

        setupProfile()
        setupButtons()
    }

    private fun setupProfile() {
        binding.txtName.text = userPreference.getName()
        binding.txtEmail.text = userPreference.getEmail()
    }

    private fun setupButtons() {
        binding.btnPaymentHistory.setOnClickListener {
            startActivity(Intent(this, PaymentHistoryActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Logout") { _, _ ->
                // Tampilkan loading
                binding.includeLoadingLayout.progressBar.visibility = View.VISIBLE
                // Simulasi delay logout
                binding.root.postDelayed({
                    FirebaseAuth.getInstance().signOut()
                    userPreference.clear()

                    // Sembunyikan loading
                    binding.includeLoadingLayout.btnRetry.visibility = View.GONE

                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }, 1500)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}