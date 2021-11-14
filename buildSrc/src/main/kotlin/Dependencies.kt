object Dependencies {
    /**********************************  Project  ******************************************/
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"

    /**********************************  App UI  ******************************************/
    const val coreKtx =
        "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appcompat =
        "androidx.appcompat:appcompat:${Versions.appcompat}"
    private const val material =
        "com.google.android.material:material:${Versions.material}"
    private const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    val appUis = listOf(coreKtx, appcompat, material, constraintLayout)

    /**********************************  Test libs  ******************************************/

    const val junit =
        "junit:junit:${Versions.junit}"
    const val etxJunit =
        "androidx.test.ext:junit:${Versions.etxJunit}"
    const val espressoCore =
        "androidx.test.espresso:espresso-core:${Versions.espressoCore}"

    val testLibs = listOf(junit, etxJunit, espressoCore)

    /**********************************  Other  ******************************************/

    // add other libs here

    // coroutines
    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    const val COROUTINES_ROOM =
        "androidx.room:room-ktx:${Versions.ROOM}"

    /******************************************************/
    // hilt
    const val HILT_GRADLE =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
    const val HILT_ANDROID =
        "com.google.dagger:hilt-android:${Versions.HILT}"

    /******************************************************/
    // kapt
    const val KAPT_HILT =
        "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
    const val KAPT_ROOM =
        "androidx.room:room-compiler:${Versions.ROOM}"

    /******************************************************/
    // room
    const val ROOM =
        "androidx.room:room-runtime:${Versions.ROOM}"
    const val ANNOTATION_PROCESSOR =
        "androidx.room:room-compiler:${Versions.ROOM}"

    /******************************************************/

    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"

    const val NAVIGATION_UI =
        "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"

    const val LEGACY_SUPPORT =
        "androidx.legacy:legacy-support-v4:${Versions.LEGACY}"

    const val TIMBER =
        "com.jakewharton.timber:timber:${Versions.TIMBER}"

    // google map
    const val SERVICE_MAP =
        "com.google.android.gms:play-services-maps:${Versions.MAP}"

    const val SERVICE_LOCATION =
        "com.google.android.gms:play-services-location:${Versions.LOCATION}"

    // easy permission

    const val EASY_PERMISSION =
        "pub.devrel:easypermissions:${Versions.EASY_PERMISSION}"

    // for lifecycle service
    const val LIFECYCLE_EXTENSION =
        "androidx.lifecycle:lifecycle-extensions:${Versions.LIFECYCLE_EXTENSION}"

    // statistic chart
    const val STATISTIC_CHART =
        "com.github.PhilJay:MPAndroidChart:${Versions.STATISTIC_CHART}"

    // glide

    const val GLIDE_CORE =
        "com.github.bumptech.glide:glide:${Versions.GLIDE}"

    const val GLIDE_COMPILE =
        "com.github.bumptech.glide:compiler:${Versions.GLIDE}"

    const val SPLASH_GIF =
        "pl.droidsonroids.gif:android-gif-drawable:${Versions.SPLASH_GIF}"





}