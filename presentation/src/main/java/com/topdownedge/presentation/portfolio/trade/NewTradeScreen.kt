package com.topdownedge.presentation.portfolio.trade

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerVisibilityListener
import com.topdownedge.domain.fmtPercent
import com.topdownedge.presentation.R
import com.topdownedge.presentation.common.ConfirmDialog
import com.topdownedge.presentation.common.SimpleAppBar
import com.topdownedge.presentation.portfolio.trade.chart.CustomDateFormatter
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
    val uiEvents = viewModel.uiEvents

    var showExitDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val messageSuccess = stringResource(R.string.new_trade_success, tickerCode)
    LaunchedEffect(Unit) {
        uiEvents.collect { event ->
           when(event){
               is UiEvent.ShowToast -> {
                   val errorMessageRes = when(event.inputError){
                       InputError.DATE -> R.string.enter_valid_date
                       InputError.PRICE -> R.string.enter_valid_price
                       InputError.SHARES -> R.string.enter_valid_shares
                   }
                   Toast.makeText(context, errorMessageRes, Toast.LENGTH_SHORT).show()
               }
               is UiEvent.Navigate -> {
                   Toast.makeText(context, messageSuccess, Toast.LENGTH_SHORT).show()
                   onBackPress()
               }
           }
        }
    }

    BackHandler(enabled = uiState.manualInputDetected) {
        showExitDialog = true
    }

    Scaffold(
        topBar = {
            SimpleAppBar(
                title = stringResource(R.string.new_trade),
                onBackPress = {
                    if (uiState.manualInputDetected) {
                        showExitDialog = true
                    } else {
                        onBackPress()
                    }
                },
                actionImage = Icons.Default.Done,
                actionDescription = "Submit",
                onActionClick = { viewModel.onSubmitClicked() }
            )
        }

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = tickerCode,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    Text(
                        text = tickerName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = uiState.displayPriceStr,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    val pctChange = uiState.displayChangePct
                    if (pctChange != null) {
                        Text(
                            text = (if (pctChange >= 0) "+" else "") + pctChange.fmtPercent(),
                            color = if (pctChange >= 0) Color.Green else Color.Red,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .align(Alignment.End)
                        )
                    }
                }
            }
            val chartHeight = 220.dp
            Box(modifier = Modifier.height(chartHeight)) {
                if (uiState.chartError) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                            .alpha(0.55f)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.undraw_growing_am8t),
                            contentDescription = "",
                            modifier = Modifier
                                .alpha(0.6f)
                                .fillMaxSize(0.8f)
                                .padding(bottom = 12.dp)
                        )
                        Text(
                            text = stringResource(R.string.problem_with_chart),
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                SimpleAssetLineChart(
                    modelProducer = viewModel.chartModelProducer,
                    customFormatter = CustomDateFormatter(
                        viewModel.priceBars.map { it.date }
                    ),
                    markerVisibilityListener = object : CartesianMarkerVisibilityListener {
                        override fun onShown(
                            marker: CartesianMarker,
                            targets: List<CartesianMarker.Target>
                        ) {
                            val index = targets.first().x.toInt()
                            viewModel.onUserClickOrDragChart(index)
                        }

                        override fun onUpdated(
                            marker: CartesianMarker,
                            targets: List<CartesianMarker.Target>
                        ) {
                            val index = targets.first().x.toInt()
                            viewModel.onUserClickOrDragChart(index)
                        }

                        override fun onHidden(marker: CartesianMarker) {
                            viewModel.onUserChartInteractionOver()
                        }
                    }
                )
            }

            BuySellSwitch(
                checked = uiState.isBuyState,
                onCheckedChange = {
                    viewModel.userSetBuyState(it)
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp)
            )

            Row {
                Text(
                    text = stringResource(R.string.select_date),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(4f)
                )
                TextField(
                    value = uiState.selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yy")),
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
                            contentDescription = null,
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
                    text = stringResource(R.string.enter_price),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(4f),
                )
                TextField(
                    value = uiState.selectedPrice,
                    onValueChange = {
                        viewModel.userSetPrice(it)
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        val ctx = LocalContext.current
                        UpDownClickableArrows { clickedUp ->
                            Toast.makeText(
                                ctx,
                                if (clickedUp) "Increment Up" else "Increment Down",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(3f)
                )
            }

            Row {
                Text(
                    text = stringResource(R.string.num_of_shares),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(4f),
                )
                TextField(
                    value = uiState.selectedShares,
                    onValueChange = { viewModel.userSetShares(it) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        val ctx = LocalContext.current
                        UpDownClickableArrows { clickedUp ->
                            Toast.makeText(
                                ctx,
                                if (clickedUp) "Increment Up" else "Increment Down",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(3f)
                )
            }


            Text(
                text = "Total: $${
                    uiState.totalPosition
                }",
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.End),
            )

            Text(
                text = "You already have 13 shares of $tickerCode at average price of 123.45",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(36.dp)
                    .align(Alignment.CenterHorizontally),
            )


        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.selectedDate.atStartOfDay(ZoneId.systemDefault())
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
                    viewModel.userSetDate(
                        Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    )
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


@Composable
fun BuySellSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        modifier = modifier
            .scale(1.3f),
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = Color.Transparent,
            uncheckedThumbColor = Color.Red,
            uncheckedTrackColor = Color.Transparent,
            checkedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            uncheckedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        thumbContent = if (checked) {
            {
                Text(
                    text = "BUY",
                    fontSize = 7.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            {
                Text(
                    text = "SELL",
                    fontSize = 7.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun UpDownClickableArrows(
    onClick: (isUp: Boolean) -> Unit,
) {
    Column {
        Icon(
            modifier = Modifier.clickable { onClick(true) },
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
            modifier = Modifier.clickable { onClick(false) },
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
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