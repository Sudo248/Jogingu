import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories{
    mavenCentral()
}
plugins{
    `kotlin-dsl`
    kotlin("jvm") version "1.6.10"
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}