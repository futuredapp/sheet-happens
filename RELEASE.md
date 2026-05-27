## Release process

This document describes release process of this plugin into `Gradle Plugin Portal` repository.
There is only one type of publication -- release. Snapshots
are [not supported](https://plugins.gradle.org/docs/publish-plugin) by Gradle Plugin Portal.

### Release

New release is published automatically when a new GitHub release is published. The release name is used as the plugin version.

CI run specification: [.github/workflows/publish.yml](.github/workflows/publish.yml)