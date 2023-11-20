## Release process

This document describes release process of this plugin into `Gradle Plugin Portal` repository.
There is only one type of publication -- release. Snapshots
are [not supported](https://plugins.gradle.org/docs/publish-plugin) by Gradle Plugin Portal.

### Release

New release is published automatically when a new tag is created which should be done by creating a new release within
GitHub UI.

#### Before release

1. Update the plugin `VERSION` property in [gradle.properties](plugin-build/gradle.properties) file.
2. Update the plugin version in [README](README.md) file.

CI run specification: [.github/workflows/publish.yml](.github/workflows/publish.yml)