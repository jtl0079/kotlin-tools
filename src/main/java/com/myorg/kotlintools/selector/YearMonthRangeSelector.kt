package com.myorg.kotlintools.selector

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myorg.kotlintools.singlechoice.SingleChoiceOutlinedButtonsBasic
import kotlinx.coroutines.processNextEventInCurrentThread
import java.time.LocalDate
import java.time.YearMonth

private fun calculateEndDate(
    startYear: Int,
    startMonth: Int,
    useYear: Boolean
): Pair<Int, Int> {
    return if (useYear) {
        // +1 year
        (startYear + 1) to startMonth
    } else {
        // +1 month（处理跨年）
        val ym = YearMonth.of(startYear, startMonth + 1).plusMonths(1)
        (ym.year to (ym.monthValue - 1))
    }
}


@Composable
fun YearMonthRangeSelector(
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    buttonBorder: BorderStroke = BorderStroke(1.dp, primaryColor),
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val now = remember { LocalDate.now() }
    // 左上角 Start Date
    var startYear by rememberSaveable { mutableIntStateOf(now.year) }
    var startMonth by rememberSaveable { mutableIntStateOf(now.monthValue - 1) }

    // 下方模式：按年 or 按月
    val options = listOf("Year", "Month")
    var mode by rememberSaveable { mutableStateOf("Year") }

    // 右上角 End Date（根据模式计算）
    val (endYear, endMonth) = remember(startYear, startMonth, mode) {
        calculateEndDate(
            startYear,
            startMonth,
            mode == "Year"
        )
    }

    Column(modifier) {

        // ======== Row 1 ========
        Row(Modifier.fillMaxWidth()) {

            // (1,1) Start Date Picker
            YearMonthPickerOutlinedButton(
                modifier = Modifier.weight(1f),
                buttonContentColor = primaryColor,
                buttonBorder = buttonBorder,
                onYearMonthSelected = { y, m ->
                    startYear = y
                    startMonth = m
                }
            )

            // (2,1) End Date (disabled)
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = primaryColor,
                    disabledContentColor = primaryColor.copy(alpha = 0.9f)
                ),
                border = buttonBorder,
                enabled = false
            ) {
                Text(
                    text = "%04d-%02d".format(endYear, endMonth + 1),
                    color = primaryColor
                )
            }
        }
        // ======== Row 2 ========
        Row(Modifier.fillMaxWidth()) {

            // (1,2) Single choice: Year / Month
            SingleChoiceOutlinedButtonsBasic(
                options = options,
                selected = mode,
                onSelected = { mode = it },
                selectedButtonBorder = buttonBorder,
                modifier = Modifier
                    .weight(1f)
            )

        }
    }
}


@Composable
fun DemoYearMonthRangeSelector(){
    val primaryColor: Color = MaterialTheme.colorScheme.primary
    val buttonBorder: BorderStroke = BorderStroke(2.dp, primaryColor)

    YearMonthRangeSelector(
        primaryColor = primaryColor,
        buttonBorder = buttonBorder
    )
}