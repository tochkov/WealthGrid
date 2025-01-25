package com.topdownedge.presentation.portfolio.trade

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.topdownedge.presentation.R
import com.topdownedge.presentation.common.ConfirmDialog
import com.topdownedge.presentation.common.SimpleAppBar
import com.topdownedge.presentation.portfolio.trade.chart.CustomFormatterWithListener
import com.topdownedge.presentation.portfolio.trade.chart.SimpleAssetLineChart
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTradeScreen(
    tickerCode: String,
    tickerExchange: String,
    tickerName: String,
    onBackPress: () -> Unit = {}
) {
    val viewModel: NewTradeViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState().value

    var showExitDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var price by remember { mutableStateOf("") }
    var shares by remember { mutableStateOf("") }

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    Scaffold(topBar = {
        SimpleAppBar(title = "New Trade", onBackPress = { showExitDialog = true })
    }

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            Box {
                Column {

                    Text(
                        text = tickerCode,
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = tickerName,
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )

                }
                SimpleAssetLineChart(
                    modelProducer = uiState.modelProducer,
                    modifier = Modifier.height(230.dp),
                    customFormatter = CustomFormatterWithListener(
                        uiState.priceBars,
                        selectedPriceBarListener = { index, priceBar ->
                            price = priceBar.close.toString()
                            selectedDate = priceBar.date
                        }
                    )
                )
            }

            Row {
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(4f)
                )
                TextField(
                    value = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                    onValueChange = { },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(3f)
                        .clickable { showDatePicker = true }
                )
            }

            Row {
                Text(
                    text = "Price",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(4f),
                )
                TextField(
                    value = price,
                    onValueChange = {
                        price = it
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(3f)
                )
            }

            Row {
                Text(
                    text = "Num of shares",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(4f),
                )
                TextField(
                    value = shares,
                    onValueChange = { shares = it },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(3f)
                )
            }

        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = Instant.ofEpochMilli(utcTimeMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    return !date.isAfter(LocalDate.now())
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    selectedDate = Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showExitDialog) {
        ConfirmDialog(
            onDismissClicked = {
                showExitDialog = false
            },
            onConfirmClicked = {
                showExitDialog = false
                onBackPress()
            },
            title = stringResource(R.string.discard_trade_dialog_title),
            confirmText = stringResource(R.string.discard_trade_dialog_confirm),
            dismissText = stringResource(R.string.discard_trade_dialog_dismiss)
        )
    }

}

@Preview
@Composable
fun NewTradeScreenPreview() {
    NewTradeScreen(
        tickerCode = "AAPL", tickerExchange = "NASDAQ", tickerName = "Apple Inc."
    )
}