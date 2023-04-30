package com.sudo248.domain.common

enum class UiState {
    /**
     * call [changeState] to change state to [SUCCESS]
     */
    IDLE {
        override fun changeState(newState: UiState?): UiState = newState ?: SUCCESS
    },

    /**
     * call [changeState] to change state to [LOADING]
     */
    SUCCESS {
        override fun changeState(newState: UiState?): UiState = newState ?: LOADING
    },
    /**
     * call [changeState] to change state to [SUCCESS]
     */
    LOADING {
        override fun changeState(newState: UiState?): UiState = newState ?: SUCCESS
    },
    /**
     * call [changeState] to change state to [SUCCESS]
     */
    ERROR {
        override fun changeState(newState: UiState?): UiState = newState ?: SUCCESS
    };

    abstract fun changeState(newState: UiState? = null): UiState

    fun onState(
        idle: () -> Unit = {},
        loading: () -> Unit = {},
        success: () -> Unit = {},
        error: () -> Unit = {}
    ) {
        when (this) {
            SUCCESS -> success()
            LOADING -> loading()
            ERROR -> error()
            else -> idle()
        }
    }
}