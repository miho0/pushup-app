pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
        // Add other repositories if needed
        google()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = "PushupApp"
include(":app")
 