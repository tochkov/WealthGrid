package com.topdownedge.presentation.common.chart

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
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.topdownedge.domain.fmt
import java.time.LocalDate


private val RangeProvider =
    object : CartesianLayerRangeProvider {
        override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
            minY - (maxY - minY) * 0.1 // add 10% on bottom

        override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
//            maxY + (maxY - minY) * 0.1 // add 10% on top
            maxY
    }

class CustomDateFormatter(
    val dateList: List<LocalDate> = emptyList(),
) : DefaultCartesianMarker.ValueFormatter {

    override fun format(
        context: CartesianDrawingContext,
        targets: List<CartesianMarker.Target>
    ): CharSequence {

        val target = targets.first() as LineCartesianLayerMarkerTarget
        val date = dateList.getOrNull(target.x.toInt())

        return SpannableStringBuilder()
            .append(
                date?.fmt("dd MMM YYYY") ?: "",
                ForegroundColorSpan(target.points.first().color),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
    }

}


@Composable
fun SimpleAssetLineChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
    customFormatter: CustomDateFormatter? = null,
    markerVisibilityListener: CartesianMarkerVisibilityListener? = null,
    animateIn: Boolean = true
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
            marker = customFormatter?.let { rememberMarker(customFormatter) },
            markerVisibilityListener = markerVisibilityListener

        ),
        animateIn = animateIn,
        modelProducer = modelProducer,
        modifier = modifier,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}