package com.mmb.clock.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mmb.clock.R
import com.mmb.clock.entity.PomodoroScreenEntity
import com.mmb.clock.viewmodel.PomodoroClockViewModel
import com.mmb.ui_compose.Layout
import com.mmb.ui_compose.component.pom.ControlButton
import com.mmb.ui_compose.component.pom.PomodoroClock

@Composable
fun Clock(
    navigateToSettings: () -> Unit,
    duration: Int,
    viewModel: PomodoroClockViewModel,
    onTimerCompleted: () -> Unit
) {
    viewModel.subscribe(duration)
    Clock(viewModel = viewModel, navigateToSettings, onTimerCompleted)
}

@Composable
internal fun Clock(
    viewModel: PomodoroClockViewModel,
    navigateToSettings: () -> Unit,
    onTimerCompleted: () -> Unit
) {
    val timerState = viewModel.timer.observeAsState()
    val timerRunning = viewModel.timerRunning.observeAsState()
    val progress = viewModel.progress.observeAsState()
    PomScreen(
        PomodoroScreenEntity(
            sessionName = viewModel.sessionName.collectAsState(initial = "").value,
            text = timerState.value?.toString() ?: "",
            numberOfPoms = 3,
            pomsCompleted = 2,
            timerRunning = timerRunning.value ?: false,
            progress = progress.value ?: 0f,
        ),
        navigateToSettings = navigateToSettings,
        onStartClicked = { viewModel.startTimer(onTimerCompleted) },
        onPauseClicked = viewModel::pauseTimer,
        onRestartClicked = viewModel::restartTimer,
        onNextClicked = { viewModel.skipSession(onTimerCompleted) }
    )
}

@Composable
fun PomScreen(
    entity: PomodoroScreenEntity,
    navigateToSettings: () -> Unit,
    onStartClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onRestartClicked: () -> Unit,
    onNextClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(id = R.string.action_setting),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.clickable { navigateToSettings() }
            )
            Text(
                text = entity.sessionName,
                modifier = Modifier.padding(vertical = 32.dp),
                fontSize = 25.sp
            )
        }
        Box {
            PomodoroClock(
                text = entity.text,
                progress = entity.progress,
                modifier = Modifier
                    .padding(horizontal = Layout.largeMargin)
                    .aspectRatio(1f)
            )
        }
        ControlButton(
            running = entity.timerRunning,
            onResumeClicked = onStartClicked,
            onRestartClicked = onRestartClicked,
            onPauseClicked = onPauseClicked,
            onNextClicked = onNextClicked
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}
