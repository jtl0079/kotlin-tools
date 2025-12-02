package com.myorg.kotlintools.selector

import android.R.attr.shape
import android.app.DatePickerDialog
import android.content.res.Resources
import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SegmentedButtonDefaults.itemShape
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar


/**
 * Stateless 年月选择组件（无状态）
 *
 * 只负责展示传入的年月并弹出系统 DatePickerDialog，
 * 不维护内部状态，适合外部控制状态（ViewModel）的场景。
 *
 * @param year 当前年份
 * @param month 当前月份（0 - 11，Calendar标准）
 * @param modifier Compose 修饰符
 * @param onYearMonthSelected 选择回调 (year, month)
 * @param content 自定义 UI：
 * - text: 格式化后的年月，如 "2025-11"
 * - onClick: 打开选择器
 */
@Composable
fun YearMonthPickerStateless(
    year: Int,
    month: Int,
    modifier: Modifier = Modifier,
    onYearMonthSelected: (Int, Int) -> Unit,
    content: @Composable (String, () -> Unit) -> Unit =
        { text, onClick ->
            Button(onClick = onClick, modifier = modifier) {
                Text(text)
            }
        }
) {
    val context = LocalContext.current

    // 格式化显示：YYYY-MM
    val formattedYearMonth = remember(year, month) {
        "%04d-%02d".format(year, month + 1)
    }

    val openDialog = {
        DatePickerDialog(
            context,
            { _, y, m, _ ->
                // 忽略 day，只返回 year 和 month
                onYearMonthSelected(y, m)
            },
            year,
            month,
            1 // day 固定为 1，仅作占位
        ).apply {
            // 可选：尝试隐藏 day（不同系统行为略有差异）
            try {
                datePicker.findViewById<View>(
                    Resources.getSystem().getIdentifier("day", "id", "android")
                )?.visibility = View.GONE
            } catch (_: Exception) {}
        }.show()
    }

    content(formattedYearMonth, openDialog)
}


/**
 * Stateful 年月选择组件（有状态）
 *
 * 内部维护 year/month 状态，选择后自动更新显示。
 * 适合直接在页面中使用。
 *
 * @param modifier Compose 修饰符
 * @param onYearMonthSelected 选择回调 (year, month)
 * @param content UI 渲染方式
 */
@Composable
fun YearMonthPicker(
    modifier: Modifier = Modifier,
    onYearMonthSelected: (Int, Int) -> Unit = { _, _ -> },
    content: @Composable (String, () -> Unit) -> Unit =
        { text, onClick ->
            Button(onClick = onClick, modifier = modifier) {
                Text(text)
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

    YearMonthPickerStateless(
        year = year,
        month = month,
        modifier = modifier,
        onYearMonthSelected = { y, m ->
            year = y
            month = m
            onYearMonthSelected(y, m)
        },
        content = content
    )
}


/**
 * 预设样式的 YearMonthPicker（OutlinedButton 风格）
 *
 * UI：📆 图标 + 年月文本
 * 推荐用于报表筛选、账单月份选择等场景。
 *
 * @param modifier Compose 修饰符
 * @param onYearMonthSelected 选择回调 (year, month)
 */
@Composable
fun YearMonthPickerOutlinedButton(
    modifier: Modifier = Modifier,
    buttonContentColor: Color = MaterialTheme.colorScheme.primary,
    buttonBorder: BorderStroke = BorderStroke(1.dp, buttonContentColor),
    onYearMonthSelected: (Int, Int) -> Unit = { _, _ ->  },
) {
    YearMonthPicker(
        modifier = modifier,
        onYearMonthSelected = onYearMonthSelected,
        content = { text, onClick ->
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = buttonContentColor,              // ← 文本 + 图标颜色
                    containerColor = Color.Transparent
                ),
                border = buttonBorder
            ) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = "Pick Year Month"
                )
                Spacer(Modifier.width(8.dp))
                Text(text)
            }
        }
    )
}

