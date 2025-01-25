package com.topdownedge.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleAppBar(
    title: String,
    onBackPress: () -> Unit,
    actionImage: ImageVector? = null,
    actionDescription: String? = null,
    onActionClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        },
        actions = {
            if (actionImage != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionImage,
                        contentDescription = actionDescription
                    )
                }
            }
        }
    )
}
