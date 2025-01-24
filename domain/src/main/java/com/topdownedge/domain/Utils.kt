package com.topdownedge.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun LocalDate.fmt(pattern: String = "yyyy-MM-dd") = format(DateTimeFormatter.ofPattern(pattern))