package com.mmb.setting.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mmb.setting.viewmodel.SettingViewModel
import com.mmb.ui_compose.Layout

@Composable
fun Setting() {
    SettingScreen(hiltViewModel())
}

@Composable
internal fun SettingScreen(
    viewModel: SettingViewModel,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        val sessionName by viewModel.sessionName.observeAsState(initial = "")

        Text(
            text = "Pomodoro Deluxe",
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = Layout.largeFontSize
        )
        Text(text = "Nombre de la tarea", modifier = Modifier.padding(top = 8.dp))
        TextField(
            value = sessionName,
            onValueChange = viewModel::setSessionName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            singleLine = true,
        )
        viewModel.setting.observeAsState().value?.let {
            Slider("Tiempo de enfoque", it.focusDuration, viewModel::setFocusDuration, "Min", 60)
            Slider(
                "Tiempo de descanso corto", it.shortBreakDuration, viewModel::setShortBreak, "Min", 60
            )
            Slider("Tiempo de descanso largo", it.longBreakDuration, viewModel::setLongBreak, "Min", 60)
            Slider("Conjunto de pomodoros", it.sessionCount, viewModel::setSessionCount, "Poms", 10)
        }
    }
}