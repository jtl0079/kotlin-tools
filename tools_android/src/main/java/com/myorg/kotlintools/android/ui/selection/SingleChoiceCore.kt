package com.myorg.kotlintools.android.ui.selection

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> SingleChoiceCore(
    options: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.(index: Int, item: T, isSelected: Boolean, onClick: () -> Unit) -> Unit
) {
    Row(modifier = modifier) {
        options.forEachIndexed { index, item ->
            val isSelected = item == selected
            content(index, item, isSelected) {
                onSelected(item)
            }
        }
    }
}

