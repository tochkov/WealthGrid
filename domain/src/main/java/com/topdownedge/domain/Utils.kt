package com.topdownedge.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun LocalDate.fmt(pattern: String = "yyyy-MM-dd") = format(DateTimeFormatter.ofPattern(pattern))

fun Double?.fmtPrice(pattern: String = "%.2f"): String = this?.let { pattern.format(it) } ?: ""

fun Double?.fmtPercent(): String = this?.let { "%.2f%%".format(it) } ?: ""