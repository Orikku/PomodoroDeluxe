package com.marymirzaie.pomodoro.pom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marymirzaie.pomodoro.R
import com.marymirzaie.pomodoro.pom.entity.TimerState

@Composable
//the final view
fun Pomodoro(timerState: TimerState, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .size(200.dp)
        .clip(CircleShape)
        .shadow(elevation = 1.dp, shape = CircleShape, clip = true)
        .background(MaterialTheme.colors.secondary)
        .wrapContentSize(align = Alignment.Center)
        .padding(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            //todo read about this
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                PomodoroClock(timerState)
                PomodoroCount()
            }
        }
    }
}

@Composable
//a clock that goes through by time
fun PomodoroClock(timerState: TimerState, modifier: Modifier = Modifier) {
    Row {
        PomodoroClockText("${timerState.hours}:${timerState.minutes}", modifier = modifier)
    }
}

@Composable
fun PomodoroClockText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.h3,
        modifier = modifier,
        color = MaterialTheme.colors.primaryVariant
    )
}

@Composable
//consists of 3 pomodoro icons that shows how many times
fun PomodoroCount(
    modifier: Modifier = Modifier,
    numberOfPoms: Int = 3,
    pomsCompleted: Int = 1,
) {
    //todo use tint properly
    Row(modifier = modifier
        .padding(8.dp)
        .wrapContentSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        var comp = pomsCompleted
        for (index in 0 until numberOfPoms) {
            val tint = if (comp != 0) {
                comp -= 1
                Color.Unspecified
            } else LocalContentColor.current.copy(alpha = LocalContentAlpha.current)

            Icon(painter = painterResource(id = R.drawable.ic_pomodoro_tomato),
                tint = tint,
                contentDescription = null,
                modifier = Modifier.size(40.dp))
        }
    }
}

@Composable
//consists of pause and resume buttons
fun PomodoroControl() {

}

@Composable
@Preview
fun PomodoroPreview() {
    Pomodoro(timerState = TimerState(minutes = 10, hours = 5))
}

@Composable
@Preview
fun PomodoroCountPreview() {
    PomodoroCount()
}