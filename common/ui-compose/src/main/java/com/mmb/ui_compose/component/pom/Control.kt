package com.mmb.ui_compose.component.pom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LastPage
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.LastPage
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mmb.ui_compose.Layout.iconSize
import com.mmb.ui_compose.R

@Composable
fun ControlButton(
    modifier: Modifier = Modifier,
    running: Boolean,
    onResumeClicked: () -> Unit,
    onRestartClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    if (running) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            IconButton(
                onClick = { onPauseClicked.invoke() },
            ) {
                Icon(
                    imageVector = Icons.Filled.Pause,
                    contentDescription = stringResource(id = R.string.stop_timer_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(iconSize)
                )
            }

            Spacer(modifier = Modifier.width(width = 16.dp))

            IconButton(
                onClick = { onRestartClicked.invoke() },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = stringResource(id = R.string.restart_timer_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(iconSize)
                )
            }

            Spacer(modifier = Modifier.width(width = 16.dp))

            IconButton(
                onClick = { onNextClicked.invoke() },
            ) {
                Icon(
                    imageVector = Icons.Outlined.LastPage,
                    contentDescription = stringResource(id = R.string.restart_timer_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onResumeClicked.invoke() },
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(id = R.string.resume_timer_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(iconSize)
                )
            }

            Spacer(modifier = Modifier.width(width = 16.dp))

            IconButton(
                onClick = { onNextClicked.invoke() },
            ) {
                Icon(
                    imageVector = Icons.Filled.LastPage,
                    contentDescription = stringResource(id = R.string.resume_timer_description),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

@Preview
@Composable
fun PomodoroRunButtonPreview() {
    ControlButton(
        running = true,
        onResumeClicked = {},
        onRestartClicked = {},
        onPauseClicked = {},
        onNextClicked = {}
    )
}

@Preview
@Composable
fun PomodoroButtonPreview() {
    ControlButton(
        running = false,
        onResumeClicked = {},
        onRestartClicked = {},
        onPauseClicked = {},
        onNextClicked = {}
    )
}