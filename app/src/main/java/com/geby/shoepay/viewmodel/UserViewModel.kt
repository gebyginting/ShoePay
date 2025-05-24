package com.geby.shoepay.viewmodel

import com.geby.shoepay.utilities.ResultState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geby.shoepay.data.models.PaymentHistory
import com.geby.shoepay.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel (private val userRepository: UserRepository) : ViewModel() {
    private val _userHistoriesState = MutableStateFlow<ResultState<List<PaymentHistory>>>(ResultState.Loading)
    val userHistoriesState: StateFlow<ResultState<List<PaymentHistory>>> = _userHistoriesState

    fun fetchUserHistories(uid: String) {
        viewModelScope.launch {
            _userHistoriesState.value = ResultState.Loading
            when (val result = userRepository.getUserHistories(uid)) {
                is ResultState.Success -> _userHistoriesState.value = ResultState.Success(result.data)
                is ResultState.Error -> _userHistoriesState.value = ResultState.Error(result.error)
                else -> {}
            }
        }
    }
}