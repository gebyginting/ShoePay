package com.geby.shoepay.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.geby.shoepay.data.repository.AuthRepository
import com.geby.shoepay.data.repository.PaymentRepository
import com.geby.shoepay.data.repository.UserRepository
import com.geby.shoepay.di.Injection

class ViewModelFactory (
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val paymentRepository: PaymentRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository) as T
            }
             modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(PaymentViewModel::class.java) -> {
                PaymentViewModel(paymentRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        // Menyediakan instance factory dengan repository
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideAuthRepository(), Injection.provideUserRepository(), Injection.providePaymentRepository())
            }.also { instance = it }
    }
}