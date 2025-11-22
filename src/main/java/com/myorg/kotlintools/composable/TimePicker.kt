package com.myorg.kotlintools.composable

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun TimePickerStateless(
    hour: Int,
    minute: Int,
    modifier: Modifier = Modifier,
    is24Hour: Boolean = true,
    onTimeSelected: (Int, Int) -> Unit,
    content: @Composable (String, () -> Unit) -> Unit =
        { timeText, onClick ->
            Button(onClick = onClick, modifier = modifier) {
                Text(timeText)
            }
        }
) {
    val context = LocalContext.current

    val formattedTime = remember(hour, minute) {
        "%02d:%02d".format(hour, minute)
    }

    val openDialog = {
        TimePickerDialog(
            context,
            { _, h, m -> onTimeSelected(h, m) },
            hour,
            minute,
            is24Hour
        ).show()
    }

    content(formattedTime, openDialog)
}


@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    is24Hour: Boolean = true,
    onTimeSelected: (Int, Int) -> Unit = { _, _ -> },
    content: @Composable (String, () -> Unit) -> Unit =
        { timeText, onClick ->
            Button(onClick = onClick, modifier = modifier) {
                Text(timeText)
            }
        }
) {
    val calendar = remember { Calendar.getInstance() }

    var hour by rememberSaveable {
        mutableIntStateOf(calendar.get(Calendar.HOUR_OF_DAY))
    }
    var minute by rememberSaveable {
        mutableIntStateOf(calendar.get(Calendar.MINUTE))
    }

    TimePickerStateless(
        hour = hour,
        minute = minute,
        modifier = modifier,
        is24Hour = is24Hour,
        onTimeSelected = { h, m ->
            hour = h
            minute = m
            onTimeSelected(h, m)
        },
        content = content
    )
}


@Composable
fun TimePickerOutlinedButton(
    modifier: Modifier = Modifier,
    onTimeSelected: (Int, Int) -> Unit
) {
    TimePicker(

        modifier = modifier,
        onTimeSelected = onTimeSelected,
        content = { time, onClick ->
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
            ) {
                Icon(Icons.Default.AccessTime, contentDescription = "Pick time")
                Spacer(Modifier.width(8.dp))
                Text(time)
            }
        }
    )
}
