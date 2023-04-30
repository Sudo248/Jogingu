package com.sudo248.domain.common

sealed class DataState<out R>{
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
    object Loading : DataState<Nothing>()

    override fun toString(): String {
        return when(this){
            is Success -> "Success(${this.data})"
            is Error -> "Error: ${this.message}"
            else -> "Loading"
        }
    }
    
}

//fun DataState<*>.isSuccess() = this is DataState.Success && this.data != null
