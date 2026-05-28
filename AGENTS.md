# AGENTS.md

This file provides guidance to AI agents when working with code in this repository.

## Project Overview

**Sheet Happens** is a Gradle plugin that generates Android/KMP string resources from Google Spreadsheets. It is published on the Gradle Plugin Portal under `app.futured.sheethappens`.

## Repository Structure

The repo is split into two independent Gradle projects:

- `plugin-build/` — the plugin itself (separate Gradle project with its own `settings.gradle.kts`)
- `sample/` — a sample Android app that consumes the plugin for manual testing

All plugin development happens inside `plugin-build/`.

## Commands

All plugin commands use `--project-dir plugin-build`:

```bash
# Build
./gradlew --project-dir plugin-build build

# Check (compiles, runs tests, detekt, ktlint)
./gradlew --project-dir plugin-build check

# Run tests only
./gradlew --project-dir plugin-build test

# Run a single test class
./gradlew --project-dir plugin-build test --tests "app.futured.sheethappens.SomeTest"

# Lint / static analysis
./gradlew --project-dir plugin-build detekt
./gradlew --project-dir plugin-build ktlintCheck

# Auto-format
./gradlew --project-dir plugin-build ktlintFormat

# Publish (requires GRADLE_PUBLISH_KEY and GRADLE_PUBLISH_SECRET env vars)
./gradlew --project-dir plugin-build setupPublishSecrets publishPlugins
```

## Architecture

### Data Flow

```
Google Sheets API (REST/JSON)
    ↓ GoogleSpreadsheetsApi
SpreadsheetResponse (API model)
    ↓ GoogleSheetParser
SheetEntry (domain model — sealed: Section | PlainResource | PluralResource)
    ↓ SheetEntryAccumulator
XmlElement (output model — sealed: PlainResource | PluralResource)
    ↓ ResourcesSerializer / XmlWriter
values[-locale]/strings.xml  +  values[-locale]/plurals.xml
```

### Key Files

- `plugin/SheetHappensPlugin.kt` — plugin entry point, registers the `makeSheetHappen` task
- `plugin/SheetHappensExtension.kt` — Gradle DSL configuration block
- `plugin/LocalizationUpdateTask.kt` — the Gradle task that orchestrates the full pipeline
- `localizer/GoogleSheetParser.kt` — parses spreadsheet rows into typed `SheetEntry` values
- `localizer/SheetEntryAccumulator.kt` — groups entries and resolves plurals
- `localizer/ResourcesSerializer.kt` — writes `XmlElement` trees to files
- `localizer/api/GoogleSpreadsheetsApi.kt` — HTTP client for the Sheets REST API
- `localizer/model/` — all domain and API models

### Plural Convention

Plural keys in the spreadsheet use the format `key##{quantifier}` (e.g., `plural_days##{one}`, `plural_days##{other}`). The parser splits on `##` to group plural forms.

### Shadow JAR

`kotlinx-serialization-json` is bundled via the Shadow plugin and relocated under a plugin-specific package to avoid classpath conflicts with consumer projects.

### Dependency Versions

Managed in `/gradle/libs.versions.toml` (version catalog). Plugin metadata (version, group, artifact) is in `/plugin-build/gradle.properties`.

## Code Style

- Line length: 130 characters (enforced by detekt and editorconfig)
- Indent: 4 spaces
- Detekt rules defined in `/config/detekt.yml`
- Use sealed interfaces for type hierarchies; avoid magic numbers

## Git Commit Conventions

- Imperative mood, sentence case, no trailing period (e.g. `Add service account authentication support`)
- No `Co-Authored-By` or other trailers
- Keep the subject line concise — describe *what* the commit does, not *why*

## Release Process

Refer to [RELEASE.md](RELEASE.md)
