package com.myorg.kotlintools.singlechoice

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

@Composable
fun <T> SingleChoiceOutlinedButtonsBasic(
    options: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    selectedButtonColor: Color = MaterialTheme.colorScheme.primary,
    selectedButtonBorder: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
    unselectedButtonBorder: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),

    toLabel: (T) -> String = { it.toString() },
) {
    SingleChoiceCore(
        options = options,
        selected = selected,
        onSelected = onSelected,
        modifier = modifier,
    ) { _, item, isSelected, onClick ->


        OutlinedButton(
            onClick = onClick,
            //shape = RoundedCornerShape(12.dp),     // 每个都是圆角
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isSelected)
                    selectedButtonColor.copy(alpha = 0.1f)
                else Color.Transparent,
                contentColor = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
            ),
            border = if (isSelected) selectedButtonBorder
            else unselectedButtonBorder
            ,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = toLabel(item)
            )
        }
    }
}


@Composable
fun DemoSingleChoiceOutlinedButtonsBasic(){

    var selected by remember { mutableStateOf("B") }

    SingleChoiceOutlinedButtonsBasic(
        options = listOf("A", "B"),
        selected = selected,
        onSelected = { selected = it }
    )

}
