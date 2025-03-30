package com.mmb.clock.entity

data class TimerState(
    val minutes: Long = 0,
    val seconds: Long = 0
) {

    companion object {
        const val SECOND_MILLS = 1000L // 1 segundo en milisegundos
        const val SECONDS_PER_MINUTE = 60L // 60 segundos por minuto
    }

    fun convertToSeconds(): Long {
        return (minutes * SECONDS_PER_MINUTE + seconds) * SECOND_MILLS
    }

    override fun toString(): String {
        return "%02d".format(minutes) + ":" + "%02d".format(seconds)
    }
}