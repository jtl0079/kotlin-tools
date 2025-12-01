package com.myorg.kotlintools.selector

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar


/**
 * Stateless 日期选择组件（无状态）
 *
 * 只负责展示传入的日期并弹出系统 DatePickerDialog，
 * 不维护内部状态，适合由外部（如 ViewModel）管理日期的场景。
 *
 * @param year 当前年份
 * @param month 当前月份（0 - 11，Calendar 标准）
 * @param day 当前日期
 * @param modifier Compose 修饰符
 * @param onDateSelected 当用户选择新日期时回调 (year, month, day)
 * @param content 自定义 UI：
 * - dateText: 格式化后的日期文本，如 "2025-11-21"
 * - onClick: 打开日期选择器
 */
@Composable
fun DatePickerStateless(
    year: Int,
    month: Int,
    day: Int,
    modifier: Modifier = Modifier,
    onDateSelected: (Int, Int, Int) -> Unit,
    content: @Composable (String, () -> Unit) -> Unit =
        { dateText, onClick ->
            Button(onClick = onClick, modifier = modifier) {
                Text(dateText)
            }
        }
) {
    val context = LocalContext.current

    // 格式化显示日期
    val formattedDate = remember(year, month, day) {
        "%04d-%02d-%02d".format(year, month + 1, day)
    }

    val openDialog = {
        DatePickerDialog(
            context,
            { _, y, m, d ->
                onDateSelected(y, m, d)
            },
            year,
            month,
            day
        ).show()
    }

    content(formattedDate, openDialog)
}


/**
 * Stateful 日期选择组件（有状态）
 *
 * 内部维护 year/month/day 状态，并在选择后自动更新。
 * 适合页面直接使用的场景。
 *
 * @param modifier Compose 修饰符
 * @param onDateSelected 日期选择回调
 * @param content 自定义 UI 内容
 */
@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    onDateSelected: (Int, Int, Int) -> Unit = { _, _, _ -> },
    content: @Composable (String, () -> Unit) -> Unit =
        { dateText, onClick ->
            Button(onClick = onClick, modifier = modifier) {
                Text(dateText)
            }
        }
) {
    val calendar = remember { Calendar.getInstance() }

    var year by rememberSaveable {
        mutableIntStateOf(calendar.get(Calendar.YEAR))
    }
    var month by rememberSaveable {
        mutableIntStateOf(calendar.get(Calendar.MONTH))
    }
    var day by rememberSaveable {
        mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    }

    DatePickerStateless(
        year = year,
        month = month,
        day = day,
        modifier = modifier,
        onDateSelected = { y, m, d ->
            year = y
            month = m
            day = d
            onDateSelected(y, m, d)
        },
        content = content
    )
}


/**
 * 预设样式的日期选择按钮（OutlinedButton 风格）
 *
 * UI：📅 图标 + 日期文本
 * 适合用于表单 / 预约 / 设置页面。
 *
 * @param modifier Compose 修饰符
 * @param onDateSelected 日期选择回调
 */
@Composable
fun DatePickerOutlinedButton(
    modifier: Modifier = Modifier,
    onDateSelected: (Int, Int, Int) -> Unit = {_, _, _, ->}
) {
    DatePicker(
        modifier = modifier,
        onDateSelected = onDateSelected,
        content = { date, onClick ->
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Pick date"
                )
                Spacer(Modifier.width(8.dp))
                Text(date)
            }
        }
    )
}
