package com.john.kmpapplication.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterChips(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { category ->
            val isSelected = category == selectedItem
            FilterChip(
                modifier = Modifier.height(45.dp),
                selected = isSelected,
                label = { Text(text = category) },
                onClick = { onItemSelected(category) },
            )
        }
    }
}