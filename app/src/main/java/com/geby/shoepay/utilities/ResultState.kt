package com.geby.shoepay.utilities

sealed class ResultState<out R> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: String) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}