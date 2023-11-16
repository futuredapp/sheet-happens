# Sheet Happens! 🦄

Sheet Happens is Gradle plugin that lets you localize your Android / KMP application using Google Spreadsheets.

## Features
- Generates Android string resources from Google Spreadsheets
- Compatible with Android and Kotlin Multiplatform ([moko-resources](https://github.com/icerockdev/moko-resources)) resources format
- Flexible configuration options
- Can be applied to multiple modules separately (useful for splitting translations per feature module)

## Installation

Make sure you have Gradle Plugin Portal in your repositories:
```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
```

Then add the plugin to your module-level `build.gradle` or `build.gradle.kts` file:

```kotlin
plugins {
    id("app.futured.sheethappens") version "$latestVersion"
}
```

## Usage

Create a Google Spreadsheet and add your translations in it:

TBD

## Credits

This Gradle plugin was inspired by Ackee's [Spreadsheet Localizer INTELLIJ IDEA plugin](https://github.com/AckeeCZ/Spreadsheet-Localizer-Plugin) & it is fully compatible with their Google Sheet formatting. 