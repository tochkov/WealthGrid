package com.topdownedge.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ConfirmDialog(
    onDismissClicked: () -> Unit,
    onConfirmClicked: () -> Unit,
    title: String,
    message: String? = null,
    confirmText: String,
    dismissText: String,
    confirmColor: Color = Color.Red
) {
    AlertDialog(
        onDismissRequest = onDismissClicked,
        title = {
            Text(text = title)
        },
        text = {
            if (message != null) {
                Text(text = message)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmClicked()
                }
            ) {
                Text(text = confirmText, color = confirmColor)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissClicked()
                }
            ) {
                Text(text = dismissText)
            }
        }
    )
}