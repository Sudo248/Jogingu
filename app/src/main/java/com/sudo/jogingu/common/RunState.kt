package com.sudo.jogingu.common

enum class RunState {
    START{
        override fun onChange(): RunState = RUNNING
    },
    RUNNING{
        override fun onChange(): RunState = PAUSE
    },
    PAUSE{
        override fun onChange(): RunState = RUNNING

    },
    FINISH{
        override fun onChange(): RunState = START
    };

    abstract fun onChange(): RunState

}