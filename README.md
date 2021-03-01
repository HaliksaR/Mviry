

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