package com.geby.shoepay.di

import com.geby.shoepay.data.repository.AuthRepository
import com.geby.shoepay.data.repository.PaymentRepository
import com.geby.shoepay.data.repository.UserRepository

object Injection {
    fun provideAuthRepository(): AuthRepository {
        return AuthRepository()
    }

    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }

    fun providePaymentRepository(): PaymentRepository {
        return PaymentRepository()
    }

}