package com.mmb.setting.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.math.roundToInt
import androidx.compose.material.Slider

@Composable
fun Slider(
    label: String,
    initialValue: Int,
    onValueChange: (Int) -> Unit,
    valuePlaceHolder: String = "",
    maxValue: Int
) {
    val sliderPosition = remember { mutableIntStateOf(initialValue) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label)
            Text(text = "${sliderPosition.intValue} $valuePlaceHolder")
        }

        Slider(
            value = sliderPosition.intValue.toFloat(),
            onValueChange = {
                sliderPosition.intValue = it.roundToInt()
                onValueChange(sliderPosition.intValue)
            },
            valueRange = 1f..maxValue.toFloat()
        )
    }
}