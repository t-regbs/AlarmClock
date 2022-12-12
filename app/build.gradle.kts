plugins {
  id("com.android.application")
  kotlin("android")
  jacoco
}

jacoco { toolVersion = "0.8.8" }

// ./gradlew test connectedDevelopDebugAndroidTest jacocoTestReport
// task must be created, examples in Kotlin which call tasks.jacocoTestReport do not work
tasks.create("jacocoTestReport", JacocoReport::class.java) {
  group = "Reporting"
  description = "Generate Jacoco coverage reports."

  reports {
    xml.isEnabled = true
    html.isEnabled = true
  }

  val fileFilter =
      listOf(
          "**/R.class",
          "**/R$*.class",
          "**/BuildConfig.*",
          "**/Manifest*.*",
          "**/*Test*.*",
          "android/**/*.*")

  val developDebug = "developDebug"

  sourceDirectories.setFrom(
      files(listOf("$projectDir/src/main/java", "$projectDir/src/main/kotlin")))
  classDirectories.setFrom(
      files(
          listOf(
              fileTree(
                  "dir" to "$buildDir/intermediates/javac/$developDebug", "excludes" to fileFilter),
              fileTree(
                  "dir" to "$buildDir/tmp/kotlin-classes/$developDebug",
                  "excludes" to fileFilter))))

  // execution data from both unit and instrumentation tests
  executionData.setFrom(
      fileTree(
          "dir" to project.buildDir,
          "includes" to
              listOf(
                  // unit tests
                  "jacoco/test${"developDebug".capitalize()}UnitTest.exec",
                  // instrumentation tests
                  "outputs/code_coverage/${developDebug}AndroidTest/connected/**/*.ec")))

  // dependsOn("test${"developDebug".capitalize()}UnitTest")
  // dependsOn("connected${"developDebug".capitalize()}AndroidTest")
}

tasks.withType(Test::class.java) {
  (this.extensions.getByName("jacoco") as JacocoTaskExtension).apply {
    isIncludeNoLocationClasses = true
    excludes = listOf("jdk.internal.*")
  }
}

val acraEmail =
    project.rootProject
        .file("local.properties")
        .let { if (it.exists()) it.readLines() else emptyList() }
        .firstOrNull { it.startsWith("acra.email") }
        ?.substringAfter("=")
        ?: System.getenv()["ACRA_EMAIL"] ?: ""

android {
  compileSdk = 31
  defaultConfig {
    versionCode = 31010
    versionName = "3.10.10"
    applicationId = "com.better.alarm"
    minSdk = 16
    targetSdk = 31
    testApplicationId = "com.better.alarm.test"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    multiDexEnabled = true
  }
  buildTypes {
    getByName("debug") {
      isTestCoverageEnabled = true
      buildConfigField("String", "ACRA_EMAIL", "\"$acraEmail\"")
      applicationIdSuffix = ".debug"
    }
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
      buildConfigField("String", "ACRA_EMAIL", "\"$acraEmail\"")
    }
  }

  flavorDimensions.add("default")

  productFlavors {
    create("develop") { applicationId = "com.better.alarm" }
    create("premium") { applicationId = "com.premium.alarm" }
  }

  installation {
    timeOutInMs = 20 * 60 * 1000 // 20 minutes
    installOptions("-d", "-t")
  }

  useLibrary("android.test.runner")
  useLibrary("android.test.base")
  useLibrary("android.test.mock")

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  testOptions { unitTests.isReturnDefaultValues = true }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions {
    freeCompilerArgs =
        freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn" + "-Xopt-in=kotlin.Experimental"

    jvmTarget = "1.8"
  }
}

dependencies {
  // App dependencies
  implementation(kotlin("stdlib", version = project.extra["kotlin"] as String))
  implementation("ch.acra:acra-mail:5.9.3")
  implementation("com.melnykov:floatingactionbutton:1.3.0")
  implementation("io.reactivex.rxjava2:rxjava:2.2.21")
  implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
  implementation("org.koin:koin-core:2.2.2")
  implementation("androidx.fragment:fragment:1.4.1")
  implementation("androidx.preference:preference:1.2.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2")
  implementation("com.google.android.material:material:1.6.1")
  implementation("org.slf4j:slf4j-api:2.0.6")
  implementation("com.github.tony19:logback-android:2.0.0")
  implementation("androidx.multidex:multidex:2.0.1")
}

dependencies {
  testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
  testImplementation("junit:junit:4.13.2")
  testImplementation("io.mockk:mockk:1.12.4")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.2")
}

dependencies {
  androidTestImplementation("com.squareup.assertj:assertj-android:1.2.0")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
  androidTestImplementation("androidx.test:runner:1.4.0")
  androidTestImplementation("androidx.test:rules:1.4.0")
  androidTestImplementation("androidx.test.ext:junit:1.1.3")
}
