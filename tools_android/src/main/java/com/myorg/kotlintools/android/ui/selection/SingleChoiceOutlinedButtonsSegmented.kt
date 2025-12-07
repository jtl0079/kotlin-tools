package com.myorg.kotlintools.android.ui.selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> SingleChoiceOutlinedButtonsSegmented(
    options: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    toLabel: (T) -> String = { it.toString() },
) {
    SingleChoiceCore(
        options = options,
        selected = selected,
        onSelected = onSelected,
        modifier = modifier,
    ) { index, item, isSelected, onClick ->

        val shape = when (index) {
            0 -> RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
            options.lastIndex -> RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
            else -> RoundedCornerShape(0.dp)
        }

        OutlinedButton(
            onClick = onClick,
            shape = shape,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor =
                    if (isSelected) MaterialTheme.colorScheme.primary.copy(0.1f)
                    else Color.Transparent,
                contentColor =
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(toLabel(item))
        }
    }
}


@Composable
fun DemoSingleChoiceOutlinedButtonsSegmented(){

    var selected by remember { mutableStateOf("B") }

    SingleChoiceOutlinedButtonsSegmented(
        options = listOf("A", "B"),
        selected = selected,
        onSelected = { selected = it }
    )

}
