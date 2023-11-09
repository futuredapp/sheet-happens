package app.futured.sheethappens

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property

interface SheetHappensExtension {
    val sheetId: Property<String>
    val apiKey: Property<String>
}

class SheetHappensPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("SheetHappensPlugin was applied to project: ${project.name}")
        val extension = project.extensions.create("sheetHappens", SheetHappensExtension::class.java)
    }
}