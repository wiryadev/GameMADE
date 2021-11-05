package com.wiryadev.gamemade.buildsrc

object Libs {
    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val appCompat = "androidx.appcompat:appcompat:1.3.1"
        const val multidex = "androidx.multidex:multidex:2.0.1"
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"

        object UI {
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.1"
            const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
            const val material = "com.google.android.material:material:1.4.0"
            const val coil = "io.coil-kt:coil:1.4.0"
            const val lottie = "com.airbnb.android:lottie:4.1.0"
            const val shimmer = "com.facebook.shimmer:shimmer:0.5.0"
        }

        object Navigation {
            private const val version = "2.3.5"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
        }
    }

    object Coroutines {
        private const val version = "1.5.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object Lifecycle {
        const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0"
        const val activityKtx = "androidx.activity:activity-ktx:1.2.3"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.5"
    }

    object Hilt {
        private const val version = "2.40"
        const val android = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-compiler:$version"
    }

    object Networking {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"

        object OkHttp {
            const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.1"
        }
    }

    object Room {
        private const val version = "2.3.0"
        const val ktx = "androidx.room:room-ktx:$version"
        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val testing = "androidx.room:room-testing:$version"

        object Security {
            const val sqliteKtx = "androidx.sqlite:sqlite-ktx:2.1.0"
            const val sqlCipher = "net.zetetic:android-database-sqlcipher:4.4.3"
        }
    }

    object Test {
        object Junit {
            private const val version = "4.13.2"
            const val junit = "junit:junit:$version"
            const val androidExt = "androidx.test.ext:junit-ktx:1.1.2"
        }

        object Espresso {
            private const val version = "3.4.0"
            const val core = "androidx.test.espresso:espresso-core:$version"
        }
    }
}