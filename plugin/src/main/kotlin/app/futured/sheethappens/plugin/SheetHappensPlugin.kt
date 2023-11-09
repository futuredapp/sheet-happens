package app.futured.sheethappens.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

private const val ExtensionName = "sheetHappens"
private const val TaskName = "localizationUpdate"

open class SheetHappensPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create(
            ExtensionName,
            SheetHappensExtension::class.java
        )

        project.tasks.register(TaskName, LocalizationUpdateTask::class.java) { task ->
            task.spreadsheetId.set(extension.spreadsheetId)
            task.sheetName.set(extension.sheetName)
            task.apiKey.set(extension.apiKey)
        }
    }
}