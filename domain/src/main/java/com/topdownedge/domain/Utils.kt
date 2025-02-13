package com.topdownedge.domain

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


fun LocalDate.fmt(pattern: String = "yyyy-MM-dd") = format(DateTimeFormatter.ofPattern(pattern))

fun Double?.fmtPrice(pattern: String = "%.2f"): String = this?.let { pattern.format(it) } ?: ""

//fun Double?.fmtMoney(dec: DecimalFormat = DecimalFormat("### ### ### ### ###")) = this?.let { "$${this.roundToInt()}" } ?: ""
fun Double?.fmtMoney(decFormatPattern: String = "###,###,###,###,###.00") = this?.let { DecimalFormat(decFormatPattern).format(it) + " $" } ?: ""

fun Double?.fmtPercent(): String = this?.let { "%.2f%%".format(it) } ?: ""