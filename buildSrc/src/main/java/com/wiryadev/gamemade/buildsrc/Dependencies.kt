package com.wiryadev.gamemade.buildsrc

object AndroidX {
    const val coreKtx = "androidx.core:core-ktx:1.3.2"
    const val appCompat = "androidx.appcompat:appcompat:1.3.0"
    const val multidex = "androidx.multidex:multidex:2.0.1"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"

    object UI {
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
        const val material = "com.google.android.material:material:1.4.0"
        const val coil = "io.coil-kt:coil:1.2.2"
        const val lottie = "com.airbnb.android:lottie:3.7.0"
        const val shimmer = "com.facebook.shimmer:shimmer:0.5.0"
    }

    object Navigation {
        private const val version = "2.3.5"
        const val ui = "androidx.navigation:navigation-ui-ktx:$version"
        const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
    }
}



object Hilt {
    private const val version = "2.37"
    const val android = "com.google.dagger:hilt-android:$version"
    const val compiler = "com.google.dagger:hilt-compiler:$version"
}

object Test {
    object Junit {
        private const val version = "4.13.1"
        const val junit = "junit:junit:$version"
        const val androidExt = "androidx.test.ext:junit-ktx:1.1.2"
    }

    object Espresso {
        private const val version = "3.4.0"
        const val core = "androidx.test.espresso:espresso-core:$version"
    }
}