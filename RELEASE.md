## Release process

This document describes release process of this plugin into `Gradle Plugin Portal` repository.
There is only one type of publication -- release. Snapshots
are [not supported](https://plugins.gradle.org/docs/publish-plugin) by Gradle Plugin Portal.

### Release

New release is published automatically when a new GitHub release is published. The release name is used as the plugin version.

CI run specification: [.github/workflows/publish.yml](.github/workflows/publish.yml)

### Local development

The `VERSION` in `plugin-build/gradle.properties` carries a `-SNAPSHOT` suffix by default. Gradle Plugin Portal does not support snapshot versions, so this value is intentionally never published — the CI workflow overrides it with the GitHub release name at publish time. The snapshot version is useful for local Maven publishing (`publishToMavenLocal`) while iterating on the plugin locally.