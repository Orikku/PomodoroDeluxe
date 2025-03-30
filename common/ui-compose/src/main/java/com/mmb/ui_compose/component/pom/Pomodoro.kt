package com.mmb.ui_compose.component.pom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mmb.ui_compose.Layout

@Composable
//a clock that goes through by time
fun PomodoroClock(
    text: String,
    progress: Float,
    modifier: Modifier = Modifier,
    total: String = "12s",
) {
    PomodoroProgress(percentage = progress, modifier = modifier.padding(Layout.largeMargin)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .padding(4.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (topText, centerText) = createRefs()

                Text(
                    text = total,
                    style = MaterialTheme.typography.body1,
                    modifier = modifier
                        .padding(horizontal = 34.dp)
                        .wrapContentSize()
                        .constrainAs(topText) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(centerText.top)
                        },
                    color = MaterialTheme.colors.background
                )

                Text(
                    text = text,
                    style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.SemiBold),
                    modifier = modifier
                        .padding(horizontal = 20.dp)
                        .wrapContentSize()
                        .constrainAs(centerText) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}

@Preview
@Composable
fun PomodoroClockPreview() {
    PomodoroClock("12:00", 0.23f, total="100s")
}