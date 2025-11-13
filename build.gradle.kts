// build.gradle.kts (raíz del proyecto)
plugins {
    // Solo declaramos los plugins, no agregamos repositorios aquí
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

// NO agregues repositorios aquí; los dejamos en settings.gradle.kts
