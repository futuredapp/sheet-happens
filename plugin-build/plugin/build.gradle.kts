import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.gradle.pluginPublish)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

group = property("GROUP").toString()

gradlePlugin {
    website = property("WEBSITE").toString()
    vcsUrl = property("VCS_URL").toString()

    plugins {
        create(property("ID").toString()) {
            id = property("ID").toString()
            displayName = property("DISPLAY_NAME").toString()
            description = property("DESCRIPTION").toString()
            tags = listOf("localization", "translation", "android", "kmp")
            implementationClass = property("IMPLEMENTATION_CLASS").toString()
            version = property("VERSION").toString()
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
    implementation(libs.kotlinx.serialization.json)
}

tasks.test {
    useJUnitPlatform()
}

tasks.create("setupPublishSecrets") {
    doLast {
        val key = System.getenv("GRADLE_PUBLISH_KEY")
        val secret = System.getenv("GRADLE_PUBLISH_SECRET")

        if (key == null || secret == null) {
            throw GradleException("gradlePublishKey and/or gradlePublishSecret are not defined environment variables")
        }

        System.setProperty("gradle.publish.key", key)
        System.setProperty("gradle.publish.secret", secret)
    }
}

detekt {
    ignoreFailures = false
    source.setFrom(files(projectDir))
    config.setFrom(files("$rootDir/../config/detekt.yml"))
    buildUponDefaultConfig = true
}

tasks.withType<Detekt> {
    reports {
        sarif.required.set(false)
        md.required.set(false)
        txt.required.set(true)
        xml.required.set(true)
    }
}

ktlint {
    ignoreFailures.set(false)
    android.set(true)
    outputToConsole.set(true)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }
    filter {
        exclude("**/generated/**")
    }
}
