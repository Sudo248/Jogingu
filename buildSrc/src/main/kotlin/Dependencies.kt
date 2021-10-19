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

}