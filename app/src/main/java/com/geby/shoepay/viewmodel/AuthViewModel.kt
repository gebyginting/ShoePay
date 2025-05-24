package com.geby.shoepay.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geby.shoepay.data.repository.AuthRepository
import com.geby.shoepay.utilities.ResultState
import com.geby.shoepay.utilities.UserPreference
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _signUpResult = MutableLiveData<ResultState<String>>()
    val signUpResult: LiveData<ResultState<String>> = _signUpResult

    private val _loginResult = MutableLiveData<ResultState<String>>()
    val loginResult: LiveData<ResultState<String>> = _loginResult

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpResult.value = ResultState.Loading
            val result = authRepository.signUpUser(name, email, password)
            _signUpResult.value = result
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = ResultState.Loading
            val result = authRepository.loginUser(email, password)
            _loginResult.value = result
        }
    }

    fun fetchAndSaveUserData(userPreference: UserPreference, onComplete: () -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                when (val result = authRepository.getUserData(currentUser.uid)) {
                    is ResultState.Success -> {
                        val name = result.data["name"] as? String ?: ""
                        val email = result.data["email"] as? String ?: ""
                        userPreference.saveUser(currentUser.uid, name, email)
                        onComplete() // panggil setelah selesai
                    }
                    is ResultState.Error -> {
                        // bisa logging error di sini
                        onComplete()
                    }
                    else -> Unit
                }
            }
        } else {
            onComplete() // jika currentUser null
        }
    }
}
