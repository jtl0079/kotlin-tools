package com.myorg.kotlintools.selector

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

/**
 * Stateless 时间选择组件（无状态）
 *
 * 该组件本身不持有时间状态，只负责根据传入的 hour 和 minute 展示时间，
 * 当用户选择新时间时，通过 onTimeSelected 回调通知外部。
 *
 * 适合用于：由 ViewModel 或父组件统一管理时间状态的场景。
 *
 * @param hour 当前小时（0 - 23）
 * @param minute 当前分钟（0 - 59）
 * @param modifier Compose 修饰符
 * @param is24Hour 是否使用 24 小时制
 * @param onTimeSelected 当用户选择新时间时回调 (hour, minute)
 * @param content 可自定义的 UI 内容，参数为：
 * - timeText: 格式化后的时间字符串，例如 "09:30"
 * - onClick: 用于触发时间选择器的点击事件
 */
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

/**
 * Stateful 时间选择组件（有状态）
 *
 * 内部维护时间状态（hour、minute），并在选择后自动更新。
 * 对外只需监听 onTimeSelected 即可。
 *
 * 适合用于：简单页面直接使用，不需要 ViewModel 管理状态的情况。
 *
 * @param modifier Compose 修饰符
 * @param is24Hour 是否使用 24 小时制
 * @param onTimeSelected 时间更新回调
 * @param content 自定义 UI 内容
 */
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


/**
 * 预设样式的时间选择按钮（OutlinedButton 风格）
 *
 * 内部使用 TimePicker，并提供默认 UI：
 * 图标 + 时间文本 的组合。
 *
 * 推荐用于表单、预约、设置页面。
 *
 * @param modifier Compose 修饰符
 * @param onTimeSelected 时间选择回调
 */
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
