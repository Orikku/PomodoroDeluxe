package com.mmb.session

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.sharp.Computer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmb.core.SettingRepository
import com.mmb.session.ui.SessionIndicatorEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
) : ViewModel() {

    val pomCount = settingRepository.getFullPomCount()

    private val _sessionDuration: MutableLiveData<Int> = MutableLiveData()
    val sessionDuration: LiveData<Int> = _sessionDuration

    var currentSessionType: SessionState = SessionState.FOCUS
        private set

    private val _completedPom: MutableLiveData<Int> = MutableLiveData()
    val completedPom: LiveData<Int> = _completedPom

    private val _indicators: MutableLiveData<List<SessionIndicatorEntity>> = MutableLiveData()
    val indicators: LiveData<List<SessionIndicatorEntity>> = _indicators

    init {
        subscribe()
    }

    fun reloadIndicators() {
        getIndicators()
    }

    private fun subscribe() {
        updateSessionType(SessionState.FOCUS)
        getIndicators()
    }

    private fun getIndicators() {
        val indicators = arrayListOf<SessionIndicatorEntity>()

        viewModelScope.launch {
            settingRepository.getFocusDuration().collect { duration ->
                indicators.add(
                    SessionIndicatorEntity(
                        title = "Enfoque",
                        duration = duration,
                        icon = Icons.Sharp.Computer,
                        active = currentSessionType == SessionState.FOCUS
                    )
                )
                _indicators.postValue(indicators)
            }
        }

        viewModelScope.launch {
            settingRepository.getShortBreakDuration().collect { duration ->
                indicators.add(
                    SessionIndicatorEntity(
                        title = "Descanso corto",
                        duration = duration,
                        icon = Icons.Filled.LocalCafe,
                        active = currentSessionType == SessionState.SHORT_BREAK
                    )
                )
                _indicators.postValue(indicators)
            }
        }

        viewModelScope.launch {
            settingRepository.getLongBreakDuration().collect { duration ->
                indicators.add(
                    SessionIndicatorEntity(
                        title = "Descanso largo",
                        duration = duration,
                        icon = Icons.Filled.Chair,
                        active = currentSessionType == SessionState.LONG_BREAK
                    )
                )
                _indicators.postValue(indicators)
            }
        }
    }

    fun onSessionCompleted(context: Context) {
        when (currentSessionType) {
            SessionState.FOCUS -> onFocusFinish(context)
            SessionState.LONG_BREAK -> onLongBreakFinish(context)
            else -> onShortBreakFinish(context)
        }
    }

    private fun onFocusFinish(context: Context) {
        val finishedSession = currentSessionType

        viewModelScope.launch {
            pomCount.collect { count ->
                val currentPomState = _completedPom.value ?: 0
                _completedPom.value = currentPomState + 1

                notifySessionFinished(context, finishedSession)

                if (currentPomState + 1 == count) {
                    updateSessionType(SessionState.LONG_BREAK)
                } else {
                    updateSessionType(SessionState.SHORT_BREAK)
                }
            }
        }
    }

    private fun onShortBreakFinish(context: Context) {
        val finishedSession = currentSessionType
        notifySessionFinished(context, finishedSession)
        updateSessionType(SessionState.FOCUS)
    }

    private fun onLongBreakFinish(context: Context) {
        val finishedSession = currentSessionType

        viewModelScope.launch {
            pomCount.collect {
                _completedPom.value = 0
            }
        }

        notifySessionFinished(context, finishedSession)
        updateSessionType(SessionState.FOCUS)
    }

    private fun updateSessionType(type: SessionState) {
        currentSessionType = type
        getIndicators()

        val session = when (currentSessionType) {
            SessionState.FOCUS -> settingRepository.getFocusDuration()
            SessionState.LONG_BREAK -> settingRepository.getLongBreakDuration()
            else -> settingRepository.getShortBreakDuration()
        }

        viewModelScope.launch {
            session.collect {
                _sessionDuration.postValue(it)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun notifySessionFinished(context: Context, finishedSession: SessionState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val (current, next) = when (finishedSession) {
            SessionState.FOCUS -> "Enfoque" to "Descanso corto"
            SessionState.SHORT_BREAK -> "Descanso corto" to "Enfoque"
            SessionState.LONG_BREAK -> "Descanso largo" to "Enfoque"
        }

        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, "pomodoro_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Sesión finalizada")
            .setContentText("La sesión de $current ha terminado. Siguiente: $next.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(1001, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pomodoro Notifications"
            val descriptionText = "Canal para notificaciones de Pomodoro"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("pomodoro_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
