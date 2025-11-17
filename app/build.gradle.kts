plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Plugin para habilitar Jetpack Compose en Kotlin
    id("org.jetbrains.kotlin.plugin.compose")
    // Plugin de serialización de Kotlin (kotlinx-serialization)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
    // KSP (Kotlin Symbol Processing) para generar código (por ejemplo, Room)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
}

android {
    namespace = "com.example.calendary"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.calendary"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Resolver conflicto de dependencias de annotations
    configurations.all {
        resolutionStrategy {
            // Fuerza una versión específica de org.jetbrains:annotations para evitar conflictos
            force("org.jetbrains:annotations:23.0.0")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Versión de Java usada para compilar
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        // Target de JVM para Kotlin
        jvmTarget = "11"
    }
    buildFeatures {
        // Habilita Jetpack Compose
        compose = true
    }
}

dependencies {
    // ─────────────────────────────────────────────
    // CORE ANDROID / LIFECYCLE / ACTIVITY / NAV
    // ─────────────────────────────────────────────

    // Extensiones KTX para APIs base de Android (Context, SharedPreferences, etc.)
    implementation("androidx.core:core-ktx:1.13.1")

    // Integración de corrutinas con el ciclo de vida (lifecycleScope, etc.)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // Activity para Compose: permite usar setContent { } y manejar el ciclo de vida con Compose
    implementation("androidx.activity:activity-compose:1.9.3")

    // Navegación con Jetpack Compose (NavHost, composable<>, etc.)
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // Coil: carga de imágenes asíncronas en Compose (Image con rememberAsyncImagePainter)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Íconos extendidos de Material (ej: Icons.Filled.* adicionales)
    implementation("androidx.compose.material:material-icons-extended:1.7.2")

    // Serialización JSON con Kotlinx Serialization (para @Serializable, Json.encode/decode)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // ─────────────────────────────────────────────
    // COMPOSE BOM + UI + MATERIAL 3
    // ─────────────────────────────────────────────

    // BOM de Compose: alinea versiones de todos los artefactos de Compose
    implementation(platform("androidx.compose:compose-bom:2024.09.03"))

    // Módulo base de UI de Compose
    implementation("androidx.compose.ui:ui")

    // Utilidades de gráficos (colores, paths, etc.) para Compose
    implementation("androidx.compose.ui:ui-graphics")

    // Soporte para previsualización de composables en Android Studio
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Componentes Material 3 (Scaffold, TopAppBar, Buttons, etc.) para Compose
    implementation("androidx.compose.material3:material3")

    // ─────────────────────────────────────────────
    // GOOGLE MAPS Y LOCATION SERVICES
    // ─────────────────────────────────────────────

    // SDK de Google Maps para Android (MapView, GoogleMap, marcadores, etc.)
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Servicios de ubicación (LocationServices, FusedLocationProviderClient, etc.)
    implementation("com.google.android.gms:play-services-location:21.1.0")

    // Maps Compose: wrappers de Jetpack Compose para Google Maps (si quisieras usar mapas 100% Compose)
    implementation("com.google.maps.android:maps-compose:4.3.0")

    // ─────────────────────────────────────────────
    // WORKMANAGER (NOTIFICACIONES / TAREAS EN SEGUNDO PLANO)
    // ─────────────────────────────────────────────

    // WorkManager KTX: ejecutar trabajos en background (notificaciones programadas, tareas periódicas)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // ─────────────────────────────────────────────
    // ROOM DATABASE (PERSISTENCIA LOCAL)
    // ─────────────────────────────────────────────

    // Runtime de Room: acceso a base de datos SQLite con DAOs y entidades
    implementation("androidx.room:room-runtime:2.6.1")

    // Extensiones KTX para Room (suspend fun en DAOs, Flow, etc.)
    implementation("androidx.room:room-ktx:2.6.1")

    // Room Compiler con KSP: genera el código de los DAOs y la base de datos
    ksp("androidx.room:room-compiler:2.6.1")

    // ─────────────────────────────────────────────
    // VIEWMODEL + LIFECYCLE PARA COMPOSE
    // ─────────────────────────────────────────────

    // ViewModel para Compose (viewModel() en composables)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // Integración de lifecycle con Compose (collectAsStateWithLifecycle, etc.)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

    // ─────────────────────────────────────────────
    // TESTING
    // ─────────────────────────────────────────────

    // JUnit 4 para pruebas unitarias
    testImplementation("junit:junit:4.13.2")

    // Dependencias de test para UI Compose (instrumentation tests)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.03"))
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Herramientas de debug para Compose (preview, inspección de layout)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ─────────────────────────────────────────────
    // GOOGLE MAPS Y LOCATION (DUPLICADOS CON OTRA VERSIÓN)
    // *OJO*: aquí tienes nuevamente Maps y Location con otra versión de Location
    // ─────────────────────────────────────────────

    // SDK de Google Maps (repetido, misma versión que arriba)
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Play Services Location (versión ligeramente diferente a la de arriba: 21.0.1 vs 21.1.0)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // ─────────────────────────────────────────────
    // COROUTINES
    // ─────────────────────────────────────────────

    // Corrutinas en Android (Dispatchers.Main, launch, withContext, etc.)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ... tus otras dependencias existentes
}
