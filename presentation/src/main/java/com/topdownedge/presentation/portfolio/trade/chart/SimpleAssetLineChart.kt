package com.topdownedge.presentation.portfolio.trade.chart

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.topdownedge.domain.entities.common.PriceBar
import com.topdownedge.domain.fmt


private val RangeProvider =
    object : CartesianLayerRangeProvider {
        override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
            minY - (maxY - minY) * 0.1 // add 10% on bottom

        override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
            maxY + (maxY - minY) * 0.1 // add 10% on top
//            maxY
    }

/**
 * A custom formatter that doubles as a selection listener for price bar data.
 * This is a workaround solution due to library limitations in Vico charts.
 * While this violates separation of concerns, it provides a way to detect
 * selection changes through the formatter's update cycle.
 *
 * After writing this i found out there is markerVisibilityListener...
 *
 * @param priceList List of price bars to display
 * @param color Default color for the marker text
 * @param selectedPriceBarListener Callback triggered when a new price bar is selected
 */
class CustomFormatterWithListener(
    val priceList: List<PriceBar> = emptyList(),
    val color: Long = -1,
    val selectedPriceBarListener: ((index: Int, priceBar: PriceBar) -> Unit)? = null
) : DefaultCartesianMarker.ValueFormatter {

    var lastBar: PriceBar? = null

    override fun format(
        context: CartesianDrawingContext,
        targets: List<CartesianMarker.Target>
    ): CharSequence {

        val target = targets.first() as LineCartesianLayerMarkerTarget
        val priceBar = priceList.getOrNull(target.x.toInt())

        if (lastBar != priceBar && priceBar != null) {
            val position = target.x.toInt()
            lastBar = priceList.get(position)
            selectedPriceBarListener?.invoke(position, priceBar)
        }

        return SpannableStringBuilder()
            .append(
                lastBar?.date?.fmt("MM/dd - ") ?: "",
                ForegroundColorSpan(Color.Yellow.hashCode()),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            .append(" ")
            .append(
                lastBar?.close?.toString() ?: "",
                ForegroundColorSpan(target.points.first().color),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )

    }

}


@Composable
fun SimpleAssetLineChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier,
    customFormatter: CustomFormatterWithListener
) {
    val lineColor = MaterialTheme.colorScheme.primary

    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(fill(lineColor)),
                        areaFill =
                        LineCartesianLayer.AreaFill.single(
                            fill(
                                ShaderProvider.verticalGradient(
                                    arrayOf(lineColor.copy(alpha = 0.1f), Color.Transparent)
                                )
                            )
                        ),
                    )
                ),
                rangeProvider = RangeProvider,
            ),
            marker = rememberMarker(customFormatter)

        ),
        modelProducer,
        modifier,
        rememberVicoScrollState(scrollEnabled = false),
    )
}