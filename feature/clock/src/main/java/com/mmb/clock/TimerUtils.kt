package com.mmb.clock

import com.mmb.clock.entity.TimerState

fun Long.convertToTimerState(): TimerState {
    val totalSeconds = this / TimerState.SECOND_MILLS // Milisegundos a segundos
    val min = totalSeconds / TimerState.SECONDS_PER_MINUTE
    val remainingSeconds = totalSeconds % TimerState.SECONDS_PER_MINUTE

    return TimerState(
        minutes = min,
        seconds = remainingSeconds
    )
}