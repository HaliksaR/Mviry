[![](https://jitpack.io/v/HaliksaR/Mviry.svg)](https://jitpack.io/#HaliksaR/Mviry) [![Android CI](https://github.com/HaliksaR/Mviry/actions/workflows/android.yml/badge.svg?branch=master)](https://github.com/HaliksaR/Mviry/actions/workflows/android.yml)

## Download
```groovy
android {
    compileOptions {
        // This library uses Java 8 features, this is required
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    def libsuVersion = '0.0.1'

    // The core
    implementation "com.github.haliksar.mviry:mviry-core:$libsuVersion"
}
```


## Guide

TODO
