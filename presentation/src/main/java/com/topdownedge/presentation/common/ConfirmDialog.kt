package com.topdownedge.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
    onDismissClicked: () -> Unit,
    onConfirmClicked: () -> Unit,
    title: String,
    message: String? = null,
    confirmText: String,
    dismissText: String
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
                Text(text = confirmText)
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