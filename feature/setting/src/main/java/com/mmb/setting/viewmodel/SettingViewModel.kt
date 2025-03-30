package com.mmb.setting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmb.setting.datasource.SettingRepositoryImpl
import com.mmb.setting.entity.SettingViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: SettingRepositoryImpl,
) : ViewModel() {

    private val settingViewState: Flow<SettingViewState> = repository.getSettings()

    private val _setting: MutableLiveData<SettingViewState> = MutableLiveData()
    val setting: LiveData<SettingViewState> = _setting

    private val _sessionName = MutableLiveData("")
    val sessionName: LiveData<String> get() = _sessionName

    private var debounceJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getSessionName().collectLatest { nameFromDb ->
                if (_sessionName.value.isNullOrEmpty()) _sessionName.value = nameFromDb
            }
        }
    }

    fun setSessionName(name: String) {
        _sessionName.value = name
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(500)
            repository.setSessionName(name)
        }
    }

    fun setFocusDuration(value: Int) {
        viewModelScope.launch {
            repository.updateSessionDuration(value)
        }
    }

    init {
        viewModelScope.launch {
            settingViewState.collect {
                _setting.value = it
            }
        }
    }

    fun setShortBreak(value: Int) {
        viewModelScope.launch {
            repository.updateShortBreakDuration(value)
        }
    }

    fun setLongBreak(value: Int) {
        viewModelScope.launch {
            repository.updateLongBreakDuration(value)
        }
    }

    fun setSessionCount(value: Int) {
        viewModelScope.launch {
            repository.updateSessionCount(value)
        }
    }

    fun enableSounds(enable: Boolean) {
        viewModelScope.launch {
            repository.enableSounds(enable)
        }
    }

    fun getAllThemes(): List<String> {
        return repository.getThemes()
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            repository.updateTheme(theme)
        }
    }
}