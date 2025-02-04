package com.topdownedge.presentation.common.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.topdownedge.domain.entities.common.PriceBar
import com.topdownedge.domain.fmt

private val RangeProvider =
    object : CartesianLayerRangeProvider {
        override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
//            minY - (maxY - minY) * 0.1 // add 10% on bottom
            minY

        override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
//            maxY + (maxY - minY) * 0.1 // add 10% on top
            maxY
    }

@Composable
fun SimpleCandlestickChart(
    modifier: Modifier = Modifier,
    chartModelProducer: CartesianChartModelProducer,
    priceBars: List<PriceBar>
) {

    val bottomAxisValueFormatter =
        object : CartesianValueFormatter {
            override fun format(
                context: CartesianMeasuringContext,
                value: Double,
                verticalAxisPosition: Axis.Position.Vertical?,
            ) = if (priceBars.isNotEmpty()) priceBars[value.toInt()].date.fmt("dd MMM") else "x"
        }

    CartesianChartHost(
        rememberCartesianChart(
            rememberCandlestickCartesianLayer(rangeProvider = RangeProvider),
            startAxis = VerticalAxis.rememberStart(
//                        valueFormatter = StartAxisValueFormatter,
//                        itemPlacer = StartAxisItemPlacer,
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = null,
                valueFormatter = bottomAxisValueFormatter
            ),
            marker = rememberMarker(
//                        valueFormatter = MarkerValueFormatter,
                showIndicator = false
            ),
        ),
        modelProducer = chartModelProducer,
        modifier = modifier,
        rememberVicoScrollState(scrollEnabled = false)
    )
}