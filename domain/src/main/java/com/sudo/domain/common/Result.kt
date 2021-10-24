package com.sudo.domain.common

sealed class Result<out R>{
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when(this){
            is Success -> "Success(${this.data})"
            is Error -> "Error: ${this.message}"
            else -> "Loading"
        }
    }
    
}

//fun Result<*>.isSuccess() = this is Result.Success && this.data != null
