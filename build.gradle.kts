// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

// TODO - take a look at build-logic, buildSrc and/or convention plugins
ext {
    set("compileSdk", 35)
    set("minSdk", 24)
    set("targetSdk", 34)
}